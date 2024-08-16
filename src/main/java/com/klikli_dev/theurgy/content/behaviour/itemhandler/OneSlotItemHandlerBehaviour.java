// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.itemhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;


public class OneSlotItemHandlerBehaviour implements ItemHandlerBehaviour {

    public static final int SLOT = 0;

    /**
     * Default interaction for blocks that have a block entity with an in/output inventory with one slot.
     */
    @Override
    public ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult, boolean simulate) {
        if (pHand != InteractionHand.MAIN_HAND)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        var blockItemHandler = pLevel.getCapability(Capabilities.ItemHandler.BLOCK, pPos, null);
        //a block without item handler is of no interest
        if (blockItemHandler == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        ItemStack stackInHand = pPlayer.getItemInHand(pHand);

        if (stackInHand.isEmpty()) {
            //with empty hand, try to take out
            var extracted = blockItemHandler.extractItem(SLOT, blockItemHandler.getSlotLimit(SLOT), simulate);
            if (!extracted.isEmpty()) {

                if (!simulate)
                    pPlayer.getInventory().placeItemBackInInventory(extracted);

                return ItemInteractionResult.SUCCESS;
            }
        } else {
            //if we have an item in hand, try to insert
            var remainder = blockItemHandler.insertItem(SLOT, stackInHand, simulate);

            if (!simulate)
                pPlayer.setItemInHand(pHand, remainder);

            if (remainder.getCount() != stackInHand.getCount()) {
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
