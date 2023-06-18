/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.liquefactioncauldron;

import com.klikli_dev.theurgy.content.block.itemhandler.BlockFluidHandler;
import com.klikli_dev.theurgy.content.block.itemhandler.BlockItemHandler;
import com.klikli_dev.theurgy.content.block.itemhandler.OneTankBlockFluidHandler;
import com.klikli_dev.theurgy.content.block.itemhandler.TwoSlotBlockItemHandler;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.EntityBlock;
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

public class LiquefactionCauldronBlock extends Block implements EntityBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape TOP = Block.box(0, 0, 0, 16, 6, 16);
    protected static final VoxelShape BOTTOM = Shapes.block();

    protected BlockItemHandler blockItemHandler;
    protected BlockFluidHandler blockFluidHandler;

    public LiquefactionCauldronBlock(Properties pProperties) {
        super(pProperties);
        this.blockItemHandler = new TwoSlotBlockItemHandler();
        this.blockFluidHandler = new OneTankBlockFluidHandler();
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER ? BOTTOM : TOP;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        //destroy both blocks if one is mined
        var half = pState.getValue(HALF);
        if (pFacing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (pFacing == Direction.UP)) {
            return pFacingState.is(this)
                    && pFacingState.getValue(HALF) != half ?
                    pState : Blocks.AIR.defaultBlockState();
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
            return this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER).setValue(LIT, false);
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

        if (this.blockFluidHandler.useFluidHandler(pState, pLevel, pPos, pPlayer, pHand, pHit) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        if (this.blockItemHandler.useItemHandler(pState, pLevel, pPos, pPlayer, pHand, pHit) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pState.getValue(HALF) == DoubleBlockHalf.LOWER && pLevel.getBlockEntity(pPos) instanceof LiquefactionCauldronBlockEntity blockEntity) {
                Containers.dropContents(pLevel, pPos, new RecipeWrapper(blockEntity.inventory));
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT, HALF);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER ? BlockEntityRegistry.LIQUEFACTION_CAULDRON.get().create(pPos, pState) : null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide() || pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof LiquefactionCauldronBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }
}
