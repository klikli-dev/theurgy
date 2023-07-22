/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator;

import com.klikli_dev.theurgy.content.behaviour.FluidHandlerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.ItemHandlerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.OneSlotItemHandlerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.OneTankFluidHandlerBehaviour;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

public class SalAmmoniacAccumulatorBlock extends Block implements EntityBlock {
    protected ItemHandlerBehaviour itemHandlerBehaviour;
    protected FluidHandlerBehaviour fluidHandlerBehaviour;

    public SalAmmoniacAccumulatorBlock(Properties pProperties) {
        super(pProperties);
        this.itemHandlerBehaviour = new OneSlotItemHandlerBehaviour();
        this.fluidHandlerBehaviour = new OneTankFluidHandlerBehaviour();
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (this.fluidHandlerBehaviour.useFluidHandler(pState, pLevel, pPos, pPlayer, pHand, pHit) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        if (this.itemHandlerBehaviour.useItemHandler(pState, pLevel, pPos, pPlayer, pHand, pHit) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof SalAmmoniacAccumulatorBlockEntity blockEntity) {
                Containers.dropContents(pLevel, pPos, new RecipeWrapper(blockEntity.inventory));
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {

        if (pLevel.getBlockEntity(pCurrentPos) instanceof SalAmmoniacAccumulatorBlockEntity accumulatorBlockEntity && pFacing == Direction.DOWN) {
            accumulatorBlockEntity.validateOutputTank();
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.SAL_AMMONIAC_ACCUMULATOR.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {

        if (pLevel.isClientSide()) {
            return (lvl, pos, blockState, t) -> {
                if (t instanceof SalAmmoniacAccumulatorBlockEntity blockEntity) {
                    blockEntity.tickClient();
                }
            };
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof SalAmmoniacAccumulatorBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }
}
