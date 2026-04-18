// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.Objects;

public class ComponentItemStorage extends ItemAccessItemHandler implements SettableItemStorage {
    public ComponentItemStorage(ItemStack parent, DataComponentType<ItemContainerContents> component, int size) {
        super(ItemAccess.forStack(parent), component, size);
    }

    @Override
    public void set(int slot, ItemResource resource, int amount) {
        Objects.checkIndex(slot, this.size());
        if (!resource.isEmpty() && !this.isValid(slot, resource)) {
            throw new RuntimeException("Invalid stack " + resource + " for slot " + slot + ")");
        }

        var accessResource = this.itemAccess.getResource();
        var updatedResource = this.update(accessResource, slot, resource, amount);
        if (updatedResource.isEmpty()) {
            throw new RuntimeException("Unable to update stack " + resource + " for slot " + slot + ")");
        }

        try (var tx = Transaction.openRoot()) {
            this.itemAccess.exchange(updatedResource, this.itemAccess.getAmount(), tx);
            tx.commit();
        }
    }
}

