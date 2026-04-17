// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticscapabilityprobe;

import com.klikli_dev.theurgy.content.apparatus.DirectionalBlockShape;
import com.klikli_dev.theurgy.content.behaviour.logistics.HasWireEndPoint;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.logistics.Wires;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageShowLogisticsNodeStatus;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LogisticsCapabilityProbeBlock extends DirectionalBlock implements HasWireEndPoint {

    public static final MapCodec<LogisticsCapabilityProbeBlock> CODEC = simpleCodec(LogisticsCapabilityProbeBlock::new);
    public static final DirectionalBlockShape SHAPE = new DirectionalBlockShape(2, 2, 8);

    public LogisticsCapabilityProbeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected @NotNull MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        if (pHand != InteractionHand.MAIN_HAND || !pPlayer.getItemInHand(pHand).isEmpty()) {
            return InteractionResult.PASS;
        }

        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        Networking.sendTo((ServerPlayer) pPlayer, new MessageShowLogisticsNodeStatus(this.getStatusHighlights(pLevel, pPos, pState)));
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE.getShape(pState.getValue(FACING));
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, boolean pMovedByPiston) {
        Containers.updateNeighboursAfterDestroy(pState, pLevel, pPos);

        var removedWires = Wires.get(pLevel).removeWiresFor(pPos);
        Block.popResource(pLevel, pPos, new ItemStack(ItemRegistry.COPPER_WIRE.get(), removedWires));
        Logistics.get().remove(GlobalPos.of(pLevel.dimension(), pPos));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
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
        Direction direction = pState.getValue(FACING);
        BlockPos blockpos = pPos.relative(direction.getOpposite());
        return !pLevel.getBlockState(blockpos).isAir();
    }

    @Override
    public BlockState updateShape(BlockState pState, net.minecraft.world.level.LevelReader pLevel, net.minecraft.world.level.ScheduledTickAccess pTickAccess, BlockPos pCurrentPos, Direction pFacing, BlockPos pFacingPos, BlockState pFacingState, net.minecraft.util.RandomSource pRandom) {
        return pFacing.getOpposite() == pState.getValue(FACING) && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : pState;
    }

    public List<Pair<BlockPos, Integer>> getStatusHighlights(Level level, BlockPos pos, BlockState state) {
        List<Pair<BlockPos, Integer>> result = new ArrayList<>();
        var target = getTarget(level, pos, state);
        if (target != null) {
            result.add(Pair.of(target.pos(), 0x00FF00));
        }
        return result;
    }

    public static @Nullable Target getTarget(Level level, BlockPos probePos) {
        if (!level.isLoaded(probePos)) {
            return null;
        }

        var state = level.getBlockState(probePos);
        if (!state.is(BlockRegistry.LOGISTICS_CAPABILITY_PROBE.get())) {
            return null;
        }

        return getTarget(level, probePos, state);
    }

    public static @Nullable Target getTarget(Level level, BlockPos probePos, BlockState state) {
        if (!(state.getBlock() instanceof LogisticsCapabilityProbeBlock)) {
            return null;
        }

        var side = state.getValue(FACING);
        var targetPos = probePos.relative(side.getOpposite());
        if (!level.isLoaded(targetPos) || level.getBlockState(targetPos).isAir()) {
            return null;
        }

        var targetState = level.getBlockState(targetPos);
        if (targetState.is(BlockRegistry.LOGISTICS_CAPABILITY_PROXY.get()) || targetState.is(BlockRegistry.LOGISTICS_CAPABILITY_PROBE.get())) {
            return null;
        }

        return new Target(targetPos, side);
    }

    public static <T> @Nullable T resolveSidedCapability(Level level, BlockPos probePos, BlockCapability<T, @Nullable Direction> capability) {
        var target = getTarget(level, probePos);
        if (target == null) {
            return null;
        }

        return level.getCapability(capability, target.pos(), target.side());
    }

    public record Target(BlockPos pos, Direction side) {
    }
}
