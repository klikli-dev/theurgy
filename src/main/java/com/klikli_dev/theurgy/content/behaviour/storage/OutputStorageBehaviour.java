// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.storage;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public interface OutputStorageBehaviour {
    ResourceHandler<ItemResource> outputInventory();

    boolean hasOutput();
}
