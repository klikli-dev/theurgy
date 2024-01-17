// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.wrapper;

import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public class RecipeWrapperWithFluid extends RecipeWrapper {

    private final IFluidHandler tank;

    public RecipeWrapperWithFluid(IItemHandlerModifiable inv, IFluidHandler tank) {
        super(inv);
        this.tank = tank;
    }

    public IFluidHandler getTank() {
        return this.tank;
    }
}
