// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.render.AlchemicalSulfurBEWLR;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.klikli_dev.theurgy.util.LevelUtil;
import com.klikli_dev.theurgy.util.TagUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AlchemicalSulfurItem extends Item {
    /**
     * if true, an item model based on builtin/entity will be generated for this item.
     * This will cause SulfurBEWLR to be used to render the item dynamically based on TheurgyConstants.Nbt.SULFUR_SOURCE_ID, instead of a json model.
     * <p>
     * Note: this variable cannot be used to conditionally register an IClientItemExtensions,
     * because the registration happens in the Item constructor, before this variable is set.
     * However, it is also not necessary, because the render behaviour is governed by the item model anyway.
     */
    public boolean useAutomaticIconRendering;
    /**
     * if true, will use TheurgyConstants.Nbt.SULFUR_SOURCE_ID to provide a parameter to the item name component that can be accessed with %s in the language file.
     */
    public boolean useAutomaticNameRendering;
    /**
     * If true, data gen will generate a lang entry for the item name.
     */
    public boolean useAutomaticNameLangGeneration;
    /**
     * if true, will use TheurgyConstants.Nbt.SULFUR_SOURCE_ID to provide a parameter to the item tooltip component that can be accessed with %s in the language file.
     */
    public boolean provideAutomaticTooltipData;

    /**
     * If true, data gen will generate a lang entry for the item tooltip.
     */
    public boolean useAutomaticTooltipLangGeneration;

    /**
     * If true will convert the tag to a description id and use it as source name, instead of the source stack procured for the tag.
     * This is mainly intended for sulfurs that actually represent a tag (such as "all logs").
     * It should not be used for sulfurs that represent a specific item where we use the tag to handle mod compat (e.g. "Tingleberry").
     */
    public boolean overrideTagSourceName;

    /**
     * If true will use the source name from the lang file, instead of the source stack.
     * Additionally, this will cause overrideTagSourceName to be ignored.
     */
    public boolean overrideSourceName;

    /**
     * If true the LiquefactionRecipe used to craft this item will automatically generate a source id based on the ingredient, if no source id is provided in the recipe result nbt.
     */
    public boolean autoGenerateSourceIdInRecipe;

    public Supplier<ItemStack> sourceStackSupplier;
    public Supplier<ItemStack> emptyJarStackSupplier;

    public AlchemicalSulfurTier tier;
    public AlchemicalSulfurType type;

    public AlchemicalSulfurItem(Properties pProperties) {
        this(pProperties, Suppliers.memoize(() -> ItemStack.EMPTY));
    }

    public AlchemicalSulfurItem(Properties pProperties, Supplier<ItemStack> sourceStackSupplier) {
        super(pProperties);
        this.useAutomaticIconRendering = true;
        this.useAutomaticNameRendering = true;
        this.useAutomaticNameLangGeneration = true;
        this.provideAutomaticTooltipData = true;
        this.useAutomaticTooltipLangGeneration = true;
        this.autoGenerateSourceIdInRecipe = true;
        this.overrideTagSourceName = false;
        this.overrideSourceName = false;
        this.sourceStackSupplier = sourceStackSupplier;
        this.emptyJarStackSupplier = Suppliers.memoize(() -> new ItemStack(ItemRegistry.EMPTY_JAR_ICON.get()));
        this.tier = AlchemicalSulfurTier.ABUNDANT;
        this.type = AlchemicalSulfurType.MISC;
    }

    public static String getSourceItemId(ItemStack sulfurStack) {
        if (!sulfurStack.hasTag()) {
            var level = LevelUtil.getLevelWithoutContext();
            var recipeManager = level == null ? null : level.getRecipeManager();
            var registryAccess = level == null ? null : level.registryAccess();

            if (recipeManager != null && sulfurStack.getItem() instanceof AlchemicalSulfurItem sulfur) {
                var liquefactionRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get()).stream().filter(r -> r.value().getResultItem(registryAccess) != null).toList();

                var preferred = SulfurRegistry.getPreferredSulfurVariant(sulfur, liquefactionRecipes, level);
                if (preferred.isPresent() && preferred.get().hasTag()) {
                    sulfurStack.setTag(preferred.get().getTag());
                }
            }
        }

        if (sulfurStack.hasTag() && sulfurStack.getTag().contains(TheurgyConstants.Nbt.SULFUR_SOURCE_ID)) {
            return sulfurStack.getTag().getString(TheurgyConstants.Nbt.SULFUR_SOURCE_ID);
        }

        return "";
    }

    public static ItemStack getEmptyJarStack(ItemStack sulfurStack) {

        if (sulfurStack.getItem() instanceof AlchemicalSulfurItem sulfur) {
            return sulfur.emptyJarStackSupplier.get();
        }

        return ItemStack.EMPTY;
    }

    public static AlchemicalSulfurTier getTier(ItemStack sulfurStack) {
        if (sulfurStack.getItem() instanceof AlchemicalSulfurItem sulfur) {
            return sulfur.tier;
        }

        return AlchemicalSulfurTier.ABUNDANT;
    }

    public static AlchemicalSulfurType getType(ItemStack sulfurStack) {
        if (sulfurStack.getItem() instanceof AlchemicalSulfurItem sulfur) {
            return sulfur.type;
        }

        return AlchemicalSulfurType.MISC;
    }

    /**
     * Get the source item stack from the sulfur stack nbt.
     * The source *should* be the item that was used to create the sulfur.
     * Due to this only being used for automatic rendering and naming purposes, the source might be a different item for some reason, and in many cases could be empty.
     */
    public static ItemStack getSourceStack(ItemStack sulfurStack) {

        if (sulfurStack.getItem() instanceof AlchemicalSulfurItem sulfur) {
            var sourceStack = sulfur.getSourceStack();
            if (!sourceStack.isEmpty())
                return sourceStack;
        }

        var itemSourceId = getSourceItemId(sulfurStack); //we call this first, because it might find the source for us

        //but then we do our normal checks
        if (sulfurStack.hasTag() && sulfurStack.getTag().contains(TheurgyConstants.Nbt.SULFUR_SOURCE_ID)) {
            ItemStack sourceStack;

            if (itemSourceId.startsWith("#")) {
                var tagId = new ResourceLocation(itemSourceId.substring(1));
                var tag = TagKey.create(Registries.ITEM, tagId);
                sourceStack = TagUtil.getItemStackForTag(tag);
            } else {
                var itemId = new ResourceLocation(itemSourceId);
                sourceStack = new ItemStack(BuiltInRegistries.ITEM.get(itemId));
            }

            if (sulfurStack.getTag().contains(TheurgyConstants.Nbt.SULFUR_SOURCE_NBT))
                sourceStack.setTag(sulfurStack.getTag().getCompound(TheurgyConstants.Nbt.SULFUR_SOURCE_NBT));
            return sourceStack;
        }

        return ItemStack.EMPTY;
    }

    public static List<MutableComponent> getTooltipData(ItemStack sulfurStack) {
        if (sulfurStack.getItem() instanceof AlchemicalSulfurItem sulfur && sulfur.provideAutomaticTooltipData) {
            var tier = getTier(sulfurStack);
            var type = getType(sulfurStack);
            return ImmutableList.of(
                    sulfur.getSourceName(sulfurStack),
                    ComponentUtils.wrapInSquareBrackets(
                            Component.translatable(tier.descriptionId())
                                    .withStyle(Style.EMPTY
                                            .withColor(tier.color)
                                            .withItalic(true))
                    ),
                    ComponentUtils.wrapInSquareBrackets(
                            Component.translatable(type.descriptionId())
                                    .withStyle(Style.EMPTY
                                            .withColor(ChatFormatting.DARK_GRAY)
                                            .withItalic(true))
                    )
            );
        }

        return ImmutableList.of();
    }

    public static MutableComponent formatSourceName(MutableComponent sourceName, AlchemicalSulfurTier tier) {
        return sourceName.withStyle(Style.EMPTY
                .withColor(tier.color())
                .withItalic(true)
        );
    }

    public AlchemicalSulfurItem tier(AlchemicalSulfurTier tier) {
        this.tier = tier;
        return this;
    }

    public AlchemicalSulfurTier tier() {
        return this.tier;
    }

    public AlchemicalSulfurItem type(AlchemicalSulfurType type) {
        this.type = type;
        return this;
    }

    public AlchemicalSulfurType type() {
        return this.type;
    }

    public ItemStack getSourceStack() {
        return this.sourceStackSupplier.get();
    }

    public AlchemicalSulfurItem noAuto() {
        this.useAutomaticIconRendering = false;
        this.useAutomaticNameRendering = false;
        this.useAutomaticNameLangGeneration = false;
        this.provideAutomaticTooltipData = false;
        this.useAutomaticTooltipLangGeneration = false;
        this.overrideTagSourceName = false;
        return this;
    }

    public AlchemicalSulfurItem autoIcon(boolean value) {
        this.useAutomaticIconRendering = value;
        return this;
    }

    public AlchemicalSulfurItem withJarIcon(Supplier<ItemStack> emptyJarStackSupplier) {
        this.emptyJarStackSupplier = emptyJarStackSupplier;
        return this;
    }

    public AlchemicalSulfurItem autoName(boolean value) {
        return this.autoName(value, value);
    }

    public AlchemicalSulfurItem autoName(boolean rendering, boolean langGeneration) {
        this.useAutomaticNameRendering = rendering;
        this.useAutomaticNameLangGeneration = langGeneration;
        return this;
    }

    public AlchemicalSulfurItem autoTooltip(boolean value) {
        return this.autoTooltip(value, value);
    }

    public AlchemicalSulfurItem autoTooltip(boolean rendering, boolean langGeneration) {
        this.provideAutomaticTooltipData = rendering;
        this.useAutomaticTooltipLangGeneration = langGeneration;
        return this;
    }

    public AlchemicalSulfurItem overrideTagSourceName(boolean value) {
        this.overrideTagSourceName = value;
        return this;
    }

    public AlchemicalSulfurItem overrideSourceName(boolean value) {
        this.overrideSourceName = value;
        this.autoGenerateSourceIdInRecipe = !value;
        return this;
    }

    public AlchemicalSulfurItem autoGenerateSourceIdInRecipe(boolean value) {
        this.autoGenerateSourceIdInRecipe = value;
        return this;
    }

    public MutableComponent getSourceName(ItemStack pStack) {
        if (this.overrideSourceName) {
            return formatSourceName(Component.translatable(pStack.getDescriptionId() + TheurgyConstants.I18n.Item.ALCHEMICAL_SULFUR_SOURCE_SUFFIX), this.tier);
        }

        var source = getSourceStack(pStack);

        if (!source.isEmpty()) {
            var sourceId = getSourceItemId(pStack);
            if (sourceId.startsWith("#") && this.overrideTagSourceName) {
                var tagId = new ResourceLocation(sourceId.substring(1));
                return formatSourceName(Component.translatable(Util.makeDescriptionId("tag", tagId)), this.tier);
            }

            if (source.getHoverName() instanceof MutableComponent hoverName)
                return formatSourceName(hoverName, this.tier);
        }

        return Component.translatable(TheurgyConstants.I18n.Item.ALCHEMICAL_SULFUR_UNKNOWN_SOURCE);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return AlchemicalSulfurBEWLR.get();
            }
        });
    }

    @Override
    public Component getName(ItemStack pStack) {
        if (this.useAutomaticNameRendering) {
            var tier = getTier(pStack);
            var type = getType(pStack);
            return Component.translatable(this.getDescriptionId(pStack),
                    ComponentUtils.wrapInSquareBrackets(
                            this.getSourceName(pStack)
                    ),
                    ComponentUtils.wrapInSquareBrackets(
                            Component.translatable(tier.descriptionId())
                                    .withStyle(Style.EMPTY
                                            .withColor(tier.color)
                                            .withItalic(true))
                    ),
                    ComponentUtils.wrapInSquareBrackets(
                            Component.translatable(type.descriptionId())
                                    .withStyle(Style.EMPTY
                                            .withColor(ChatFormatting.DARK_GRAY)
                                            .withItalic(true))
                    )
            );
        }
        return super.getName(pStack);
    }
}
