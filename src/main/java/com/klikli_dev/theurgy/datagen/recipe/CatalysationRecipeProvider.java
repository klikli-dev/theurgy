/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.CatalysationRecipe;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.BiConsumer;

public class CatalysationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = CatalysationRecipe.DEFAULT_CATALYSATION_TIME;

    public CatalysationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "catalysation");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe("mercury_flux_from_mercury_shard", Ingredient.of(ItemRegistry.MERCURY_SHARD.get()), 250, TIME);
    }

    public void makeRecipe(String name, Ingredient ingredient, int mercuryFlux, int catalysationTime) {
        this.recipeConsumer.accept(
                this.modLoc(name),
                this.makeRecipeJson(
                        ingredient, mercuryFlux, catalysationTime));
    }

    public JsonObject makeRecipeJson(Ingredient ingredient, int mercuryFlux, int catalysationTime) {
        var recipe = new CatalysationRecipe(ingredient, mercuryFlux, catalysationTime);

        return CatalysationRecipe.CODEC.encodeStart(JsonOps.INSTANCE, recipe)
                .resultOrPartial(Theurgy.LOGGER::error).get().getAsJsonObject();
    }

    @Override
    public String getName() {
        return "Catalysation Recipes";
    }
}
