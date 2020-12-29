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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;
import java.util.Random;

public class CrystalBlock extends DirectionalBlock implements IWaterLoggable {

    //region Fields
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(
            ImmutableMap.<Direction, VoxelShape>builder()
                    .put(Direction.EAST, Block.makeCuboidShape(4, 4, 4, 12, 12, 12))  //TODO: Find correct rotation
                    .put(Direction.WEST, Block.makeCuboidShape(4, 4, 4, 12, 12, 12))  //      Find correct rotation
                    .put(Direction.NORTH, Block.makeCuboidShape(4, 4, 4, 12, 12, 12)) //      Find correct rotation
                    .put(Direction.SOUTH, Block.makeCuboidShape(4, 4, 4, 12, 12, 12)) //      Find correct rotation
                    .put(Direction.UP, Block.makeCuboidShape(4, 0, 4, 12, 14, 12))
                    .put(Direction.DOWN, Block.makeCuboidShape(4, 2, 4, 12, 16, 12))
                    .build());

    public ICrystalSpreadHandler spreadHandler;
    //endregion Fields

    //region Initialization
    public CrystalBlock(AbstractBlock.Properties builder, ICrystalSpreadHandler spreadHandler) {
        super(builder);
        this.spreadHandler = spreadHandler;
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP).with(WATERLOGGED, false));
    }
    //endregion Initialization

    //region Overrides


    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!worldIn.isAreaLoaded(pos, 2))
            return; //avoid loading unloaded chunks when checking for placement.
        //30% chance to grow on tick
        //if(worldIn.rand.nextInt(2) == 0){
            if(this.spreadHandler == null)
                return;
            this.spreadHandler.handleSpread(this, worldIn, state, pos);
       // }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getFace();
        BlockState blockstate = context.getWorld().getBlockState(context.getPos().offset(direction.getOpposite()));
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());

        return (blockstate.isIn(this) && blockstate.get(FACING) == direction ? this.getDefaultState().with(FACING,
                direction.getOpposite()) : this.getDefaultState().with(FACING, direction))
                       .with(WATERLOGGED, fluidstate.isTagged(FluidTags.WATER) && fluidstate.getLevel() == 8);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(WATERLOGGED);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
                                          BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }


    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.with(FACING, mirrorIn.mirror(state.get(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(state.get(BlockStateProperties.FACING));
    }
    //endregion Overrides
}
