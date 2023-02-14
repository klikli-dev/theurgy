/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public RecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        //we need nbt output, so we use a manual json recipe
        //        ShapedRecipeBuilder.shaped(ItemRegistry.DIVINATION_ROD_T1.get())
//                .define('R', Tags.Items.RODS_WOODEN)
//                .define('G', Tags.Items.GLASS)
//                .pattern("RGR")
//                .pattern("R R")
//                .pattern(" R ")
//                .unlockedBy("has_wooden_rod", has(Tags.Items.RODS_WOODEN)) //Note: this generates an advancement that we may or may not want.
//                .save(pFinishedRecipeConsumer);
    }
}
