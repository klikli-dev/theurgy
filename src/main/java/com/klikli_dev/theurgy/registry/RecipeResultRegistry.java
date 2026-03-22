// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.result.ItemRecipeResult;
import com.klikli_dev.theurgy.content.recipe.result.RecipeResultType;
import com.klikli_dev.theurgy.content.recipe.result.TagRecipeResult;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RecipeResultRegistry {
    public static final DeferredRegister<RecipeResultType<?>> RECIPE_RESULT_TYPES = DeferredRegister.create(
            TheurgyRegistries.RECIPE_RESULT_TYPES, Theurgy.MODID);


    public static final DeferredHolder<RecipeResultType<?>, RecipeResultType<ItemRecipeResult>> ITEM =
            RECIPE_RESULT_TYPES.register("item", () -> new RecipeResultType<>(ItemRecipeResult.CODEC, ItemRecipeResult.STREAM_CODEC));

    public static final DeferredHolder<RecipeResultType<?>, RecipeResultType<TagRecipeResult>> TAG =
            RECIPE_RESULT_TYPES.register("tag", () -> new RecipeResultType<>(TagRecipeResult.CODEC, TagRecipeResult.STREAM_CODEC));


}
