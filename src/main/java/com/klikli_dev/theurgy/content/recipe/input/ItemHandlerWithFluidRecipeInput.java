// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.input;

import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class ItemHandlerWithFluidRecipeInput extends ItemHandlerRecipeInput {

    private final IFluidHandler tank;

    public ItemHandlerWithFluidRecipeInput(SettableItemStorage inv, IFluidHandler tank) {
        super(inv);
        this.tank = tank;
    }

    public IFluidHandler getTank() {
        return this.tank;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getTank().getTanks(); i++) {
            if (!this.getTank().getFluidInTank(i).isEmpty()) {
                return false;
            }
        }

        return super.isEmpty();
    }
}
