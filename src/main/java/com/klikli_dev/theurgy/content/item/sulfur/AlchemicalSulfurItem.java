// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.sulfur;

import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AlchemicalSulfurItem extends AlchemicalDerivativeItem {
    public AlchemicalSulfurType type;

    public AlchemicalSulfurItem(Properties pProperties) {
        super(pProperties);
        this.type = AlchemicalSulfurType.MISC;
    }

    public static AlchemicalSulfurType getType(ItemStack sulfurStack) {
        if (sulfurStack.getItem() instanceof AlchemicalSulfurItem sulfur) {
            return sulfur.type;
        }

        return AlchemicalSulfurType.MISC;
    }

    public AlchemicalSulfurItem ofTag(TagKey<Item> source, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        var item = new AlchemicalSulfurItem(new Item.Properties()
                .component(
                        DataComponentRegistry.SOURCE_TAG,
                        source
                ));
        //tags need to override the source name
        item.useCustomSourceName(true);
        item.tier(tier);
        item.type(type);
        return item;
    }

    public AlchemicalSulfurItem ofTag(TagKey<Item> source, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        var item = new AlchemicalSulfurItem(new Item.Properties()
                .component(
                        DataComponentRegistry.SOURCE_TAG,
                        source
                ));
        //tags need to override the source name
        item.useCustomSourceName(true);
        item.tier(tier);
        item.type(type);
        return item;
    }

    public AlchemicalSulfurItem ofItem(Item sourceStack, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        //noinspection deprecation
        var item = new AlchemicalSulfurItem(new Item.Properties()
                .component(
                        DataComponentRegistry.SOURCE_ITEM,
                        sourceStack.builtInRegistryHolder()
                ));
        item.tier(tier);
        item.type(type);
        return item;
    }

    @Override
    public List<MutableComponent> getTooltipData(ItemStack sulfurStack) {
        var result = super.getTooltipData(sulfurStack);
        if (this.provideAutomaticTooltipData) {
            result.add(ComponentUtils.wrapInSquareBrackets(
                    Component.translatable(this.type().descriptionId())
                            .withStyle(Style.EMPTY
                                    .withColor(ChatFormatting.DARK_GRAY)
                                    .withItalic(true))
            ));
        }

        return result;
    }

    public AlchemicalSulfurItem type(AlchemicalSulfurType type) {
        this.type = type;
        return this;
    }

    public AlchemicalSulfurType type() {
        return this.type;
    }

    @Override
    public List<MutableComponent> getNameData(ItemStack stack) {
        var result = super.getNameData(stack);

        if (this.useAutomaticNameRendering) {
            result.add(ComponentUtils.wrapInSquareBrackets(
                    Component.translatable(this.type.descriptionId())
                            .withStyle(Style.EMPTY
                                    .withColor(ChatFormatting.DARK_GRAY)
                                    .withItalic(true))
            ));
        }

        return result;
    }
}
