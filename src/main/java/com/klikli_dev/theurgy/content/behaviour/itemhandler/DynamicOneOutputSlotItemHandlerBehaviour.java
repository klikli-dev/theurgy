// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.itemhandler;

import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;


public class DynamicOneOutputSlotItemHandlerBehaviour implements ItemHandlerBehaviour {

    protected int getOutputSlot(ResourceHandler<ItemResource> handler) {
        return handler.size() - 1;
    }

    protected int getMaxInputSlot(ResourceHandler<ItemResource> handler) {
        return handler.size() - 2;
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
            try (var tx = Transaction.openRoot()) {
                var extracted = ItemStack.EMPTY;
                var outputStack = ItemUtil.getStack(blockItemHandler, outputSlot);
                if (!outputStack.isEmpty()) {
                    var outputResource = ItemResource.of(outputStack);
                    extracted = outputResource.toStack(blockItemHandler.extract(outputSlot, outputResource, blockItemHandler.getCapacityAsInt(outputSlot, null), tx));
                }
                if (extracted.isEmpty()) {
                    for (int inputSlot = 0; inputSlot <= maxInputSlot; inputSlot++) {
                        var inputStack = ItemUtil.getStack(blockItemHandler, inputSlot);
                        if (inputStack.isEmpty()) {
                            continue;
                        }

                        var resource = ItemResource.of(inputStack);
                        extracted = resource.toStack(blockItemHandler.extract(inputSlot, resource, blockItemHandler.getCapacityAsInt(inputSlot, null), tx));
                        if (!extracted.isEmpty()) break;
                    }
                }
                if (!extracted.isEmpty()) {
                    tx.commit();
                    pPlayer.getInventory().placeItemBackInInventory(extracted);
                    return InteractionResult.SUCCESS;
                }
            }
        } else {
            for (int inputSlot = 0; inputSlot <= maxInputSlot; inputSlot++) {
                try (var tx = Transaction.openRoot()) {
                    var remainder = ItemUtil.insertItemReturnRemaining(blockItemHandler, inputSlot, stackInHand, false, tx);
                    if (remainder.getCount() != stackInHand.getCount()) {
                        tx.commit();
                        pPlayer.setItemInHand(pHand, remainder);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }
}
