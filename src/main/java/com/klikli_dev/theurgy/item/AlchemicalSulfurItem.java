/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.item;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.client.render.SulfurBEWLR;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
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

    public static void registerCreativeModeTabs(AlchemicalSulfurItem item, CreativeModeTab.Output output) {
        var level = Minecraft.getInstance().level;
        if (level != null) {
            var recipeManager = level.getRecipeManager();
            recipeManager.getRecipes().forEach((recipe) -> {
                if (recipe.getResultItem().getItem() == item) {
                    output.accept(recipe.getResultItem().copy());
                }
            });
        }
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
        var source = getSourceStack(pStack);

        if (!source.isEmpty() && source.getHoverName() instanceof MutableComponent hoverName)
            return Component.translatable(this.getDescriptionId(), ComponentUtils.wrapInSquareBrackets(
                    hoverName.withStyle(Style.EMPTY
                            .withColor(ChatFormatting.GREEN)
                            .withItalic(true)
                    )));

        return super.getName(pStack);
    }
}
