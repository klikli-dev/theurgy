// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface HandlesOnLeftClick {

    boolean onLeftClickBlock(Level level, Player player, InteractionHand hand, BlockPos pos, Direction direction);

    /**
     * No return value, as the empty click is not cancellable.
     */
    void onLeftClickEmpty(Level level, Player player, InteractionHand hand);
}
