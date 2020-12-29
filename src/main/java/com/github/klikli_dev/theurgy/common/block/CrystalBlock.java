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

package com.github.klikli_dev.theurgy.common.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import java.util.Map;

public class CrystalBlock extends DirectionalBlock {
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(
            ImmutableMap.<Direction, VoxelShape>builder()
                    .put(Direction.EAST, Block.makeCuboidShape(4, 4, 4, 12, 12, 12))  //TODO: Find correct rotation
                    .put(Direction.WEST, Block.makeCuboidShape(4, 4, 4, 12, 12, 12))  //      Find correct rotation
                    .put(Direction.NORTH, Block.makeCuboidShape(4, 4, 4, 12, 12, 12)) //      Find correct rotation
                    .put(Direction.SOUTH, Block.makeCuboidShape(4, 4, 4, 12, 12, 12)) //      Find correct rotation
                    .put(Direction.UP, Block.makeCuboidShape(4, 0, 4, 12, 14, 12))
                    .put(Direction.DOWN, Block.makeCuboidShape(4, 2, 4, 12, 16, 12))
                    .build());
    //region Initialization
    public CrystalBlock(AbstractBlock.Properties builder) {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP));
    }
    //endregion Initialization

    //region Overrides


    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(state.get(BlockStateProperties.FACING));
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.with(FACING, mirrorIn.mirror(state.get(FACING)));
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getFace();
        BlockState blockstate = context.getWorld().getBlockState(context.getPos().offset(direction.getOpposite()));
        return blockstate.isIn(this) && blockstate.get(FACING) == direction ? this.getDefaultState().with(FACING,
                direction.getOpposite()) : this.getDefaultState().with(FACING, direction);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    //endregion Overrides
}
