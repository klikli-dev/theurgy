// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.input;

import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;


public class ItemHandlerWithFluidRecipeInput extends ItemHandlerRecipeInput {

    private final IFluidHandler tank;

    public ItemHandlerWithFluidRecipeInput(IItemHandlerModifiable inv, IFluidHandler tank) {
        super(inv);
        this.tank = tank;
    }

    public IFluidHandler getTank() {
        return this.tank;
    }
}
