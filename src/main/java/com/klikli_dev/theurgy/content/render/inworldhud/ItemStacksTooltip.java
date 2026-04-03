// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record ItemStacksTooltip(List<ItemStack> items) implements TooltipComponent {

    private static final int MAX_ITEMS = 9;

    public ItemStacksTooltip {
        items = items.stream().filter(stack -> !stack.isEmpty()).limit(MAX_ITEMS).map(ItemStack::copy).toList();
    }
}
