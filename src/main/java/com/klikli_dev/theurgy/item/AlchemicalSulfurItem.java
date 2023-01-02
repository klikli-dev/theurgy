/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.item;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.client.render.SulfurBEWLR;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Consumer;

public class AlchemicalSulfurItem extends Item {
    public AlchemicalSulfurItem(Properties pProperties) {
        super(pProperties);
    }

    public static ItemStack getSourceStack(ItemStack sulfurStack) {
        if (sulfurStack.hasTag() && sulfurStack.getTag().contains(TheurgyConstants.Nbt.SULFUR_SOURCE_ID)) {
            var itemId = new ResourceLocation(sulfurStack.getTag().getString(TheurgyConstants.Nbt.SULFUR_SOURCE_ID));
            var sourceStack = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId));

            if (sulfurStack.getTag().contains(TheurgyConstants.Nbt.SULFUR_SOURCE_NBT))
                sourceStack.setTag(sulfurStack.getTag().getCompound(TheurgyConstants.Nbt.SULFUR_SOURCE_NBT));
            return sourceStack;
        }

        return ItemStack.EMPTY;
    }

    public static List<MutableComponent> getTooltipData(ItemStack sulfurStack) {
        var source = getSourceStack(sulfurStack);

        if (!source.isEmpty() && source.getHoverName() instanceof MutableComponent hoverName)
            return ImmutableList.of(hoverName.withStyle(Style.EMPTY
                    .withColor(ChatFormatting.GREEN)
                    .withItalic(true)
            ));

        return ImmutableList.of();
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return SulfurBEWLR.get();
            }
        });
    }

    @Override
    public Component getName(ItemStack pStack) {
        var source = getSourceStack(pStack);

        if (!source.isEmpty() && source.getHoverName() instanceof MutableComponent hoverName)
            return new TranslatableComponent(this.getDescriptionId(), ComponentUtils.wrapInSquareBrackets(
                    hoverName.withStyle(Style.EMPTY
                            .withColor(ChatFormatting.GREEN)
                            .withItalic(true)
                    )));

        return super.getName(pStack);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            DistHelper.fillItemCategory(this, tab, items);
        }
    }

    /**
     * Inner class to avoid classloading issues on the server
     */
    public static class DistHelper {
        public static void fillItemCategory(AlchemicalSulfurItem item, CreativeModeTab tab, NonNullList<ItemStack> items) {
            if(tab != Theurgy.CREATIVE_MODE_TAB && tab != CreativeModeTab.TAB_SEARCH)
                return;

            var level = Minecraft.getInstance().level;
            if (level != null) {
                var recipeManager = level.getRecipeManager();
                recipeManager.getRecipes().forEach((recipe) -> {
                    if (recipe.getResultItem() != null && recipe.getResultItem().getItem() == item) {
                        //should not have to check for null here, but in the case of https://github.com/klikli-dev/theurgy/issues/68 we actually get a null ResultItem
                        items.add(recipe.getResultItem().copy());
                    }
                });
            }
        }
    }
}
