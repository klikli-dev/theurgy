// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class CombinedItemStorage extends CombinedResourceHandler<ItemResource> implements SettableItemStorage {
    public CombinedItemStorage(SettableItemStorage... handlers) {
        super(handlers);
    }

    @Override
    public void set(int slot, ItemResource resource, int amount) {
        throw new UnsupportedOperationException("Combined item storage does not support direct slot replacement");
    }
}

