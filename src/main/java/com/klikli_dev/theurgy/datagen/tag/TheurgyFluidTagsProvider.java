// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.tag;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.FluidRegistry;
import com.klikli_dev.theurgy.registry.FluidTagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

import java.util.concurrent.CompletableFuture;

public class TheurgyFluidTagsProvider extends FluidTagsProvider {

    public TheurgyFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Theurgy.MODID);
    }

    private ResourceKey<Fluid> key(Fluid fluid) {
        return BuiltInRegistries.FLUID.getResourceKey(fluid).orElseThrow();
    }

    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(FluidTagRegistry.SAL_AMMONIAC).add(this.key(FluidRegistry.SAL_AMMONIAC.get()), this.key(FluidRegistry.SAL_AMMONIAC_FLOWING.get()));
        this.tag(FluidTagRegistry.SOLVENT).addTag(FluidTagRegistry.SAL_AMMONIAC);
    }
}
