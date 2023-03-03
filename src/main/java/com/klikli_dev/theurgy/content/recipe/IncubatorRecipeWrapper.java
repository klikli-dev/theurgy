/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.recipe;

import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class IncubatorRecipeWrapper extends RecipeWrapper {
    private final IItemHandlerModifiable mercuryVesselInv;
    private final IItemHandlerModifiable saltVesselInv;
    private final IItemHandlerModifiable sulfurVesselInv;

    public IncubatorRecipeWrapper(IItemHandlerModifiable mercuryVesselInv, IItemHandlerModifiable saltVesselInv, IItemHandlerModifiable sulfurVesselInv) {
        super(new CombinedInvWrapper(mercuryVesselInv, saltVesselInv, sulfurVesselInv));

        this.mercuryVesselInv = mercuryVesselInv;
        this.saltVesselInv = saltVesselInv;
        this.sulfurVesselInv = sulfurVesselInv;
    }

    public IItemHandlerModifiable getMercuryVesselInv() {
        return this.mercuryVesselInv;
    }

    public IItemHandlerModifiable getSaltVesselInv() {
        return this.saltVesselInv;
    }

    public IItemHandlerModifiable getSulfurVesselInv() {
        return this.sulfurVesselInv;
    }
}
