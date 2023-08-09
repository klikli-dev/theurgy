// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.tooltips;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface TooltipDataProvider {
    List<MutableComponent> getTooltipData(ItemStack pStack);
}
