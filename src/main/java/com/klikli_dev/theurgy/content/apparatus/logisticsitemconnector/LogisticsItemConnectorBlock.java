// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector;

import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.logistics.Wires;
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
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class LogisticsItemConnectorBlock extends DirectionalBlock implements EntityBlock {

    protected static final float LENGTH = 2.0F;
    protected static final float WIDTH = 2.0F;
    protected static final float HEIGHT = 8.0F;

    protected static final float CENTER = 8.0F; // Center of the block

    protected static final VoxelShape UP = Block.box(CENTER - WIDTH / 2, 0.0F, CENTER - LENGTH / 2, CENTER + WIDTH / 2, HEIGHT, CENTER + LENGTH / 2);
    protected static final VoxelShape DOWN = Block.box(CENTER - WIDTH / 2, 16.0F - HEIGHT, CENTER - LENGTH / 2, CENTER + WIDTH / 2, 16.0F, CENTER + LENGTH / 2);
    protected static final VoxelShape WEST = Block.box(16.0F - HEIGHT, CENTER - WIDTH / 2, CENTER - LENGTH / 2, 16.0F, CENTER + WIDTH / 2, CENTER + LENGTH / 2);
    protected static final VoxelShape EAST = Block.box(0.0F, CENTER - WIDTH / 2, CENTER - LENGTH / 2, HEIGHT, CENTER + WIDTH / 2, CENTER + LENGTH / 2);
    protected static final VoxelShape NORTH = Block.box(CENTER - WIDTH / 2, CENTER - LENGTH / 2, 16.0F - HEIGHT, CENTER + WIDTH / 2, CENTER + LENGTH / 2, 16.0F);
    protected static final VoxelShape SOUTH = Block.box(CENTER - WIDTH / 2, CENTER - LENGTH / 2, 0.0F, CENTER + WIDTH / 2, CENTER + LENGTH / 2, HEIGHT);

    public LogisticsItemConnectorBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (!pPlayer.getItemInHand(pHand).isEmpty())
            return InteractionResult.PASS;


        if (!pLevel.isClientSide) {
            //TODO: needs to send packet that does outlining
            //maybe a generic one?

            if (pLevel.getBlockEntity(pPos) instanceof LogisticsItemConnectorBlockEntity blockEntity) {
                var targets = blockEntity.leafNode().targets();
                for (var target : targets) {
                    Outliner.get().showAABB(target, Shapes.block().bounds().move(target), 20 * 5)
                            .colored(0x00FFFF)
                            .lineWidth(1 / 16f);
                }
            }

            var connected = Logistics.get().getNetwork(GlobalPos.of(pLevel.dimension(), pPos));
            if (connected != null) {
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
        }

        return InteractionResult.SUCCESS;
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
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);

        Wires.get(pLevel).removeWiresFor(pPos);
        //TODO: crash on remove -> concurrent modification exception

        if (!pLevel.isClientSide && pLevel.getBlockEntity(pPos) instanceof LogisticsItemConnectorBlockEntity blockEntity) {
            blockEntity.leafNode().onDestroyed();
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
