/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

import java.util.function.BiConsumer;

public class DistillationRecipeProvider extends JsonRecipeProvider {

    public DistillationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "distillation");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {

    }

    public void makeMercuryShardRecipe(String recipeName, int resultAmount, TagKey<Item> ingredient, int distillationTime, BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {

        recipeConsumer.accept(
                this.modLoc(recipeName),
                this.makeRecipeJson(
                        this.makeTagIngredient(ingredient.location()),
                        this.makeResult(this.locFor(ItemRegistry.MERCURY_SHARD.get()), resultAmount), distillationTime));

    }

    public void makeMercuryShardRecipe(String recipeName, int resultAmount, Item ingredient, int distillationTime, BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {

        recipeConsumer.accept(
                this.modLoc(recipeName),
                this.makeRecipeJson(
                        this.makeItemIngredient(this.locFor(ingredient)),
                        this.makeResult(this.locFor(ItemRegistry.MERCURY_SHARD.get()), resultAmount), distillationTime));

    }

    public JsonObject makeRecipeJson(JsonObject ingredient, JsonObject result, int distillationTime) {
        var recipe = new JsonObject();
        recipe.addProperty("type", RecipeTypeRegistry.DISTILLATION.getId().toString());
        recipe.add("ingredient", ingredient);
        recipe.add("result", result);
        recipe.addProperty("distillation_time", distillationTime);
        return recipe;
    }

    @Override
    public String getName() {
        return "Distillation Recipes";
    }
}
