// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public interface SettableItemStorage extends ResourceHandler<ItemResource> {
    void setStackInSlot(int slot, ItemStack stack);

    default int getSlots() {
        return ItemStorageHelper.getSlots(this);
    }

    default ItemStack getStackInSlot(int slot) {
        return ItemStorageHelper.getStackInSlot(this, slot);
    }

    default ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return ItemStorageHelper.insertItem(this, slot, stack, simulate);
    }

    default ItemStack insertItemStacked(ItemStack stack, boolean simulate) {
        return ItemStorageHelper.insertItemStacked(this, stack, simulate);
    }

    default ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStorageHelper.extractItem(this, slot, amount, simulate);
    }

    default int getSlotLimit(int slot) {
        return ItemStorageHelper.getSlotLimit(this, slot);
    }

    @Override
    default boolean isValid(int slot, ItemResource resource) {
        return resource.isEmpty() || this.isItemValid(slot, resource.toStack(1));
    }

    default boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }
}

