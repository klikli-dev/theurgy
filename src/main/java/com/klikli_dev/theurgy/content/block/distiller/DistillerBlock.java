/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.distiller;

import com.klikli_dev.theurgy.content.block.calcinationoven.CalcinationOvenBlockEntity;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

public class DistillerBlock extends Block implements EntityBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public DistillerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(LIT, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (pLevel.getBlockEntity(pPos) instanceof DistillerBlockEntity blockEntity) {

            ItemStack stackInHand = pPlayer.getItemInHand(pHand);
            var outputStack = blockEntity.outputInventory.getStackInSlot(0);
            var inputStack = blockEntity.inputInventory.getStackInSlot(0);
            if (stackInHand.isEmpty()) {
                if (!outputStack.isEmpty()) {
                    //with empty hand first try take output
                    pPlayer.getInventory().placeItemBackInInventory(outputStack);
                    blockEntity.outputInventory.setStackInSlot(0, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                } else if (!inputStack.isEmpty()) {
                    //if no output, try take input
                    pPlayer.getInventory().placeItemBackInInventory(inputStack);
                    blockEntity.inputInventory.setStackInSlot(0, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            } else {
                //if we have an item in hand, try to insert
                var remainder = blockEntity.inputInventory.insertItem(0, stackInHand, false);
                pPlayer.setItemInHand(pHand, remainder);
                if (remainder.getCount() != stackInHand.getCount()) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof DistillerBlockEntity blockEntity) {
                Containers.dropContents(pLevel, pPos, new RecipeWrapper(blockEntity.inventory));
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.DISTILLER.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof DistillerBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }
}
