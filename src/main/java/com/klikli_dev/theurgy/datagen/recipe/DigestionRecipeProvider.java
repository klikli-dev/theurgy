// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import com.klikli_dev.theurgy.registry.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class DigestionRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = FermentationRecipe.DEFAULT_TIME;

    public DigestionRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "digestion");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipeWithTags(Fluids.WATER, 1000, List.of(
                Tags.Items.INGOTS_GOLD,
                ItemTagRegistry.ALCHEMICAL_SALTS
        ), ItemRegistry.PURIFIED_GOLD.get(), 10, TIME * 5);

        this.makeRecipe(FluidRegistry.SAL_AMMONIAC.get(), 10, List.of(
            Pair.of(SulfurRegistry.GEMS_ABUNDANT.get(), 4),
                Pair.of(ItemRegistry.PURIFIED_GOLD.get(), 1)
        ), SulfurRegistry.GEMS_COMMON.get(), 1, TIME * 5);

        this.makeRecipe(FluidRegistry.SAL_AMMONIAC.get(), 15, List.of(
            Pair.of(SulfurRegistry.GEMS_COMMON.get(), 4),
                Pair.of(ItemRegistry.PURIFIED_GOLD.get(), 1)
        ), SulfurRegistry.GEMS_RARE.get(), 1, TIME * 5);

        this.makeRecipe(FluidRegistry.SAL_AMMONIAC.get(), 50, List.of(
            Pair.of(SulfurRegistry.GEMS_RARE.get(), 4),
                Pair.of(ItemRegistry.PURIFIED_GOLD.get(), 1)
        ), SulfurRegistry.GEMS_PRECIOUS.get(), 1, TIME * 5);
    }

    public void makeRecipe(Fluid fluid, int fluidAmount, List<Pair<Item, Integer>> ingredients, Item result, int resultCount, int time) {
        this.makeRecipe(this.name(result), fluid, fluidAmount, ingredients, result, resultCount, time);
    }

    public void makeRecipe(String name, Fluid fluid, int fluidAmount, List<Pair<Item, Integer>> ingredients, Item result, int resultCount, int time) {
        var recipe = this.makeRecipeJson(
                this.makeFluidTagIngredient(this.locFor(fluid)),
                fluidAmount,
                ingredients.stream().map(i -> this.makeTagIngredient(this.locFor(i.getFirst()), i.getSecond())).toList(),
                this.makeItemStackCodecResult(this.locFor(result), resultCount),
                time);

        this.recipeConsumer.accept(this.modLoc(name), recipe);
    }

    public void makeRecipeWithTags(Fluid fluid, int fluidAmount, List<TagKey<Item>> ingredients, Item result, int resultCount, int time) {
        this.makeRecipeWithTags(this.name(result), fluid, fluidAmount, ingredients, result, resultCount, time);
    }

    public void makeRecipeWithTags(String name, Fluid fluid, int fluidAmount, List<TagKey<Item>> ingredients, Item result, int resultCount, int time) {
        var recipe = this.makeRecipeJson(
                this.makeFluidTagIngredient(this.locFor(fluid)),
                fluidAmount,
                ingredients.stream().map(i -> this.makeTagIngredient(this.locFor(i), 1)).toList(),
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
        recipe.addProperty("type", RecipeTypeRegistry.DIGESTION.getId().toString());
        recipe.add("fluid", fluid);
        recipe.addProperty("fluidAmount", fluidAmount);
        recipe.add("ingredients", ingredientsArray);
        recipe.add("result", result);
        recipe.addProperty("time", time);
        return recipe;
    }


    @Override
    public String getName() {
        return "Digestion Recipes";
    }
}
