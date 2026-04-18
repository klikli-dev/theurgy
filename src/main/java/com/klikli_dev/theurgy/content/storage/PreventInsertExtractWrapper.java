// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
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
    public void set(int slot, ItemResource resource, int amount) {
        this.compose.set(slot, resource, amount);
    }

    @Override
    public int size() {
        return this.compose.size();
    }

    @Override
    public ItemResource getResource(int index) {
        return this.compose.getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        return this.compose.getAmountAsLong(index);
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        return this.compose.getCapacityAsLong(index, resource);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return false;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

}
