// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector;

import com.klikli_dev.theurgy.content.apparatus.DirectionalBlockShape;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlock;
import com.klikli_dev.theurgy.content.behaviour.logistics.HasWireEndPoint;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public abstract class LogisticsFluidConnectorBlock extends DirectionalBlock implements EntityBlock, HasWireEndPoint, SimpleWaterloggedBlock {

    public static final BooleanProperty HAS_FILTER = LogisticsItemConnectorBlock.HAS_FILTER;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final DirectionalBlockShape SHAPE = new DirectionalBlockShape(2, 2, 8);

    public LogisticsFluidConnectorBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(HAS_FILTER, false).setValue(WATERLOGGED, false));
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        if (!(pLevel.getBlockEntity(pPos) instanceof LogisticsFluidConnectorBlockEntity blockEntity)) {
            return InteractionResult.PASS;
        }

        var result = blockEntity.filter().useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
        if (result != InteractionResult.PASS)
            return result;

        if (!pPlayer.getItemInHand(pHand).isEmpty())
            return InteractionResult.PASS;

        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        pPlayer.openMenu(blockEntity, pPos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE.getShape(pState.getValue(FACING));
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, boolean pMovedByPiston) {
        Containers.updateNeighboursAfterDestroy(pState, pLevel, pPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, HAS_FILTER, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getClickedFace();
        BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos().relative(direction.getOpposite()));
        boolean waterlogged = pContext.getLevel().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER;
        return (blockstate.is(this) && blockstate.getValue(FACING) == direction
                ? this.defaultBlockState().setValue(FACING, direction.getOpposite())
                : this.defaultBlockState().setValue(FACING, direction))
                .setValue(WATERLOGGED, waterlogged);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        //The item connector can be connected to any block that is not air.
        //that is because many machines do not offer solid faces.
        Direction direction = pState.getValue(FACING);
        BlockPos blockpos = pPos.relative(direction.getOpposite());
        return !pLevel.getBlockState(blockpos).isAir();
    }

    @Override
    public BlockState updateShape(BlockState pState, net.minecraft.world.level.LevelReader pLevel, net.minecraft.world.level.ScheduledTickAccess pTickAccess, BlockPos pCurrentPos, Direction pFacing, BlockPos pFacingPos, BlockState pFacingState, net.minecraft.util.RandomSource pRandom) {
        if (pState.getValue(WATERLOGGED)) {
            pTickAccess.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return pFacing.getOpposite() == pState.getValue(FACING) && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : pState;
    }

    @Override
    protected @NotNull FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }
}
