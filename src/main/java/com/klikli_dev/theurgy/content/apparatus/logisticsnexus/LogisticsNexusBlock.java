// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsnexus;

import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class LogisticsNexusBlock extends Block implements EntityBlock {
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public LogisticsNexusBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(UP, false).setValue(DOWN, false).setValue(NORTH, false).setValue(SOUTH, false).setValue(EAST, false).setValue(WEST, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();
        return this.defaultBlockState()
                .setValue(UP, hasNeighbor(level, pos.above()))
                .setValue(DOWN, hasNeighbor(level, pos.below()))
                .setValue(NORTH, hasNeighbor(level, pos.north()))
                .setValue(SOUTH, hasNeighbor(level, pos.south()))
                .setValue(EAST, hasNeighbor(level, pos.east()))
                .setValue(WEST, hasNeighbor(level, pos.west()));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, net.minecraft.util.RandomSource random) {
        if (facing.getAxis() == Direction.Axis.Y || facing.getAxis().isHorizontal()) {
            return state.setValue(propertyFor(facing), hasNeighbor(facingState));
        }
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, SOUTH, EAST, WEST);
    }

    @Nullable @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return BlockEntityRegistry.LOGISTICS_NEXUS.get().create(pos, state); }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level.isClientSide() || state.is(oldState.getBlock()) || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (!(level.getBlockEntity(pos) instanceof LogisticsNexusBlockEntity nexus)) {
            return;
        }

        nexus.ensureNexusId();
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
        Logistics.get().remove(GlobalPos.of(level.dimension(), pos));
    }

    private BooleanProperty propertyFor(Direction d) { return switch (d) { case UP -> UP; case DOWN -> DOWN; case NORTH -> NORTH; case SOUTH -> SOUTH; case EAST -> EAST; case WEST -> WEST; }; }
    private boolean hasNeighbor(LevelReader level, BlockPos pos) { return hasNeighbor(level.getBlockState(pos)); }
    private boolean hasNeighbor(BlockState state) { return !state.isAir(); }
}
