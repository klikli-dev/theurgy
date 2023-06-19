/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.recipe.wrapper;

import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class RecipeWrapperWithFluid extends RecipeWrapper {

    private final IFluidTank tank;

    public RecipeWrapperWithFluid(IItemHandlerModifiable inv, IFluidTank tank) {
        super(inv);
        this.tank = tank;
    }

    public IFluidTank getTank() {
        return this.tank;
    }
}
