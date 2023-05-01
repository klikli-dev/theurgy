/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

import java.util.function.BiConsumer;

public class CalcinationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = CalcinationRecipe.DEFAULT_CALCINATION_TIME;

    public CalcinationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "calcination");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe(SaltRegistry.ORE.get(), Tags.Items.ORES);
        this.makeRecipe(SaltRegistry.CROPS.get(), Tags.Items.CROPS);
    }

    public void makeRecipe(String saltName, Item ingredient) {
        this.makeRecipe(saltName, 1, ingredient, TIME);
    }

    public void makeRecipe(String saltName, Item ingredient, int calcinationTime) {
        this.makeRecipe(saltName, 1, ingredient, calcinationTime);
    }

    public void makeRecipe(String saltName, int resultCount, Item ingredient, int calcinationTime) {
        this.recipeConsumer.accept(
                this.modLoc(saltName),
                this.makeRecipeJson(
                        this.makeItemIngredient(this.locFor(ingredient)),
                        this.makeItemResult(this.modLoc("alchemical_salt_" + saltName), resultCount), calcinationTime));

    }

    public void makeRecipe(String saltName, TagKey<Item> ingredient) {
        this.makeRecipe(saltName, 1, ingredient, TIME);
    }


    public void makeRecipe(String saltName, TagKey<Item> ingredient, int calcinationTime) {
        this.makeRecipe(saltName, 1, ingredient, calcinationTime);
    }

    public void makeRecipe(String saltName, int resultCount, TagKey<Item> ingredient, int calcinationTime) {
        this.recipeConsumer.accept(
                this.modLoc(saltName),
                this.makeRecipeJson(
                        this.makeTagIngredient(this.locFor(ingredient)),
                        this.makeItemResult(this.modLoc("alchemical_salt_" + saltName), resultCount), calcinationTime));

    }

    public void makeRecipe(Item salt, TagKey<Item> ingredient) {
        this.makeRecipe(salt, 1, ingredient, TIME);
    }


    public void makeRecipe(Item salt, TagKey<Item> ingredient, int calcinationTime) {
        this.makeRecipe(salt, 1, ingredient, calcinationTime);
    }

    public void makeRecipe(Item salt, int resultCount, TagKey<Item> ingredient, int calcinationTime) {
        this.recipeConsumer.accept(
                this.modLoc(this.name(salt).replace("alchemical_salt_", "")),
                this.makeRecipeJson(
                        this.makeTagIngredient(this.locFor(ingredient)),
                        this.makeItemResult(this.locFor(salt), resultCount), calcinationTime));

    }

    public JsonObject makeRecipeJson(JsonObject ingredient, JsonObject result, int calcinationTime) {
        var recipe = new JsonObject();
        recipe.addProperty("type", RecipeTypeRegistry.CALCINATION.getId().toString());
        recipe.add("ingredient", ingredient);
        recipe.add("result", result);
        recipe.addProperty("calcination_time", calcinationTime);
        return recipe;
    }

    @Override
    public String getName() {
        return "Calcination Recipes";
    }
}
