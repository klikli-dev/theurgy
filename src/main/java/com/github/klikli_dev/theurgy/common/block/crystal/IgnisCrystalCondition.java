/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.common.block.crystal;

import com.github.klikli_dev.theurgy.registry.TagRegistry;
import com.github.klikli_dev.theurgy.util.Math3DUtil;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * Requires an adjacent heat source
 */
public class IgnisCrystalCondition implements ICrystalSpreadCondition {
    //region Overrides
    @Override
    public Direction canSpreadTo(IWorld world, BlockState targetState, BlockPos targetPos, BlockPos sourcePos) {
        long heatSources = Math3DUtil.getBlockPosInBox(targetPos, 1)
                                   .filter(pos -> TagRegistry.HEAT_SOURCES
                                                          .contains(world.getBlockState(pos).getBlock()))
                                   .count();
        if (heatSources == 0)
            return null;

        return this.getPlacementDirection(world, targetPos, sourcePos);
    }
    //endregion Overrides
}
