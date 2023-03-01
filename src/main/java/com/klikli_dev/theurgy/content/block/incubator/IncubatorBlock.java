/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.incubator;

import com.klikli_dev.theurgy.registry.BlockTagRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class IncubatorBlock extends Block {

    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    protected static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((entry) -> entry.getKey().getAxis().isHorizontal()).collect(Util.toMap());

    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 11, 15);

    public IncubatorBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NORTH, EAST, WEST, SOUTH);
    }

    public boolean isVessel(BlockState pState) {
        return pState.is(BlockTagRegistry.INCUBATOR_VESSELS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockGetter blockgetter = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState north = blockgetter.getBlockState(blockpos.north());
        BlockState east = blockgetter.getBlockState(blockpos.east());
        BlockState south = blockgetter.getBlockState(blockpos.south());
        BlockState west = blockgetter.getBlockState(blockpos.west());
        return super.getStateForPlacement(pContext)
                .setValue(NORTH, this.isVessel(north))
                .setValue(EAST, this.isVessel(east))
                .setValue(SOUTH, this.isVessel(south))
                .setValue(WEST, this.isVessel(west));
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        //TODO: notify BE to update vessel list
        //      that vessel list update should also happen in onLoad of the BE, because onPlace of the block is before BE spawns
        return pFacing.getAxis().getPlane() == Direction.Plane.HORIZONTAL ?
                pState.setValue(PROPERTY_BY_DIRECTION.get(pFacing), this.isVessel(pFacingState)) :
                super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }
}
