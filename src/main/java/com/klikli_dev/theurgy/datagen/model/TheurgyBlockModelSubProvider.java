// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.model;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;

public class TheurgyBlockModelSubProvider {

    public void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // TODO 1.21.5: rewrite custom blockstate/model datagen against the new blockstate API.
        // The previous implementation targeted the older VariantProperties/PropertyDispatch helpers
        // and no longer compiles on 1.21.5.
    }
}

