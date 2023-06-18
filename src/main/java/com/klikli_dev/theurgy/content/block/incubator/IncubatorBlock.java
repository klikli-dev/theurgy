/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.incubator;

import com.klikli_dev.theurgy.content.block.itemhandler.BlockItemHandler;
import com.klikli_dev.theurgy.content.block.itemhandler.OneSlotBlockItemHandler;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.BlockTagRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class IncubatorBlock extends Block implements EntityBlock {
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    protected static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((entry) -> entry.getKey().getAxis().isHorizontal()).collect(Util.toMap());
    protected static final VoxelShape TOP = Shapes.block();
    protected static final VoxelShape BOTTOM = Shapes.block();

    protected BlockItemHandler blockItemHandler;

    public IncubatorBlock(Properties pProperties) {
        super(pProperties);
        this.blockItemHandler = new OneSlotBlockItemHandler();
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER ? BOTTOM : TOP;
    }


    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {

        if (pLevel.getBlockEntity(pCurrentPos) instanceof IncubatorBlockEntity incubatorBlockEntity) {
            incubatorBlockEntity.validateMultiblock();
        }

        //destroy both blocks if one is mined
        var half = pState.getValue(HALF);
        if (pFacing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (pFacing == Direction.UP)) {
            return pFacingState.is(this)
                    && pFacingState.getValue(HALF) != half ?
                    pState : Blocks.AIR.defaultBlockState();
        } else if (pFacing.getAxis().getPlane() == Direction.Plane.HORIZONTAL) {
            return pState.setValue(PROPERTY_BY_DIRECTION.get(pFacing), this.isVessel(pFacingState));
        } else {
            return half == DoubleBlockHalf.LOWER
                    && pFacing == Direction.DOWN
                    && !pState.canSurvive(pLevel, pCurrentPos) ?
                    Blocks.AIR.defaultBlockState() :
                    super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        }
    }

    @Override
    protected void spawnDestroyParticles(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState) {
        //also spawn for other half
        var otherHalf = pPos.above(pState.getValue(HALF) == DoubleBlockHalf.LOWER ? 1 : -1);
        pLevel.levelEvent(pPlayer, 2001, otherHalf, getId(pState));

        //and for ourselves
        super.spawnDestroyParticles(pLevel, pPlayer, pPos, pState);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide && pPlayer.isCreative()) {
            DoublePlantBlock.preventCreativeDropFromBottomPart(pLevel, pPos, pState, pPlayer);
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(pContext)) {

            BlockState north = level.getBlockState(blockpos.north());
            BlockState east = level.getBlockState(blockpos.east());
            BlockState south = level.getBlockState(blockpos.south());
            BlockState west = level.getBlockState(blockpos.west());
            return super.getStateForPlacement(pContext)
                    .setValue(NORTH, this.isVessel(north))
                    .setValue(EAST, this.isVessel(east))
                    .setValue(SOUTH, this.isVessel(south))
                    .setValue(WEST, this.isVessel(west))
                    .setValue(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        pLevel.setBlock(pPos.above(), pState.setValue(HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos below = pPos.below();
        BlockState belowState = pLevel.getBlockState(below);
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER || belowState.is(this);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        //handle top block
        pPos = pState.getValue(HALF) == DoubleBlockHalf.UPPER ? pPos.below() : pPos;

        if (this.blockItemHandler.useItemHandler(pState, pLevel, pPos, pPlayer, pHand, pHit) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pState.getValue(HALF) == DoubleBlockHalf.LOWER && pLevel.getBlockEntity(pPos) instanceof IncubatorBlockEntity blockEntity) {
                Containers.dropContents(pLevel, pPos, new RecipeWrapper(blockEntity.outputInventory));
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HALF, NORTH, EAST, WEST, SOUTH);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER ? BlockEntityRegistry.INCUBATOR.get().create(pPos, pState) : null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide() || pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof IncubatorBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }

    public boolean isVessel(BlockState pState) {
        return pState.is(BlockTagRegistry.INCUBATOR_VESSELS);
    }
}