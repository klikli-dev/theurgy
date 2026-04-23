// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsconnectionnode;

import com.klikli_dev.theurgy.content.apparatus.DirectionalBlockShape;
import com.klikli_dev.theurgy.content.behaviour.logistics.HasWireEndPoint;
import com.klikli_dev.theurgy.content.apparatus.logisticsnexus.LogisticsNexusBlockEntity;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.logistics.Wires;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageShowLogisticsNodeStatus;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class LogisticsConnectionNodeBlock extends DirectionalBlock implements HasWireEndPoint {
    public static final MapCodec<LogisticsConnectionNodeBlock> CODEC = simpleCodec(LogisticsConnectionNodeBlock::new);

    public static final DirectionalBlockShape SHAPE = new DirectionalBlockShape(6, 6, 8);


    public LogisticsConnectionNodeBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }


    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE.getShape(pState.getValue(FACING));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide() && pHand == InteractionHand.MAIN_HAND && pPlayer.getMainHandItem().isEmpty()) {
            List<Pair<BlockPos, Integer>> result = new ArrayList<>();
            var connected = Logistics.get().getNetwork(GlobalPos.of(pLevel.dimension(), pPos));

            if (connected != null) {
                for (var block : connected.nodes()) {
                    if (block.dimension().equals(pLevel.dimension())) {
                        result.add(Pair.of(block.pos(), 0xFFFFF00));
                    }
                }
                Networking.sendTo((ServerPlayer) pPlayer, new MessageShowLogisticsNodeStatus(result));

            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState pState, ServerLevel pLevel, BlockPos pPos, boolean pMovedByPiston) {
        Containers.updateNeighboursAfterDestroy(pState, pLevel, pPos);

        var removedWires = Wires.get(pLevel).removeWiresFor(pPos);
        Block.popResource(pLevel, pPos, new ItemStack(ItemRegistry.COPPER_WIRE.get(), removedWires));
        Logistics.get().remove(GlobalPos.of(pLevel.dimension(), pPos));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level.isClientSide() || state.is(oldState.getBlock())) {
            return;
        }

        var attachedPos = pos.relative(state.getValue(FACING).getOpposite());
        if (level.getBlockEntity(attachedPos) instanceof LogisticsNexusBlockEntity nexus && nexus.nexusId() != null) {
            var logistics = Logistics.get();
            var nodePos = GlobalPos.of(level.dimension(), pos);
            var nexusPos = GlobalPos.of(level.dimension(), attachedPos);
            logistics.add(nodePos, nexusPos);
        } else {
            Logistics.get().add(GlobalPos.of(level.dimension(), pos));
        }
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

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
//        //the connection node can only be placed on a solid face
//        Direction direction = pState.getValue(FACING);
//        BlockPos blockpos = pPos.relative(direction.getOpposite());
//        BlockState blockstate = pLevel.getBlockState(blockpos);
//        return blockstate.isFaceSturdy(pLevel, blockpos, direction);

        //The connection node can be connected to any block that is not air.
        Direction direction = pState.getValue(FACING);
        BlockPos blockpos = pPos.relative(direction.getOpposite());
        return !pLevel.getBlockState(blockpos).isAir();
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState pState, LevelReader pLevel, ScheduledTickAccess pTickAccess, BlockPos pCurrentPos, Direction pFacing, BlockPos pFacingPos, BlockState pFacingState, RandomSource pRandom) {
        return pFacing.getOpposite() == pState.getValue(FACING) && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : pState;
    }
}
