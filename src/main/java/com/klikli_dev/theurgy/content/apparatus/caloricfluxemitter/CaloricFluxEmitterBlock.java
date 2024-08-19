// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.klikli_dev.theurgy.content.behaviour.selection.SelectionBehaviour;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CaloricFluxEmitterBlock extends DirectionalBlock implements EntityBlock {

    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    public static final MapCodec<CaloricFluxEmitterBlock> CODEC = simpleCodec(CaloricFluxEmitterBlock::new);
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
    protected CaloricFluxEmitterInteractionBehaviour interactionBehaviour;

    public CaloricFluxEmitterBlock(@NotNull Properties pProperties) {
        super(pProperties);

        this.selectionBehaviour = new CaloricFluxEmitterSelectionBehaviour();
        this.interactionBehaviour = new CaloricFluxEmitterInteractionBehaviour();

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(ENABLED, true));
    }

    public SelectionBehaviour<CaloricFluxEmitterSelectedPoint> selectionBehaviour() {
        return this.selectionBehaviour;
    }

    public CaloricFluxEmitterInteractionBehaviour interactionBehaviour() {
        return this.interactionBehaviour;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPES.get(pState.getValue(FACING));
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        return this.interactionBehaviour.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        Direction direction = pContext.getClickedFace();
        BlockState oppositeState = pContext.getLevel().getBlockState(pContext.getClickedPos().relative(direction.getOpposite()));

        var placementState = this.defaultBlockState().setValue(ENABLED, true);

        return oppositeState.is(this) && oppositeState.getValue(FACING) == direction ?
                placementState.setValue(FACING, direction.getOpposite()) : placementState.setValue(FACING, direction);
    }


    @Override
    public void neighborChanged(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Block pBlock, @NotNull BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
        this.checkPoweredState(pLevel, pPos, pState, Block.UPDATE_INVISIBLE);

        if (!this.canSurvive(pState, pLevel, pPos)) {
            pLevel.destroyBlock(pPos, true);
        }
    }

    @Override
    public boolean canSurvive(@NotNull BlockState pState, @NotNull LevelReader pLevel, @NotNull BlockPos pPos) {
        var facing = pState.getValue(FACING);
        var facingNeighborState = pLevel.getBlockState(pPos.relative(facing.getOpposite()));
        return facingNeighborState.isFaceSturdy(pLevel, pPos, facing);
    }

    @Override
    public void onPlace(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock())) {
            this.checkPoweredState(pLevel, pPos, pState, Block.UPDATE_CLIENTS);
        }
    }

    private void checkPoweredState(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, int pFlags) {
        boolean enabled = !pLevel.hasNeighborSignal(pPos);
        if (enabled != pState.getValue(ENABLED)) {
            pLevel.setBlock(pPos, pState.setValue(ENABLED, enabled), pFlags);
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirrorIn) {
        return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, ENABLED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return BlockEntityRegistry.CALORIC_FLUX_EMITTER.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof CaloricFluxEmitterBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }

    @Override
    protected @NotNull MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }
}
