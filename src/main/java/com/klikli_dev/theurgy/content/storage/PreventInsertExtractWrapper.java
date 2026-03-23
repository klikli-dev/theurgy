// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Wrapper for item storage that prevents insertion and extraction of items.
 */
public class PreventInsertExtractWrapper implements SettableItemStorage {
    protected final SettableItemStorage compose;

    public PreventInsertExtractWrapper(SettableItemStorage compose) {
        this.compose = compose;
    }

    @Override
    public int getSlots() {
        return SettableItemStorage.super.getSlots();
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(int slot) {
        return SettableItemStorage.super.getStackInSlot(slot);
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.compose.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlotLimit(int slot) {
        return SettableItemStorage.super.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }

    @Override
    public int size() {
        return this.compose.size();
    }

    @Override
    public net.neoforged.neoforge.transfer.item.ItemResource getResource(int index) {
        return this.compose.getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        return this.compose.getAmountAsLong(index);
    }

    @Override
    public long getCapacityAsLong(int index, net.neoforged.neoforge.transfer.item.ItemResource resource) {
        return this.compose.getCapacityAsLong(index, resource);
    }

    @Override
    public boolean isValid(int index, net.neoforged.neoforge.transfer.item.ItemResource resource) {
        return false;
    }

    @Override
    public int insert(int index, net.neoforged.neoforge.transfer.item.ItemResource resource, int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
        return 0;
    }

    @Override
    public int extract(int index, net.neoforged.neoforge.transfer.item.ItemResource resource, int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
        return 0;
    }

}
