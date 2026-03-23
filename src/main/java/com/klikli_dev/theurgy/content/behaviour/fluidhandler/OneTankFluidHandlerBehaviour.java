// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.fluidhandler;

import com.klikli_dev.theurgy.content.storage.FluidStorageHelper;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;


public class OneTankFluidHandlerBehaviour implements FluidHandlerBehaviour {

    /**
     * Default interaction for blocks have a block entity with a fluid handler.
     */
    @Override
    public InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pHand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        var stackInHand = pPlayer.getItemInHand(pHand);

        var blockFluidHandler = pLevel.getCapability(CapabilityRegistry.FLUID_HANDLER, pPos, null);
        //a block without fluid handler is of no interest
        if (blockFluidHandler == null)
            return InteractionResult.PASS;

        if (stackInHand.isEmpty() && pPlayer.isShiftKeyDown()) {
            //sneaking with empty hand means we're trying to void the liquid
            FluidStorageHelper.drain(blockFluidHandler, Integer.MAX_VALUE, false);
            return InteractionResult.SUCCESS;
        }

        if (FluidUtil.interactWithFluidHandler(pPlayer, pHand, pPos, blockFluidHandler)) {
            return InteractionResult.SUCCESS;
        }


        return InteractionResult.PASS;
    }


}
