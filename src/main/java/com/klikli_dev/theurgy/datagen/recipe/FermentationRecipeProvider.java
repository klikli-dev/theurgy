// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.function.BiConsumer;

public class FermentationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = FermentationRecipe.DEFAULT_TIME;

    public FermentationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "fermentation");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe(Fluids.WATER, 1000, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT,
                ItemTagRegistry.SUGAR
        ), SulfurRegistry.GEMS_ABUNDANT.get(), 1, TIME);
    }
    public void makeRecipe(Fluid fluid, int fluidAmount, List<TagKey<Item>> ingredients, Item result, int resultCount, int time) {
        this.makeRecipe(this.name(result), fluid, fluidAmount, ingredients, result, resultCount, time);
    }
    public void makeRecipe(String name, Fluid fluid, int fluidAmount, List<TagKey<Item>> ingredients, Item result, int resultCount, int time) {
        var recipe = this.makeRecipeJson(
                this.makeFluidTagIngredient(this.locFor(fluid)),
                fluidAmount,
                ingredients.stream().map(i -> this.makeTagIngredient(this.locFor(i))).toList(),
                this.makeItemStackCodecResult(this.locFor(result), resultCount),
                time);

        var conditions = new JsonArray();
        for (var ingredient : ingredients) {
            conditions.add(this.makeTagNotEmptyCondition(ingredient.location().toString()));
        }
        recipe.add("conditions", conditions);

        this.recipeConsumer.accept(this.modLoc(name), recipe);
    }


    public JsonObject makeRecipeJson(JsonObject fluid, int fluidAmount, List<JsonObject> ingredients, JsonObject result, int time) {
        var ingredientsArray = new JsonArray();
        for (var ingredient : ingredients) {
            ingredientsArray.add(ingredient);
        }

        var recipe = new JsonObject();
        recipe.addProperty("type", RecipeTypeRegistry.FERMENTATION.getId().toString());
        recipe.add("fluid", fluid);
        recipe.addProperty("fluidAmount", fluidAmount);
        recipe.add("ingredients", ingredientsArray);
        recipe.add("result", result);
        recipe.addProperty("time", time);
        return recipe;
    }


    @Override
    public String getName() {
        return "Fermentation Recipes";
    }
}
