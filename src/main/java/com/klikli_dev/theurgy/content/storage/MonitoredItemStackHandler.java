// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jetbrains.annotations.NotNull;


/**
 * A stack handler that provides additional functionality for monitoring changes.
 */
public abstract class MonitoredItemStackHandler extends ItemStacksResourceHandler implements SettableItemStorage {
    public MonitoredItemStackHandler() {
        super(1);
    }

    public MonitoredItemStackHandler(int size) {
        super(size);
    }

    public MonitoredItemStackHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }


    /**
     * Called when the content type of a slot changes, that means, a different Item, or a change between Empty (Air) and Non-Empty.
     * Mere changes of stack size do not trigger this.
     */
    protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {

    }

    protected void onSetStackInSlot(int slot, ItemStack oldStack, ItemStack newStack, boolean isSameItem) {

    }

    protected void onInsertItem(int slot, ItemStack oldStack, ItemStack newStack, ItemStack toInsert, ItemStack remainingInSource) {

    }

    protected void onExtractItem(int slot, ItemStack oldStack, ItemStack newStack, ItemStack extracted) {

    }

    protected void onContentsChanged(int slot) {

    }

    @Override
    protected void onContentsChanged(int slot, ItemStack previousContents) {
        this.onContentsChanged(slot);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack newStack) {
        var oldStack = this.getStackInSlot(slot).copy();

        boolean sameItem = ItemStack.isSameItemSameComponents(newStack, oldStack);

        this.set(slot, ItemResource.of(newStack), newStack.getCount());

        this.onSetStackInSlot(slot, oldStack, newStack, sameItem);
        if (!sameItem) {
            this.onContentTypeChanged(slot, oldStack, newStack);
        }
    }


    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack toInsert, boolean simulate) {
        if (!simulate) {
            var oldStack = this.getStackInSlot(slot).copy();
            var remaining = SettableItemStorage.super.insertItem(slot, toInsert, false);
            var newStack = this.getStackInSlot(slot);

            this.onInsertItem(slot, oldStack, newStack, toInsert, remaining);
            if (!ItemStack.isSameItemSameComponents(newStack, oldStack)) {
                this.onContentTypeChanged(slot, oldStack, newStack);
            }
            return remaining;
        }
        return SettableItemStorage.super.insertItem(slot, toInsert, true);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!simulate) {
            var oldStack = this.getStackInSlot(slot).copy();
            var extracted = SettableItemStorage.super.extractItem(slot, amount, false);
            var newStack = this.getStackInSlot(slot);

            this.onExtractItem(slot, oldStack, newStack, extracted);
            if (oldStack.isEmpty() != newStack.isEmpty()) {
                this.onContentTypeChanged(slot, oldStack, newStack);
            }

            return extracted;
        }
        return SettableItemStorage.super.extractItem(slot, amount, true);
    }
}
