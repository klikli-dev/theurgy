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
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;


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
            try (var tx = Transaction.openRoot()) {
                var extracted = ItemStack.EMPTY;
                var outputStack = ItemUtil.getStack(blockItemHandler, OUTPUT_SLOT);
                if (!outputStack.isEmpty()) {
                    var outputResource = ItemResource.of(outputStack);
                    extracted = outputResource.toStack(blockItemHandler.extract(OUTPUT_SLOT, outputResource, blockItemHandler.getCapacityAsInt(OUTPUT_SLOT, null), tx));
                }
                if (extracted.isEmpty()) {
                    var inputStack = ItemUtil.getStack(blockItemHandler, INPUT_SLOT);
                    if (!inputStack.isEmpty()) {
                        var inputResource = ItemResource.of(inputStack);
                        extracted = inputResource.toStack(blockItemHandler.extract(INPUT_SLOT, inputResource, blockItemHandler.getCapacityAsInt(INPUT_SLOT, null), tx));
                    }
                }
                if (!extracted.isEmpty()) {
                    tx.commit();
                    pPlayer.getInventory().placeItemBackInInventory(extracted);
                    return InteractionResult.SUCCESS;
                }
            }
        } else {
            try (var tx = Transaction.openRoot()) {
                var remainder = ItemUtil.insertItemReturnRemaining(blockItemHandler, INPUT_SLOT, stackInHand, false, tx);
                if (remainder.getCount() != stackInHand.getCount()) {
                    tx.commit();
                    pPlayer.setItemInHand(pHand, remainder);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }
}
