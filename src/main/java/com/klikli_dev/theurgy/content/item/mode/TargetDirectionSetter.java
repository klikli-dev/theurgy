// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mode;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public interface TargetDirectionSetter {
    void targetDirection(Direction direction);

    Direction targetDirection();

    /**
     * This is used for the visualization of the current target direction in the {@link com.klikli_dev.theurgy.content.item.mercurialwand.mode.SelectDirectionMode}
     */
    BlockPos targetPos();
}
