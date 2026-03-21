// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public final class ItemStorageHelper {
    private ItemStorageHelper() {
    }

    public static int getSlots(ResourceHandler<ItemResource> handler) {
        return handler.size();
    }

    public static ItemStack getStackInSlot(ResourceHandler<ItemResource> handler, int slot) {
        return ItemUtil.getStack(handler, slot);
    }

    public static ItemStack insertItem(ResourceHandler<ItemResource> handler, int slot, ItemStack stack, boolean simulate) {
        return ItemUtil.insertItemReturnRemaining(handler, slot, stack, simulate, null);
    }

    public static ItemStack insertItemStacked(ResourceHandler<ItemResource> handler, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        try (var tx = Transaction.openRoot()) {
            int inserted = ResourceHandlerUtil.insertStacking(handler, ItemResource.of(stack), stack.getCount(), tx);
            if (!simulate) {
                tx.commit();
            }

            int remaining = stack.getCount() - inserted;
            return remaining == 0 ? ItemStack.EMPTY : stack.copyWithCount(remaining);
        }
    }

    public static ItemStack extractItem(ResourceHandler<ItemResource> handler, int slot, int amount, boolean simulate) {
        if (amount <= 0) {
            return ItemStack.EMPTY;
        }

        var resource = handler.getResource(slot);
        if (resource.isEmpty()) {
            return ItemStack.EMPTY;
        }

        amount = Math.min(amount, resource.getMaxStackSize());
        try (var tx = Transaction.openRoot()) {
            int extracted = handler.extract(slot, resource, amount, tx);
            if (!simulate) {
                tx.commit();
            }
            return resource.toStack(extracted);
        }
    }

    public static int getSlotLimit(ResourceHandler<ItemResource> handler, int slot) {
        return handler.getCapacityAsInt(slot, ItemResource.EMPTY);
    }

    public static boolean isItemValid(ResourceHandler<ItemResource> handler, int slot, ItemStack stack) {
        return stack.isEmpty() || handler.isValid(slot, ItemResource.of(stack));
    }
}

