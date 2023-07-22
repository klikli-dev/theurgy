/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.item;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.theurgy.TheurgyConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AlchemicalSaltItem extends Item {
    public AlchemicalSaltItem(Properties pProperties) {
        super(pProperties);
    }

    public static List<MutableComponent> getTooltipData(ItemStack saltItem) {
        if (saltItem.getItem() instanceof AlchemicalSaltItem salt) {
            return ImmutableList.of(salt.getSourceName(saltItem));
        }

        return ImmutableList.of();
    }

    public static MutableComponent formatSourceName(MutableComponent sourceName) {
        return sourceName.withStyle(Style.EMPTY
                .withColor(ChatFormatting.GREEN)
                .withItalic(true)
        );
    }

    public MutableComponent getSourceName(ItemStack pStack) {
        return formatSourceName(Component.translatable(pStack.getDescriptionId() + TheurgyConstants.I18n.Item.ALCHEMICAL_SALT_SOURCE_SUFFIX));
    }

    @Override
    public Component getName(ItemStack pStack) {
        return Component.translatable(this.getDescriptionId(pStack), ComponentUtils.wrapInSquareBrackets(
                this.getSourceName(pStack)
        ));
    }
}
