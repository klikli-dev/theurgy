// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.itemhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class DynamicOneOutputSlotItemHandlerBehaviour implements ItemHandlerBehaviour {

    protected int getOutputSlot(IItemHandler handler){
        return handler.getSlots() - 1;
    }

    protected int getMaxInputSlot(IItemHandler handler){
        return handler.getSlots() - 2;
    }

    /**
     * Default interaction for blocks that have a block entity with an input and an output inventory, where the output inventory has one slot and the input inventory a dynamic acmount of slots, made available as combined inventory with inputs on slot 0 to n-2 and output on slot n-1.
     */
    @Override
    public InteractionResult useItemHandler(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pHand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        var blockEntity = pLevel.getBlockEntity(pPos);

        if (blockEntity == null)
            return InteractionResult.PASS;

        var blockItemHandlerCap = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (!blockItemHandlerCap.isPresent())
            return InteractionResult.PASS;

        var blockItemHandler = blockItemHandlerCap.orElse(null);
        var outputSlot = getOutputSlot(blockItemHandler);
        var maxInputSlot = getMaxInputSlot(blockItemHandler);

        ItemStack stackInHand = pPlayer.getItemInHand(pHand);

        if (stackInHand.isEmpty()) {
            //with empty hand first try take output
            var extracted = blockItemHandler.extractItem(outputSlot, blockItemHandler.getSlotLimit(outputSlot), false);
            if (!extracted.isEmpty()) {
                pPlayer.getInventory().placeItemBackInInventory(extracted);
                return InteractionResult.SUCCESS;
            }

            //if no output, try take input
            for(int inputSlot = 0; inputSlot <= maxInputSlot; inputSlot++){
                extracted = blockItemHandler.extractItem(inputSlot, blockItemHandler.getSlotLimit(inputSlot), false);
                if (!extracted.isEmpty()) {
                    pPlayer.getInventory().placeItemBackInInventory(extracted);
                    return InteractionResult.SUCCESS;
                }
            }
        } else {
            for(int inputSlot = 0; inputSlot <= maxInputSlot; inputSlot++){
                //if we have an item in hand, try to insert
                var remainder = blockItemHandler.insertItem(inputSlot, stackInHand, false);
                pPlayer.setItemInHand(pHand, remainder);
                if (remainder.getCount() != stackInHand.getCount()) {
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }
}
