// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.input;

import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import com.klikli_dev.theurgy.content.storage.FluidStorageHelper;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public class ItemHandlerWithFluidRecipeInput extends ItemHandlerRecipeInput {

    private final ResourceHandler<FluidResource> tank;

    public ItemHandlerWithFluidRecipeInput(SettableItemStorage inv, ResourceHandler<FluidResource> tank) {
        super(inv);
        this.tank = tank;
    }

    public ResourceHandler<FluidResource> getTank() {
        return this.tank;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < FluidStorageHelper.getTanks(this.getTank()); i++) {
            if (!FluidStorageHelper.getFluidInTank(this.getTank(), i).isEmpty()) {
                return false;
            }
        }

        return super.isEmpty();
    }
}
