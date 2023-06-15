/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.itemhandlers;

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

public class OneTankBlockFluidHandler implements BlockFluidHandler {

    /**
     * Default interaction for blocks have a block entity with a fluid handler.
     */
    @Override
    public InteractionResult useFluidHandler(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
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
