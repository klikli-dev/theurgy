// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.input;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

public class IncubatorRecipeInput extends ItemHandlerRecipeInput {
    private final IItemHandlerModifiable mercuryVesselInv;
    private final IItemHandlerModifiable saltVesselInv;
    private final IItemHandlerModifiable sulfurVesselInv;

    public IncubatorRecipeInput(IItemHandlerModifiable mercuryVesselInv, IItemHandlerModifiable saltVesselInv, IItemHandlerModifiable sulfurVesselInv) {
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
