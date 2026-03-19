// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.model;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

public class TheurgyModelProvider extends ModelProvider {
    private final TheurgyBlockModelSubProvider blockModels = new TheurgyBlockModelSubProvider();
    private final TheurgyItemModelSubProvider itemModels = new TheurgyItemModelSubProvider();

    public TheurgyModelProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID);
    }

    @Override
    public @NotNull String getName() {
        return "Model Definitions - " + this.modId;
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
        this.blockModels.registerModels(blockModels, itemModels);
        this.itemModels.registerModels(itemModels);
    }
}
