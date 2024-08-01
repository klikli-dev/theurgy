// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.capability;

import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;

/**
 * A heat receiver that only wants to receive heat when ready for crafting.
 */
public class CraftingHeatReceiver extends DefaultHeatReceiver {

    protected HasCraftingBehaviour<?, ?, ?> parent;

    public CraftingHeatReceiver(HasCraftingBehaviour<?, ?, ?> parent) {
        this.parent = parent;
    }

    @Override
    public boolean readyToReceive() {
        return this.parent.craftingBehaviour().couldCraftLastTick();
    }
}
