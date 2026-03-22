// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.input;

import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class IncubatorRecipeInput extends ItemHandlerRecipeInput {
    private final SettableItemStorage mercuryVesselInv;
    private final SettableItemStorage saltVesselInv;
    private final SettableItemStorage sulfurVesselInv;

    public IncubatorRecipeInput(SettableItemStorage mercuryVesselInv, SettableItemStorage saltVesselInv, SettableItemStorage sulfurVesselInv) {
        super(new CombinedResourceHandler<ItemResource>(mercuryVesselInv, saltVesselInv, sulfurVesselInv));

        this.mercuryVesselInv = mercuryVesselInv;
        this.saltVesselInv = saltVesselInv;
        this.sulfurVesselInv = sulfurVesselInv;
    }

    public SettableItemStorage getMercuryVesselInv() {
        return this.mercuryVesselInv;
    }

    public SettableItemStorage getSaltVesselInv() {
        return this.saltVesselInv;
    }

    public SettableItemStorage getSulfurVesselInv() {
        return this.sulfurVesselInv;
    }
}
