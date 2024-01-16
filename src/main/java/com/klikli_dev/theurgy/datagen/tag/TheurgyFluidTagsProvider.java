// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.tag;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.FluidRegistry;
import com.klikli_dev.theurgy.registry.FluidTagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TheurgyFluidTagsProvider extends FluidTagsProvider {

    public TheurgyFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Theurgy.MODID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(FluidTagRegistry.SAL_AMMONIAC).add(FluidRegistry.SAL_AMMONIAC.get(), FluidRegistry.SAL_AMMONIAC_FLOWING.get());
        this.tag(FluidTagRegistry.SOLVENT).addTag(FluidTagRegistry.SAL_AMMONIAC);
    }
}