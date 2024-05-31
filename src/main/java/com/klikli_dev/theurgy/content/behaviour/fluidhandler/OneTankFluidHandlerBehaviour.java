// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.fluidhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;


public class OneTankFluidHandlerBehaviour implements FluidHandlerBehaviour {

    /**
     * Default interaction for blocks have a block entity with a fluid handler.
     */
    @Override
    public ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pHand != InteractionHand.MAIN_HAND)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        var stackInHand = pPlayer.getItemInHand(pHand);
        var fillStack = stackInHand.copyWithCount(1); //necessary to handle stacks of containers, because FluidUtil only can handle one item at a time

        var blockFluidHandler = pLevel.getCapability(Capabilities.FluidHandler.BLOCK, pPos, null);
        //a block without fluid handler is of no interest
        if (blockFluidHandler == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (stackInHand.isEmpty() && pPlayer.isShiftKeyDown()) {
            //sneaking with empty hand means we're trying to void the liquid
            blockFluidHandler.drain(Integer.MAX_VALUE, IFluidHandlerItem.FluidAction.EXECUTE);
            return ItemInteractionResult.SUCCESS;
        }

        var itemFluidHandler = fillStack.getCapability(Capabilities.FluidHandler.ITEM);

        //if our item does not have a fluid handler we cannot interact further
        if (itemFluidHandler == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        //first we try to insert
        var transferredFluid = FluidUtil.tryFluidTransfer(blockFluidHandler, itemFluidHandler,
                Integer.MAX_VALUE, true);
        if (this.updateFluidContainerInHand(pPlayer, pHand, stackInHand, itemFluidHandler, transferredFluid))
            return ItemInteractionResult.SUCCESS;

        //if that fails, try to extract
        transferredFluid = FluidUtil.tryFluidTransfer(itemFluidHandler, blockFluidHandler,
                Integer.MAX_VALUE, true);
        if (this.updateFluidContainerInHand(pPlayer, pHand, stackInHand, itemFluidHandler, transferredFluid))
            return ItemInteractionResult.SUCCESS;


        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
