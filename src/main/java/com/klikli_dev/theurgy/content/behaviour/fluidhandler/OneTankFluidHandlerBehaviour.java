// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.fluidhandler;

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
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class OneTankFluidHandlerBehaviour implements FluidHandlerBehaviour {

    /**
     * Default interaction for blocks have a block entity with a fluid handler.
     */
    @Override
    public InteractionResult useFluidHandler(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pHand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        var blockEntity = pLevel.getBlockEntity(pPos);

        if (blockEntity == null)
            return InteractionResult.PASS;

        var stackInHand = pPlayer.getItemInHand(pHand);
        var fillStack = stackInHand.copyWithCount(1); //necessary to handle stacks of containers, because FluidUtil only can handle one item at a time

        var blockFluidHandlerCap = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER);
        var itemFluidHandlerCap = FluidUtil.getFluidHandler(fillStack);

        //a block without fluid handler is of no interest
        if (!blockFluidHandlerCap.isPresent())
            return InteractionResult.PASS;
        var blockFluidHandler = blockFluidHandlerCap.orElse(null);

        if(stackInHand.isEmpty() && pPlayer.isShiftKeyDown()){
            //sneaking with empty hand means we're trying to void the liquid
            blockFluidHandler.drain(Integer.MAX_VALUE, IFluidHandlerItem.FluidAction.EXECUTE);
            return InteractionResult.SUCCESS;
        }

        //if our item does not have a fluid handler we cannot interact further
        if (!itemFluidHandlerCap.isPresent())
            return InteractionResult.PASS;
        var itemFluidHandler = itemFluidHandlerCap.orElse(null);

        //first we try to insert from item to block
        //Use a simulated drain first to check what fluid is available, ignoring NBT
        var simulatedDrain = itemFluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
        if (!simulatedDrain.isEmpty()) {
            //Now try to fill the block with this fluid type (without NBT comparison)
            var amountFilled = blockFluidHandler.fill(simulatedDrain, IFluidHandler.FluidAction.EXECUTE);
            if (amountFilled > 0) {
                //Actually drain from the item now
                itemFluidHandler.drain(amountFilled, IFluidHandler.FluidAction.EXECUTE);
                if (this.updateFluidContainerInHand(pPlayer, pHand, stackInHand, itemFluidHandler, new FluidStack(simulatedDrain.getFluid(), amountFilled)))
                    return InteractionResult.SUCCESS;
            }
        }

        //if that fails, try to extract from block to item
        //Use a simulated drain from block first
        var blockSimulatedDrain = blockFluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
        if (!blockSimulatedDrain.isEmpty()) {
            //Try to fill the item with this fluid
            var amountFilled = itemFluidHandler.fill(blockSimulatedDrain, IFluidHandler.FluidAction.EXECUTE);
            if (amountFilled > 0) {
                //Actually drain from the block now
                blockFluidHandler.drain(amountFilled, IFluidHandler.FluidAction.EXECUTE);
                if (this.updateFluidContainerInHand(pPlayer, pHand, stackInHand, itemFluidHandler, new FluidStack(blockSimulatedDrain.getFluid(), amountFilled)))
                    return InteractionResult.SUCCESS;
            }
        }

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
