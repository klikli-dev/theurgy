// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.ArrayList;
import java.util.List;


/**
 * A stack handler that provides additional functionality for monitoring changes.
 */
public abstract class MonitoredItemStackHandler extends ItemStacksResourceHandler implements SettableItemStorage {
    private final ItemChangeJournal itemChangeJournal = new ItemChangeJournal();

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

    protected void onInsert(int slot, ItemStack oldStack, ItemStack newStack, ItemStack inserted, ItemStack remainingInSource) {

    }

    protected void onExtract(int slot, ItemStack oldStack, ItemStack newStack, ItemStack extracted) {

    }

    protected void onContentsChanged(int slot) {

    }

    @Override
    protected void onContentsChanged(int slot, ItemStack previousContents) {
        this.onContentsChanged(slot);
    }

    @Override
    public void set(int slot, ItemResource resource, int amount) {
        var oldStack = ItemUtil.getStack(this, slot).copy();
        var newStack = resource.toStack(amount);

        boolean sameItem = ItemStack.isSameItemSameComponents(newStack, oldStack);

        super.set(slot, resource, amount);

        this.onSetStackInSlot(slot, oldStack, newStack, sameItem);
        if (!sameItem) {
            this.onContentTypeChanged(slot, oldStack, newStack);
        }
    }


    @Override
    public int insert(int slot, ItemResource resource, int amount, TransactionContext transaction) {
        var oldStack = ItemUtil.getStack(this, slot).copy();
        int inserted = super.insert(slot, resource, amount, transaction);
        if (inserted > 0) {
            var insertedStack = resource.toStack(inserted);
            var remaining = resource.toStack(amount - inserted);
            var newStack = ItemUtil.getStack(this, slot).copy();
            this.itemChangeJournal.updateSnapshots(transaction);
            this.itemChangeJournal.recordInsert(slot, oldStack, newStack, insertedStack, remaining);
        }
        return inserted;
    }

    @Override
    public int extract(int slot, ItemResource resource, int amount, TransactionContext transaction) {
        var oldStack = ItemUtil.getStack(this, slot).copy();
        int extractedAmount = super.extract(slot, resource, amount, transaction);
        if (extractedAmount > 0) {
            var newStack = ItemUtil.getStack(this, slot).copy();
            this.itemChangeJournal.updateSnapshots(transaction);
            this.itemChangeJournal.recordExtract(slot, oldStack, newStack, resource.toStack(extractedAmount));
        }
        return extractedAmount;
    }

    private enum ItemChangeKind {INSERT, EXTRACT}

    private record ItemChange(ItemChangeKind kind, int slot, ItemStack oldStack, ItemStack newStack,
                              ItemStack affectedStack, ItemStack remainingStack) {
        static ItemChange insert(int slot, ItemStack oldStack, ItemStack newStack, ItemStack insertedStack, ItemStack remaining) {
            return new ItemChange(ItemChangeKind.INSERT, slot, oldStack, newStack, insertedStack, remaining);
        }

        static ItemChange extract(int slot, ItemStack oldStack, ItemStack newStack, ItemStack extracted) {
            return new ItemChange(ItemChangeKind.EXTRACT, slot, oldStack, newStack, extracted, ItemStack.EMPTY);
        }
    }

    private class ItemChangeJournal extends SnapshotJournal<List<ItemChange>> {
        private final List<ItemChange> pending = new ArrayList<>();

        void recordInsert(int slot, ItemStack oldStack, ItemStack newStack, ItemStack insertedStack, ItemStack remaining) {
            this.pending.add(ItemChange.insert(slot, oldStack, newStack, insertedStack, remaining));
        }

        void recordExtract(int slot, ItemStack oldStack, ItemStack newStack, ItemStack extracted) {
            this.pending.add(ItemChange.extract(slot, oldStack, newStack, extracted));
        }

        @Override
        protected List<ItemChange> createSnapshot() {
            return List.copyOf(this.pending);
        }

        @Override
        protected void revertToSnapshot(List<ItemChange> snapshot) {
            this.pending.clear();
            this.pending.addAll(snapshot);
        }

        @Override
        protected void onRootCommit(List<ItemChange> originalState) {
            for (var change : this.pending) {
                if (change.kind() == ItemChangeKind.INSERT) {
                    MonitoredItemStackHandler.this.onInsert(change.slot(), change.oldStack(), change.newStack(), change.affectedStack(), change.remainingStack());
                } else if (change.kind() == ItemChangeKind.EXTRACT) {
                    MonitoredItemStackHandler.this.onExtract(change.slot(), change.oldStack(), change.newStack(), change.affectedStack());
                }

                if (!ItemStack.isSameItemSameComponents(change.newStack(), change.oldStack())) {
                    MonitoredItemStackHandler.this.onContentTypeChanged(change.slot(), change.oldStack(), change.newStack());
                }
            }
            this.pending.clear();
        }
    }
}
