// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector;

import com.klikli_dev.theurgy.content.apparatus.DirectionalBlockShape;
import com.klikli_dev.theurgy.content.behaviour.filter.HasFilterBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.HasWireEndPoint;
import com.klikli_dev.theurgy.logistics.Wires;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageShowLogisticsNodeStatus;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public abstract class LogisticsFluidConnectorBlock extends DirectionalBlock implements EntityBlock, HasWireEndPoint {

    public static final BooleanProperty HAS_FILTER = BooleanProperty.create("has_filter");

    public static final DirectionalBlockShape SHAPE = new DirectionalBlockShape(2, 2, 8);

    public LogisticsFluidConnectorBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(HAS_FILTER, false));
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        if (!(pLevel.getBlockEntity(pPos) instanceof LogisticsFluidConnectorBlockEntity blockEntity)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        var result = blockEntity.filter().useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
        if (result != ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION)
            return result;

        if (!pPlayer.getItemInHand(pHand).isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (pLevel.isClientSide)
            return ItemInteractionResult.SUCCESS;

        Networking.sendTo((ServerPlayer) pPlayer, new MessageShowLogisticsNodeStatus(blockEntity.getStatusHighlights()));

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE.getShape(pState.getValue(FACING));
    }

    @Override
    public void onRemove(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        //Drop filter
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof HasFilterBehaviour hasFilterBehaviour) {
                hasFilterBehaviour.filter().onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
            }
        }

        //drop wires
        if (pState.hasBlockEntity() && (!pState.is(pNewState.getBlock()) || !pNewState.hasBlockEntity())) {
            var removedWires = Wires.get(pLevel).removeWiresFor(pPos);
            if (pLevel.isClientSide)
                return;

            Block.popResource(pLevel, pPos, new ItemStack(ItemRegistry.COPPER_WIRE.get(), removedWires));

            if (pLevel.getBlockEntity(pPos) instanceof LogisticsFluidConnectorBlockEntity blockEntity) {
                blockEntity.leafNode().onDestroyed();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, HAS_FILTER);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getClickedFace();
        BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos().relative(direction.getOpposite()));
        return blockstate.is(this) && blockstate.getValue(FACING) == direction
                ? this.defaultBlockState().setValue(FACING, direction.getOpposite())
                : this.defaultBlockState().setValue(FACING, direction);
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
    public @NotNull BlockState updateShape(BlockState pState, Direction pFacing, @NotNull BlockState pFacingState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pFacingPos) {
        return pFacing.getOpposite() == pState.getValue(FACING) && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : pState;
    }
}
