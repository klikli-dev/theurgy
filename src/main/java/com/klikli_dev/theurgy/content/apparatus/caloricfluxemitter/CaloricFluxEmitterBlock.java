// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.klikli_dev.theurgy.content.behaviour.selection.SelectionBehaviour;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CaloricFluxEmitterBlock extends DirectionalBlock implements EntityBlock {

    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    private static final int SHAPE_LENGTH = 4;
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(
            ImmutableMap.<Direction, VoxelShape>builder()
                    .put(Direction.EAST, Block.box(0, 4, 4, SHAPE_LENGTH, 12, 12))
                    .put(Direction.WEST, Block.box(16 - SHAPE_LENGTH, 4, 4, 16, 12, 12))
                    .put(Direction.NORTH, Block.box(4, 4, 16 - SHAPE_LENGTH, 12, 12, 16))
                    .put(Direction.SOUTH, Block.box(4, 4, 0, 12, 12, SHAPE_LENGTH))
                    .put(Direction.UP, Block.box(4, 0, 4, 12, SHAPE_LENGTH, 12))
                    .put(Direction.DOWN, Block.box(4, 16 - SHAPE_LENGTH, 4, 12, 16, 12))
                    .build()
    );
    protected SelectionBehaviour<CaloricFluxEmitterSelectedPoint> selectionBehaviour;

    public CaloricFluxEmitterBlock(Properties pProperties, SelectionBehaviour<CaloricFluxEmitterSelectedPoint> selectionBehaviour) {
        super(pProperties);
        this.selectionBehaviour = selectionBehaviour;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(ENABLED, true));
    }

    public SelectionBehaviour<CaloricFluxEmitterSelectedPoint> selectionBehaviour() {
        return this.selectionBehaviour;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPES.get(pState.getValue(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getClickedFace();
        BlockState oppositeState = pContext.getLevel().getBlockState(pContext.getClickedPos().relative(direction.getOpposite()));

        var placementState = this.defaultBlockState().setValue(ENABLED, true);

        return oppositeState.is(this) && oppositeState.getValue(FACING) == direction ?
                placementState.setValue(FACING, direction.getOpposite()) : placementState.setValue(FACING, direction);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
        this.checkPoweredState(pLevel, pPos, pState, Block.UPDATE_INVISIBLE);

        if (!this.canSurvive(pState, pLevel, pPos)) {
            pLevel.destroyBlock(pPos, true);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        var facing = pState.getValue(FACING);
        var facingNeighborState = pLevel.getBlockState(pPos.relative(facing.getOpposite()));
        return facingNeighborState.isFaceSturdy(pLevel, pPos, facing);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock())) {
            this.checkPoweredState(pLevel, pPos, pState, Block.UPDATE_CLIENTS);
        }
    }

    private void checkPoweredState(Level pLevel, BlockPos pPos, BlockState pState, int pFlags) {
        boolean enabled = !pLevel.hasNeighborSignal(pPos);
        if (enabled != pState.getValue(ENABLED)) {
            pLevel.setBlock(pPos, pState.setValue(ENABLED, enabled), pFlags);
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, ENABLED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.CALORIC_FLUX_EMITTER.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof CaloricFluxEmitterBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }
}
