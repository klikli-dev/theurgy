// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * An ItemHandler that wraps a list of item handlers, and conditionally returns one of them.
 */
public class SelectItemHandlerWrapper implements SettableItemStorage {

    protected final SettableItemStorage[] handlers;
    protected Supplier<Integer> selector;

    public SelectItemHandlerWrapper(Supplier<Integer> selector, SettableItemStorage... handlers) {
        this.handlers = handlers;
        this.selector = selector;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.handlers[this.selector.get()].setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return SettableItemStorage.super.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return SettableItemStorage.super.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return SettableItemStorage.super.insertItem(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return SettableItemStorage.super.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return SettableItemStorage.super.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return SettableItemStorage.super.isItemValid(slot, stack);
    }

    @Override
    public int size() {
        return this.handlers[this.selector.get()].size();
    }

    @Override
    public net.neoforged.neoforge.transfer.item.ItemResource getResource(int index) {
        return this.handlers[this.selector.get()].getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        return this.handlers[this.selector.get()].getAmountAsLong(index);
    }

    @Override
    public long getCapacityAsLong(int index, net.neoforged.neoforge.transfer.item.ItemResource resource) {
        return this.handlers[this.selector.get()].getCapacityAsLong(index, resource);
    }

    @Override
    public boolean isValid(int index, net.neoforged.neoforge.transfer.item.ItemResource resource) {
        return this.handlers[this.selector.get()].isValid(index, resource);
    }

    @Override
    public int insert(int index, net.neoforged.neoforge.transfer.item.ItemResource resource, int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
        return this.handlers[this.selector.get()].insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, net.neoforged.neoforge.transfer.item.ItemResource resource, int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
        return this.handlers[this.selector.get()].extract(index, resource, amount, transaction);
    }
}
