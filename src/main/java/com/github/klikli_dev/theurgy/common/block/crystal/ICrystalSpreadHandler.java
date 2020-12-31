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

import com.github.klikli_dev.theurgy.util.Math3DUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface ICrystalSpreadHandler {
    /**
     * Handles spreading the crystal at the given position to a nearby block.
     * @param sourceCrystalType the source crystal type trying to spread.
     * @param world the world to spread in.
     * @param sourceState the block state of the source position.
     * @param sourcePos the source position.
     * @return true if the block was spread, false otherwise
     */
    boolean handleSpread(CrystalBlock sourceCrystalType, IWorld world, BlockState sourceState, BlockPos sourcePos);

    /**
     * Gets a list of blocks that a crystal can spread to. Excludes source pos and any blocked blocks.
     * @param world the world.
     * @param sourcePos the source position to search around.
     * @return a list of positions.
     */
    default List<BlockPos> getPossibleSpreadBlockPos(IWorld world, BlockPos sourcePos){
        return Math3DUtil.getBlockPosInBox(sourcePos, 1)
                                                 .filter((pos) -> {
                                                     BlockState state = world.getBlockState(pos);
                                                     return !pos.equals(sourcePos)
                                                            //allow air or water for crystals
                                                     && (state.isAir(world, pos) || state.isIn(Blocks.WATER));
                                                 })
                                                 .collect(Collectors.toList());
    }

    default CrystalPlacementInfo getValidSpreadPosition(ICrystalSpreadCondition condition,  IWorld world, List<BlockPos> possibleTargets, BlockState sourceState, BlockPos sourcePos){
        //randomize order to avoid a preference for one direction of spread
        Collections.shuffle(possibleTargets);

        for(BlockPos pos : possibleTargets){
            BlockState state = world.getBlockState(pos);
            Direction direction = condition.canSpreadTo(world, state, pos, sourceState, sourcePos);
            if(direction != null)
                return new CrystalPlacementInfo(pos, direction.getOpposite());
        }
        return null;
    }
}
