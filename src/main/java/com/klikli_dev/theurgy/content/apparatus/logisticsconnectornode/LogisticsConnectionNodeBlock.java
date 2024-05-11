// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsconnectornode;

import com.klikli_dev.theurgy.content.apparatus.DirectionalBlockShape;
import com.klikli_dev.theurgy.content.behaviour.logistics.HasWireEndPoint;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

    //TODO destroy self on neighbor destroy
    //TODO only allow to attach to a full face

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        //TODO: this whole block probably should not exist and be only for debug - or converted to a "connector" node for multiple cables to meet:)
        if (!pPlayer.getItemInHand(pHand).isEmpty())
            return InteractionResult.PASS;

        if (pLevel.isClientSide)
            return InteractionResult.SUCCESS;

        List<Pair<BlockPos, Integer>> result = new ArrayList<>();
        var connected = Logistics.get().getNetwork(GlobalPos.of(pLevel.dimension(), pPos));
        for (var block : connected.nodes()) {
            if (block.dimension().equals(pLevel.dimension())) {
                result.add(Pair.of(block.pos(), 0xFFFFF00));
            }
        }
        Networking.sendTo((ServerPlayer) pPlayer, new MessageShowLogisticsNodeStatus(result));

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);

        //Note: Adding is not necessary, because when using the wire it adds the two connection points.
        var removedWires = Wires.get(pLevel).removeWiresFor(pPos);

        if (pLevel.isClientSide)
            return;

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
}
