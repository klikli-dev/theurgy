/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.recipe.wrapper;

import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class RecipeWrapperWithFluid extends RecipeWrapper {

    private final FluidTank tank;

    public RecipeWrapperWithFluid(IItemHandlerModifiable inv, FluidTank tank) {
        super(inv);
        this.tank = tank;
    }

    public FluidTank getTank() {
        return this.tank;
    }
}
