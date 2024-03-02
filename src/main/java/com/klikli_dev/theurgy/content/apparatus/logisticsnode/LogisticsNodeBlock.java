// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsnode;

import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LogisticsNodeBlock extends DirectionalBlock {
    public static final MapCodec<LogisticsNodeBlock> CODEC = simpleCodec(LogisticsNodeBlock::new);

    protected static final float LENGTH = 6.0F;
    protected static final float WIDTH = 6.0F;
    protected static final float HEIGHT = 8.0F;

    protected static final float CENTER = 8.0F; // Center of the block

    protected static final VoxelShape UP = Block.box(CENTER - WIDTH / 2, 0.0F, CENTER - LENGTH / 2, CENTER + WIDTH / 2, HEIGHT, CENTER + LENGTH / 2);
    protected static final VoxelShape DOWN = Block.box(CENTER - WIDTH / 2, 16.0F - HEIGHT, CENTER - LENGTH / 2, CENTER + WIDTH / 2, 16.0F, CENTER + LENGTH / 2);
    protected static final VoxelShape WEST = Block.box(16.0F - HEIGHT, CENTER - WIDTH / 2, CENTER - LENGTH / 2, 16.0F, CENTER + WIDTH / 2, CENTER + LENGTH / 2);
    protected static final VoxelShape EAST = Block.box(0.0F, CENTER - WIDTH / 2, CENTER - LENGTH / 2, HEIGHT, CENTER + WIDTH / 2, CENTER + LENGTH / 2);
    protected static final VoxelShape NORTH = Block.box(CENTER - WIDTH / 2, CENTER - LENGTH / 2, 16.0F - HEIGHT, CENTER + WIDTH / 2, CENTER + LENGTH / 2, 16.0F);
    protected static final VoxelShape SOUTH = Block.box(CENTER - WIDTH / 2, CENTER - LENGTH / 2, 0.0F, CENTER + WIDTH / 2, CENTER + LENGTH / 2, HEIGHT);

    protected LogisticsNodeSelectionBehaviour selectionBehaviour;

    public LogisticsNodeBlock(Properties properties) {
        super(properties);
        this.selectionBehaviour = new LogisticsNodeSelectionBehaviour();

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    public LogisticsNodeSelectionBehaviour selectionBehaviour() {
        return this.selectionBehaviour;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            default -> UP;
            case DOWN -> DOWN;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
        };
    }

    //TODO destroy self on neighbor destroy

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        //TODO: this whole block probably should not exist and be only for debug :)
        if (!pPlayer.getItemInHand(pHand).isEmpty())
            return InteractionResult.PASS;

        //TODO: selection behaviour should be an interface -> somehow automate tick/right/left click events
        //      e.g. lazily make list of fitting blocks and cache it

        if (!pLevel.isClientSide) {
            //TODO: needs to send packet that does outlining
            //maybe a generic one?

            var connected = Logistics.get().getNetwork(GlobalPos.of(pLevel.dimension(), pPos));
            var shape = Shapes.block();
            for (var block : connected.nodes()) {
                if (block.dimension().equals(pLevel.dimension())) {
                    Outliner.get().showAABB(block, shape.bounds()
                                    .move(block.pos()), 20 * 5)
                            .colored(0x00FF00)
                            .lineWidth(1 / 16f);
                }
            }

        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);

        if (!pLevel.isClientSide) {
            Logistics.get().remove(GlobalPos.of(pLevel.dimension(), pPos));
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
}
