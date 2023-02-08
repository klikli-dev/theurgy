/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class CalcinationRecipeProvider extends JsonRecipeProvider {

    public CalcinationRecipeProvider(DataGenerator pGenerator) {
        super(pGenerator, "calcination");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        recipeConsumer.accept(
                this.modLoc("alchemical_salt_from_ore"),
                this.buildCalcinationRecipe(
                        this.makeTagIngredient(this.forgeLoc("ores")),
                        this.makeResult(this.modLoc("alchemical_salt_ore")), 200));
    }

    public JsonObject buildCalcinationRecipe(JsonObject ingredient, JsonObject result, int cookingTime) {
        var recipe = new JsonObject();
        recipe.addProperty("type", RecipeTypeRegistry.CALCINATION.getId().toString());
        recipe.add("ingredient", ingredient);
        recipe.add("result", result);
        recipe.addProperty("cookingtime", cookingTime);
        return recipe;
    }

    @Override
    public String getName() {
        return "Calcination Recipes";
    }
}
