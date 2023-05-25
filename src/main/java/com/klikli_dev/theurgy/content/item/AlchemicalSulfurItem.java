/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.item;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.renderer.SulfurBEWLR;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.util.LevelUtil;
import com.klikli_dev.theurgy.util.TagUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.function.Consumer;

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
     * if true, will use TheurgyConstants.Nbt.SULFUR_SOURCE_ID to provide a parameter to the item tooltip component that can be accessed with %s in the language file.
     */
    public boolean provideAutomaticTooltipData;

    /**
     * If true will convert the tag to a description id and use it as source name, instead of the source stack procured for the tag.
     * This is mainly intended for sulfurs that actually represent a tag (such as "all logs").
     * It should not be used for sulfurs that represent a specific item where we use the tag to handle mod compat (e.g. "silver ingot").
     */
    public boolean overrideTagSourceName;

    /**
     * If true the LiquefactionRecipe used to craft this item will automatically generate a source id based on the ingredient, if no source id is provided in the recipe result nbt.
     */
    public boolean autoGenerateSourceIdInRecipe;

    public AlchemicalSulfurItem(Properties pProperties) {
        super(pProperties);
        this.useAutomaticIconRendering = true;
        this.useAutomaticNameRendering = true;
        this.provideAutomaticTooltipData = true;
        this.autoGenerateSourceIdInRecipe = true;
        this.overrideTagSourceName = false;
    }

    public static String getSourceItemId(ItemStack sulfurStack) {
        if(!sulfurStack.hasTag()){
            var level = LevelUtil.getLevelWithoutContext();
            var recipeManager = level == null ? null : level.getRecipeManager();
            var registryAccess = level == null ? null : level.registryAccess();

            if (recipeManager != null) {
                var liquefactionRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get()).stream().filter(r -> r.getResultItem(registryAccess) != null).toList();

                var sulfurWithNbt = liquefactionRecipes.stream()
                        .filter(recipe -> recipe.getResultItem(registryAccess) != null && recipe.getResultItem(registryAccess).getItem() == sulfurStack.getItem()).findFirst().map(recipe -> recipe.getResultItem(registryAccess));

                if (sulfurWithNbt.isPresent() && sulfurWithNbt.get().hasTag()) {
                    sulfurStack.setTag(sulfurWithNbt.get().getTag());
                }
            }
        }

        if (sulfurStack.hasTag() && sulfurStack.getTag().contains(TheurgyConstants.Nbt.SULFUR_SOURCE_ID)) {
            return sulfurStack.getTag().getString(TheurgyConstants.Nbt.SULFUR_SOURCE_ID);
        }

        return "";
    }

    /**
     * Get the source item stack from the sulfur stack nbt.
     * The source *should* be the item that was used to create the sulfur.
     * Due to this only being used for automatic rendering and naming purposes, the source might be a different item for some reason, and in many cases could be empty.
     */
    public static ItemStack getSourceStack(ItemStack sulfurStack) {
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
                sourceStack = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId));
            }

            if (sulfurStack.getTag().contains(TheurgyConstants.Nbt.SULFUR_SOURCE_NBT))
                sourceStack.setTag(sulfurStack.getTag().getCompound(TheurgyConstants.Nbt.SULFUR_SOURCE_NBT));
            return sourceStack;
        }

        return ItemStack.EMPTY;
    }

    public static List<MutableComponent> getTooltipData(ItemStack sulfurStack) {
        if (sulfurStack.getItem() instanceof AlchemicalSulfurItem sulfur && sulfur.provideAutomaticTooltipData) {
            return ImmutableList.of(sulfur.getSourceName(sulfurStack));
        }

        return ImmutableList.of();
    }

    public static MutableComponent formatSourceName(MutableComponent sourceName) {
        return sourceName.withStyle(Style.EMPTY
                .withColor(ChatFormatting.GREEN)
                .withItalic(true)
        );
    }

    public AlchemicalSulfurItem noAuto() {
        this.useAutomaticIconRendering = false;
        this.useAutomaticNameRendering = false;
        this.provideAutomaticTooltipData = false;
        this.overrideTagSourceName = false;
        return this;
    }

    public AlchemicalSulfurItem autoIcon(boolean value) {
        this.useAutomaticIconRendering = value;
        return this;
    }

    public AlchemicalSulfurItem autoName(boolean value) {
        this.useAutomaticNameRendering = value;
        return this;
    }

    public AlchemicalSulfurItem autoTooltip(boolean value) {
        this.provideAutomaticTooltipData = value;
        return this;
    }

    public AlchemicalSulfurItem overrideTagSourceName(boolean value) {
        this.overrideTagSourceName = value;
        return this;
    }

    public AlchemicalSulfurItem autoGenerateSourceIdInRecipe(boolean value) {
        this.autoGenerateSourceIdInRecipe = value;
        return this;
    }

    public MutableComponent getSourceName(ItemStack pStack) {
        var source = getSourceStack(pStack);

        if (!source.isEmpty()) {
            var sourceId = getSourceItemId(pStack);
            if (sourceId.startsWith("#") && this.overrideTagSourceName) {
                var tagId = new ResourceLocation(sourceId.substring(1));
                return formatSourceName(Component.translatable(Util.makeDescriptionId("tag", tagId)));
            }

            if (source.getHoverName() instanceof MutableComponent hoverName)
                return formatSourceName(hoverName);
        }

        return Component.translatable(TheurgyConstants.I18n.Item.ALCHEMICAL_SULFUR_UNKNOWN_SOURCE);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return SulfurBEWLR.get();
            }
        });
    }

    @Override
    public Component getName(ItemStack pStack) {
        if (this.useAutomaticNameRendering) {
            return Component.translatable(this.getDescriptionId(pStack), ComponentUtils.wrapInSquareBrackets(
                    this.getSourceName(pStack)
            ));
        }
        return super.getName(pStack);
    }
}
