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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;


public class DynamicOneOutputSlotItemHandlerBehaviour implements ItemHandlerBehaviour {

    protected int getOutputSlot(ResourceHandler<ItemResource> handler) {
        return ItemStorageHelper.getSlots(handler) - 1;
    }

    protected int getMaxInputSlot(ResourceHandler<ItemResource> handler) {
        return ItemStorageHelper.getSlots(handler) - 2;
    }

    /**
     * Default interaction for blocks that have a block entity with an input and an output inventory, where the output inventory has one slot and the input inventory a dynamic acmount of slots, made available as combined inventory with inputs on slot 0 to n-2 and output on slot n-1.
     */
    @Override
    public InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pHand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        var blockItemHandler = pLevel.getCapability(CapabilityRegistry.ITEM_HANDLER, pPos, null);
        //a block without item handler is of no interest
        if (blockItemHandler == null)
            return InteractionResult.PASS;

        var outputSlot = this.getOutputSlot(blockItemHandler);
        var maxInputSlot = this.getMaxInputSlot(blockItemHandler);

        ItemStack stackInHand = pPlayer.getItemInHand(pHand);

        if (stackInHand.isEmpty()) {
            //with empty hand first try take output
            var extracted = ItemStorageHelper.extractItem(blockItemHandler, outputSlot, ItemStorageHelper.getSlotLimit(blockItemHandler, outputSlot), false);
            if (!extracted.isEmpty()) {
                pPlayer.getInventory().placeItemBackInInventory(extracted);
                return InteractionResult.SUCCESS;
            }

            //if no output, try take input
            for (int inputSlot = 0; inputSlot <= maxInputSlot; inputSlot++) {
                extracted = ItemStorageHelper.extractItem(blockItemHandler, inputSlot, ItemStorageHelper.getSlotLimit(blockItemHandler, inputSlot), false);
                if (!extracted.isEmpty()) {
                    pPlayer.getInventory().placeItemBackInInventory(extracted);
                    return InteractionResult.SUCCESS;
                }
            }
        } else {
            for (int inputSlot = 0; inputSlot <= maxInputSlot; inputSlot++) {
                //if we have an item in hand, try to insert
                var remainder = ItemStorageHelper.insertItem(blockItemHandler, inputSlot, stackInHand, false);
                pPlayer.setItemInHand(pHand, remainder);
                if (remainder.getCount() != stackInHand.getCount()) {
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }
}
