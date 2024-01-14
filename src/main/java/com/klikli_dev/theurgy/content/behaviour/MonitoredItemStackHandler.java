// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour;

import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationSourcePedestalBlockEntity;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/**
 * A stack handler that provides additional functionality for monitoring changes.
 */
public abstract class MonitoredItemStackHandler extends ItemStackHandler {
    public MonitoredItemStackHandler() {
        super();
    }

    public MonitoredItemStackHandler(int size) {
        super(size);
    }

    public MonitoredItemStackHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    protected void onSetStackInSlot(int slot, ItemStack oldStack, ItemStack newStack, boolean isSameItem) {

    }

    protected void onInsertItem(int slot, ItemStack oldStack, ItemStack newStack, ItemStack toInsert, ItemStack remaining) {

    }

    protected void onExtractItem(int slot, ItemStack oldStack, ItemStack newStack, ItemStack extracted) {

    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack newStack) {
        var oldStack = this.getStackInSlot(slot);

        boolean sameItem = !newStack.isEmpty() && ItemStack.isSameItemSameTags(newStack, oldStack);

        super.setStackInSlot(slot, newStack);

        this.onSetStackInSlot(slot, oldStack, newStack, sameItem);
    }


    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (!simulate) {
            var oldStack = this.getStackInSlot(slot);
            var remaining = super.insertItem(slot, stack, simulate);
            var newStack = this.getStackInSlot(slot);

            this.onInsertItem(slot, oldStack, newStack, stack, remaining);

            return remaining;
        }
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!simulate) {
            var oldStack = this.getStackInSlot(slot);
            var extracted = super.extractItem(slot, amount, simulate);
            var newStack = this.getStackInSlot(slot);

            this.onExtractItem(slot, oldStack, newStack, extracted);

            return extracted;
        }
        return super.extractItem(slot, amount, simulate);
    }
}
