/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public interface UsableAlchemicalDeviceBlock {

    int INPUT_SLOT = 0;
    int OUTPUT_SLOT = 1;

    /**
     * Default interaction for blocks that have a block entity with an input and an output inventory with one slot each, made available as combined inventory with input on slot 0 and output on slot 1.
     */
    default InteractionResult useItemHandler(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        var blockEntity = pLevel.getBlockEntity(pPos);

        if (blockEntity == null)
            return InteractionResult.PASS;

        var blockItemHandlerCap = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (!blockItemHandlerCap.isPresent())
            return InteractionResult.PASS;

        var blockItemHandler = blockItemHandlerCap.orElse(null);

        ItemStack stackInHand = pPlayer.getItemInHand(pHand);

        if (stackInHand.isEmpty()) {
            //with empty hand first try take output
            var extracted = blockItemHandler.extractItem(OUTPUT_SLOT, blockItemHandler.getSlotLimit(OUTPUT_SLOT), false);
            if (!extracted.isEmpty()) {
                pPlayer.getInventory().placeItemBackInInventory(extracted);
                return InteractionResult.SUCCESS;
            }

            //if no output, try take input
            extracted = blockItemHandler.extractItem(INPUT_SLOT, blockItemHandler.getSlotLimit(INPUT_SLOT), false);
            if (!extracted.isEmpty()) {
                pPlayer.getInventory().placeItemBackInInventory(extracted);
                return InteractionResult.SUCCESS;
            }
        } else {
            //if we have an item in hand, try to insert
            var remainder = blockItemHandler.insertItem(INPUT_SLOT, stackInHand, false);
            pPlayer.setItemInHand(pHand, remainder);
            if (remainder.getCount() != stackInHand.getCount()) {
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }


    /**
     * Default interaction for blocks have a block entity with a fluid handler.
     */
    default InteractionResult useFluidHandler(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        var blockEntity = pLevel.getBlockEntity(pPos);

        if (blockEntity == null)
            return InteractionResult.PASS;

        var stackInHand = pPlayer.getItemInHand(pHand);
        var fillStack = stackInHand.copyWithCount(1); //necessary to handle stacks of containers, because FluidUtil only can handle one item at a time

        var blockFluidHandlerCap = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER);
        var itemFluidHandlerCap = FluidUtil.getFluidHandler(fillStack);

        if (!blockFluidHandlerCap.isPresent() || !itemFluidHandlerCap.isPresent())
            return InteractionResult.PASS;

        var blockFluidHandler = blockFluidHandlerCap.orElse(null);
        var itemFluidHandler = itemFluidHandlerCap.orElse(null);

        //first we try to insert
        var transferredFluid = FluidUtil.tryFluidTransfer(blockFluidHandler, itemFluidHandler,
                Integer.MAX_VALUE, true);
        if (this.updateFluidContainerInHand(pPlayer, pHand, stackInHand, itemFluidHandler, transferredFluid))
            return InteractionResult.SUCCESS;

        //if that fails, try to extract
        transferredFluid = FluidUtil.tryFluidTransfer(itemFluidHandler, blockFluidHandler,
                Integer.MAX_VALUE, true);
        if (this.updateFluidContainerInHand(pPlayer, pHand, stackInHand, itemFluidHandler, transferredFluid))
            return InteractionResult.SUCCESS;


        return InteractionResult.PASS;
    }

    private boolean updateFluidContainerInHand(Player pPlayer, InteractionHand pHand, ItemStack stackInHand, IFluidHandlerItem itemFluidHandler, FluidStack transferredFluid) {
        if (!transferredFluid.isEmpty()) {
            //handle bucket stacking correctly
            stackInHand.shrink(1);
            if (stackInHand.isEmpty()) {
                pPlayer.setItemInHand(pHand, itemFluidHandler.getContainer()); //always set to container to handle e.g. empty bucket correctly
            } else {
                pPlayer.setItemInHand(pHand, stackInHand);
                pPlayer.getInventory().placeItemBackInInventory(itemFluidHandler.getContainer());
            }

            return true;
        }
        return false;
    }
}
