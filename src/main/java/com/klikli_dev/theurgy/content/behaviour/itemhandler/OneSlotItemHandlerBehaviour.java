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


public class OneSlotItemHandlerBehaviour implements ItemHandlerBehaviour {

    public static final int SLOT = 0;

    /**
     * Default interaction for blocks that have a block entity with an in/output inventory with one slot.
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
                var stackInSlot = ItemUtil.getStack(blockItemHandler, SLOT);
                var extracted = ItemStack.EMPTY;
                if (!stackInSlot.isEmpty()) {
                    var resource = ItemResource.of(stackInSlot);
                    extracted = resource.toStack(blockItemHandler.extract(SLOT, resource, blockItemHandler.getCapacityAsInt(SLOT, null), tx));
                }
                if (!extracted.isEmpty()) {
                    tx.commit();
                    pPlayer.getInventory().placeItemBackInInventory(extracted);
                    return InteractionResult.SUCCESS;
                }
            }
        } else {
            try (var tx = Transaction.openRoot()) {
                var remainder = ItemUtil.insertItemReturnRemaining(blockItemHandler, SLOT, stackInHand, false, tx);
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
