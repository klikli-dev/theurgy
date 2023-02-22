/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.liquefactioncauldron;

import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

public class LiquefactionCauldronBlock extends Block implements EntityBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 17, 21, 16);

    public LiquefactionCauldronBlock(Properties pProperties) {
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
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (pLevel.getBlockEntity(pPos) instanceof LiquefactionCauldronBlockEntity blockEntity) {

            ItemStack stackInHand = pPlayer.getItemInHand(pHand);

            //first attempt fluid transfer
            var itemFluidHandlerCap = FluidUtil.getFluidHandler(stackInHand);
            if (itemFluidHandlerCap.isPresent()) {
                var itemFluidHandler = itemFluidHandlerCap.orElse(null); //ok here because we checked for presence

                //first we try to insert
                var transferredFluid = FluidUtil.tryFluidTransfer(blockEntity.solventTank, itemFluidHandler,
                        Integer.MAX_VALUE, true);
                if (!transferredFluid.isEmpty()) {
                    pPlayer.setItemInHand(pHand, itemFluidHandler.getContainer()); //always set to container to handle e.g. empty bucket correctly
                    blockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }

                //if that fails, try to extract
                transferredFluid = FluidUtil.tryFluidTransfer(itemFluidHandler, blockEntity.solventTank,
                        Integer.MAX_VALUE, true);
                if (!transferredFluid.isEmpty()) {
                    pPlayer.setItemInHand(pHand, itemFluidHandler.getContainer());
                    blockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }
            }

            //only if that fails try item transfer
            var outputStack = blockEntity.outputInventory.getStackInSlot(0);
            var inputStack = blockEntity.inputInventory.getStackInSlot(0);
            if (stackInHand.isEmpty()) {
                if (!outputStack.isEmpty()) {
                    //with empty hand first try take output
                    pPlayer.getInventory().placeItemBackInInventory(outputStack);
                    blockEntity.outputInventory.setStackInSlot(0, ItemStack.EMPTY);
                    blockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                } else if (!inputStack.isEmpty()) {
                    //if no output, try take input
                    pPlayer.getInventory().placeItemBackInInventory(inputStack);
                    blockEntity.inputInventory.setStackInSlot(0, ItemStack.EMPTY);
                    blockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }
            } else {
                //if we have an item in hand, try to insert
                var remainder = blockEntity.inputInventory.insertItem(0, stackInHand, false);
                pPlayer.setItemInHand(pHand, remainder);
                if (remainder.getCount() != stackInHand.getCount()) {
                    blockEntity.setChanged();
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
            if (pLevel.getBlockEntity(pPos) instanceof LiquefactionCauldronBlockEntity blockEntity) {
                Containers.dropContents(pLevel, pPos, new RecipeWrapper(blockEntity.inventory));
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.LIQUEFACTION_CAULDRON.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof LiquefactionCauldronBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }
}
