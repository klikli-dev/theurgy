// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.itemhandler;

import com.klikli_dev.theurgy.content.storage.ItemStorageHelper;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;


public class TwoSlotItemHandlerBehaviour implements ItemHandlerBehaviour {

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    /**
     * Default interaction for blocks that have a block entity with an input and an output inventory with one slot each, made available as combined inventory with input on slot 0 and output on slot 1.
     */
    @Override
    public InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pHand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        var blockItemHandler = pLevel.getCapability(CapabilityRegistry.ITEM_HANDLER, pPos, null);
        //a block without item handler is of no interest
        if (blockItemHandler == null)
            return InteractionResult.PASS;

        ItemStack stackInHand = pPlayer.getItemInHand(pHand);

        if (stackInHand.isEmpty()) {
            //with empty hand first try take output
            var extracted = ItemStorageHelper.extractItem(blockItemHandler, OUTPUT_SLOT, ItemStorageHelper.getSlotLimit(blockItemHandler, OUTPUT_SLOT), false);
            if (!extracted.isEmpty()) {
                pPlayer.getInventory().placeItemBackInInventory(extracted);
                return InteractionResult.SUCCESS;
            }

            //if no output, try take input
            extracted = ItemStorageHelper.extractItem(blockItemHandler, INPUT_SLOT, ItemStorageHelper.getSlotLimit(blockItemHandler, INPUT_SLOT), false);
            if (!extracted.isEmpty()) {
                pPlayer.getInventory().placeItemBackInInventory(extracted);
                return InteractionResult.SUCCESS;
            }
        } else {
            //if we have an item in hand, try to insert
            var remainder = ItemStorageHelper.insertItem(blockItemHandler, INPUT_SLOT, stackInHand, false);
            pPlayer.setItemInHand(pHand, remainder);
            if (remainder.getCount() != stackInHand.getCount()) {
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
