// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.input;

import com.klikli_dev.theurgy.content.capability.MercuryFluxStorage;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.List;
import java.util.stream.Stream;

public class ReformationArrayRecipeInput extends ItemHandlerRecipeInput {
    private final List<IItemHandlerModifiable> sourcePedestalInvs;
    private final IItemHandlerModifiable targetPedestalInv;

    private final MercuryFluxStorage mercuryFluxStorage;

    public ReformationArrayRecipeInput(List<IItemHandlerModifiable> sourcePedestalInvs, IItemHandlerModifiable targetPedestalInv, MercuryFluxStorage mercuryFluxStorage) {
        super(new CombinedInvWrapper(Stream.concat(Stream.of(targetPedestalInv), sourcePedestalInvs.stream())
                .toArray(IItemHandlerModifiable[]::new)));

        this.sourcePedestalInvs = sourcePedestalInvs;
        this.targetPedestalInv = targetPedestalInv;
        this.mercuryFluxStorage = mercuryFluxStorage;
    }

    public List<IItemHandlerModifiable> getSourcePedestalInvs() {
        return this.sourcePedestalInvs;
    }

    public IItemHandlerModifiable getTargetPedestalInv() {
        return this.targetPedestalInv;
    }

    public MercuryFluxStorage getMercuryFluxStorage() {
        return this.mercuryFluxStorage;
    }
}