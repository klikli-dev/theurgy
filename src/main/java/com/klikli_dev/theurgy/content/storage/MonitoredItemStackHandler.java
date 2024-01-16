// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationSourcePedestalBlockEntity;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.ItemStackHandler;
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

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack newStack) {
        var oldStack = this.getStackInSlot(slot).copy();

        boolean sameItem = !newStack.isEmpty() && ItemStack.isSameItemSameTags(newStack, oldStack);

        super.setStackInSlot(slot, newStack);

        this.onSetStackInSlot(slot, oldStack, newStack, sameItem);
        if(!sameItem){
            this.onContentTypeChanged(slot, oldStack, newStack);
        }
    }


    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack toInsert, boolean simulate) {
        if (!simulate) {
            var oldStack = this.getStackInSlot(slot).copy();
            var remaining = super.insertItem(slot, toInsert, simulate);
            var newStack = this.getStackInSlot(slot);

            this.onInsertItem(slot, oldStack, newStack, toInsert, remaining);
            if (remaining != toInsert) {
                this.onContentTypeChanged(slot, oldStack, newStack);
            }
            return remaining;
        }
        return super.insertItem(slot, toInsert, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!simulate) {
            var oldStack = this.getStackInSlot(slot).copy();
            var extracted = super.extractItem(slot, amount, simulate);
            var newStack = this.getStackInSlot(slot);

            this.onExtractItem(slot, oldStack, newStack, extracted);
            if(newStack.isEmpty()){
                this.onContentTypeChanged(slot, oldStack, newStack);
            }

            return extracted;
        }
        return super.extractItem(slot, amount, simulate);
    }
}
