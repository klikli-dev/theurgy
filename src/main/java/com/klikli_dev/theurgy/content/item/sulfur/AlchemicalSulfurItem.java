// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.sulfur;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.sulfur.render.AlchemicalSulfurBEWLR;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.util.TagUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AlchemicalSulfurItem extends Item {
    private static final Map<Holder<Item>, ItemStack> sulfurSourceItemCache = new Object2ObjectOpenHashMap<>();
    private static final Map<TagKey<Item>, ItemStack> sulfurSourceTagCache = new Object2ObjectOpenHashMap<>();
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
    public Supplier<ItemStack> emptyJarStackSupplier;
    public AlchemicalSulfurTier tier;

    //TODO: Rework sulfur source handling
    //      if items can support default data components, we can probably provide all sulfurs with the proper source as component
    //      ideally we should remove all "get from recipe" handling
    //      the only exception can maybe be JEI? that should reflect recipe changes
    //      although the question is if recipes ever change the sulfur source - probably not!
    public AlchemicalSulfurType type;

    public AlchemicalSulfurItem(Properties pProperties) {
        super(pProperties);
        this.useAutomaticIconRendering = true;
        this.useAutomaticNameRendering = true;
        this.useAutomaticNameLangGeneration = true;
        this.provideAutomaticTooltipData = true;
        this.useAutomaticTooltipLangGeneration = true;
        this.autoGenerateSourceIdInRecipe = true;
        this.overrideTagSourceName = false;
        this.overrideSourceName = false;
        this.emptyJarStackSupplier = Suppliers.memoize(() -> new ItemStack(ItemRegistry.EMPTY_JAR_ICON.get()));
        this.tier = AlchemicalSulfurTier.ABUNDANT;
        this.type = AlchemicalSulfurType.MISC;
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

        if (sulfurStack.has(DataComponentRegistry.SULFUR_SOURCE_ITEM))
            return sulfurSourceItemCache.computeIfAbsent(sulfurStack.get(DataComponentRegistry.SULFUR_SOURCE_ITEM), ItemStack::new);


        if (sulfurStack.has(DataComponentRegistry.SULFUR_SOURCE_TAG))
            return sulfurSourceTagCache.computeIfAbsent(sulfurStack.get(DataComponentRegistry.SULFUR_SOURCE_TAG), TagUtil::getItemStackForTag);

        return ItemStack.EMPTY;
    }

    public static ItemStack getSourceStack(Item sulfur) {

        if (sulfur.components().has(DataComponentRegistry.SULFUR_SOURCE_ITEM.get()))
            return sulfurSourceItemCache.computeIfAbsent(sulfur.components().get(DataComponentRegistry.SULFUR_SOURCE_ITEM.get()), ItemStack::new);


        if (sulfur.components().has(DataComponentRegistry.SULFUR_SOURCE_TAG.get()))
            return sulfurSourceTagCache.computeIfAbsent(sulfur.components().get(DataComponentRegistry.SULFUR_SOURCE_TAG.get()), TagUtil::getItemStackForTag);

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
            if (pStack.has(DataComponentRegistry.SULFUR_SOURCE_TAG) && this.overrideTagSourceName) {
                return formatSourceName(Component.translatable(Util.makeDescriptionId("tag", pStack.get(DataComponentRegistry.SULFUR_SOURCE_TAG).location())), this.tier);
            }

            if (source.getHoverName() instanceof MutableComponent hoverName)
                return formatSourceName(hoverName, this.tier);
        }

        return Component.translatable(TheurgyConstants.I18n.Item.ALCHEMICAL_SULFUR_UNKNOWN_SOURCE);
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
