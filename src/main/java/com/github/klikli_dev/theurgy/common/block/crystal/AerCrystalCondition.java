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
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * Can have max 3 adjacent solid blocks that are not crystals.
 * Should allow to grow them in columns
 */
public class AerCrystalCondition implements ICrystalSpreadCondition{
    @Override
    public Direction canSpreadTo(IWorld world, BlockState targetState, BlockPos targetPos, BlockState sourceState, BlockPos sourcePos) {
        long solidBlocks = Math3DUtil.getBlockPosInBox(sourcePos, 1)
                                   .filter((pos) -> {
                                       BlockState state = world.getBlockState(pos);
                                       return !pos.equals(sourcePos)
                                              //only count non-air blocks that are not crystals
                                              && !state.isAir(world, pos) && !TagRegistry.CRYSTALS.contains(state.getBlock());
                                   }).count();
        if(solidBlocks > 3)
            return null;

        return this.getPlacementDirection(world, targetPos, sourcePos);
    }
}
