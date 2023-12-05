package com.klikli_dev.theurgy.content.recipe.wrapper;

import com.klikli_dev.theurgy.content.capability.MercuryFluxStorage;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ReformationArrayRecipeWrapper extends RecipeWrapper {
    private final List<IItemHandlerModifiable> sourcePedestalInvs;
    private final IItemHandlerModifiable targetPedestalInv;

    private final MercuryFluxStorage mercuryFluxStorage;

    public ReformationArrayRecipeWrapper(List<IItemHandlerModifiable> sourcePedestalInvs, IItemHandlerModifiable targetPedestalInv, MercuryFluxStorage mercuryFluxStorage) {
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