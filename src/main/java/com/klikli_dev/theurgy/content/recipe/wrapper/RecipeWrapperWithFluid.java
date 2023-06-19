/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.recipe.wrapper;

import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

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
