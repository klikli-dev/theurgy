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


public class TwoSlotItemHandlerBehaviour implements ItemHandlerBehaviour {

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    /**
     * Default interaction for blocks that have a block entity with an input and an output inventory with one slot each, made available as combined inventory with input on slot 0 and output on slot 1.
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
            //with empty hand first try take output
            var extracted = blockItemHandler.extractItem(OUTPUT_SLOT, blockItemHandler.getSlotLimit(OUTPUT_SLOT), simulate);
            if (!extracted.isEmpty()) {
                if (!simulate)
                    pPlayer.getInventory().placeItemBackInInventory(extracted);
                return ItemInteractionResult.SUCCESS;
            }

            //if no output, try take input
            extracted = blockItemHandler.extractItem(INPUT_SLOT, blockItemHandler.getSlotLimit(INPUT_SLOT), simulate);
            if (!extracted.isEmpty()) {
                if (!simulate)
                    pPlayer.getInventory().placeItemBackInInventory(extracted);
                return ItemInteractionResult.SUCCESS;
            }
        } else {
            //if we have an item in hand, try to insert
            var remainder = blockItemHandler.insertItem(INPUT_SLOT, stackInHand, simulate);
            if (!simulate)
                pPlayer.setItemInHand(pHand, remainder);

            if (remainder.getCount() != stackInHand.getCount()) {
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
