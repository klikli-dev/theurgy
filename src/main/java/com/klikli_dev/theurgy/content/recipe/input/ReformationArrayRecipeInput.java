// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.input;

import com.klikli_dev.theurgy.content.capability.MercuryFluxStorage;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.List;
import java.util.stream.Stream;

public class ReformationArrayRecipeInput extends ItemHandlerRecipeInput {
    private final List<SettableItemStorage> sourcePedestalInvs;
    private final SettableItemStorage targetPedestalInv;

    private final MercuryFluxStorage mercuryFluxStorage;

    public ReformationArrayRecipeInput(List<SettableItemStorage> sourcePedestalInvs, SettableItemStorage targetPedestalInv, MercuryFluxStorage mercuryFluxStorage) {
        super(new CombinedResourceHandler<ItemResource>(Stream.concat(Stream.of(targetPedestalInv), sourcePedestalInvs.stream())
                .toArray(SettableItemStorage[]::new)));

        this.sourcePedestalInvs = sourcePedestalInvs;
        this.targetPedestalInv = targetPedestalInv;
        this.mercuryFluxStorage = mercuryFluxStorage;
    }

    public List<SettableItemStorage> getSourcePedestalInvs() {
        return this.sourcePedestalInvs;
    }

    public SettableItemStorage getTargetPedestalInv() {
        return this.targetPedestalInv;
    }

    public MercuryFluxStorage getMercuryFluxStorage() {
        return this.mercuryFluxStorage;
    }
}