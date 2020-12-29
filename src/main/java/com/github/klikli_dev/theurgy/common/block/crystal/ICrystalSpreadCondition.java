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

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface ICrystalSpreadCondition {
    /**
     * Checks if a crystal can spread to the given position.
     * @param world the world to spread in.
     * @param targetState the block state of the target position.
     * @param targetPos the target position.
     * @param sourcePos the source position.
     * @return Valid direction if can spread, null otherwise
     */
    Direction canSpreadTo(IWorld world, BlockState targetState, BlockPos targetPos, BlockPos sourcePos);

    /**
     * Gets a list of valid placement directions. Can be used to restrict spread to e.g. ceilings only.
     * @return a list of directions.
     */
    default List<Direction> getValidPlacementDirections(){
        ArrayList<Direction> list = new ArrayList<>(Direction.values().length);
        Collections.addAll(list,Direction.values());
        return list;
    }

    default boolean isValidNeighbor(IWorld world, BlockState neighborState, BlockPos neighborPos, Direction neighborDirection){
        return neighborState.isSolidSide(world, neighborPos, neighborDirection);
    }

    /**
     * Gets a random valid placement direction for the target block pos.
     * @param world the world to place in.
     * @param targetPos the target block pos.
     * @param sourcePos the source block pos.
     * @return The direction to place or null if there is no valid one.
     */
    default Direction getPlacementDirection(IWorld world, BlockPos targetPos, BlockPos sourcePos){
        List<Direction> directions = this.getValidPlacementDirections();
        Collections.shuffle(directions);
        for(Direction direction : directions){
            BlockPos neighborPos = targetPos.offset(direction);
            if(neighborPos.equals(sourcePos)) //don't use the source crystal as substrate
                continue;

            BlockState neighbor = world.getBlockState(neighborPos);
            if(this.isValidNeighbor(world, neighbor, neighborPos, direction))
                return direction;
        }

        return null;
    }
}
