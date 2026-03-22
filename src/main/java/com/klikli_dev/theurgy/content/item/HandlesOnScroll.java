// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface HandlesOnScroll {
    void onScroll(Player player, ItemStack stack, int shift);
}
