package com.klikli_dev.theurgy.content.behaviour.storage;

import net.neoforged.neoforge.items.IItemHandler;

public interface OutputStorageBehaviour {
    IItemHandler outputInventory();

    boolean hasOutput();
}
