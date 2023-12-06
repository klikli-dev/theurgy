// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.ReformationRecipe;
import com.klikli_dev.theurgy.registry.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.List;
import java.util.function.BiConsumer;

public class ReformationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = ReformationRecipe.DEFAULT_REFORMATION_TIME;

    public ReformationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "reformation");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe(SulfurRegistry.IRON.get(), ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON, 100);
        this.makeRecipe(SulfurRegistry.TIN.get(), ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON, 100);
        this.makeRecipe(SulfurRegistry.ZINC.get(), ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON, 100);
        this.makeRecipe(SulfurRegistry.LEAD.get(), ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON, 100);
        this.makeRecipe(SulfurRegistry.NICKEL.get(), ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON, 100);
        this.makeRecipe(SulfurRegistry.CINNABAR.get(), ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON, 100);
        this.makeRecipe(SulfurRegistry.OSMIUM.get(), ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON, 100);
    }

    public void makeRecipe(Item result, TagKey<Item> source, int mercuryFlux) {
        this.makeRecipe(this.name(result) + "_from_" + this.name(source), result, source, 1, mercuryFlux, TIME);
    }

    public void makeRecipe(String recipeName, Item result, TagKey<Item> source, int mercuryFlux) {
        this.makeRecipe(recipeName, result, source, 1, mercuryFlux, TIME);
    }

    public void makeRecipe(String recipeName, Item result, TagKey<Item> source, int mercuryFlux, int reformationTime) {
        this.makeRecipe(recipeName, result, source, 1, mercuryFlux, reformationTime);
    }

    public void makeRecipe(String recipeName, Item result, TagKey<Item> source, int sourceCount, int mercuryFlux, int reformationTime) {

        var recipe = this.makeRecipeJson(
                List.of(this.makeTagIngredient(this.locFor(source), sourceCount)),
                this.makeItemIngredient(this.locFor(result)),
                mercuryFlux,
                this.makeItemStackCodecResult(this.locFor(result)),
                reformationTime);

        var conditions = new JsonArray();
        conditions.add(this.makeTagNotEmptyCondition(source.location().toString()));
        recipe.add("conditions", conditions);

        this.recipeConsumer.accept(
                this.modLoc(recipeName),
                recipe
        );
    }

    public void makeRecipe(Item result, Item source, int mercuryFlux) {
        this.makeRecipe(this.name(result) + "_from_" + this.name(source), result, source, 1, mercuryFlux, TIME);
    }

    public void makeRecipe(String recipeName, Item result, Item source, int mercuryFlux) {
        this.makeRecipe(recipeName, result, source, 1, mercuryFlux, TIME);
    }

    public void makeRecipe(String recipeName, Item result, Item source, int mercuryFlux, int reformationTime) {
        this.makeRecipe(recipeName, result, source, 1, mercuryFlux, reformationTime);
    }

    public void makeRecipe(String recipeName, Item result, Item source, int sourceCount, int mercuryFlux, int reformationTime) {
        this.recipeConsumer.accept(
                this.modLoc(recipeName),
                this.makeRecipeJson(
                        List.of(this.makeItemIngredient(this.locFor(source), sourceCount)),
                        this.makeItemIngredient(this.locFor(result)),
                        mercuryFlux,
                        this.makeItemStackCodecResult(this.locFor(result)),
                        reformationTime));
    }

    public JsonObject makeRecipeJson(List<JsonObject> sources, JsonObject target, int mercuryFlux, JsonObject result, int reformationTime) {
        var sourcesArray = new JsonArray();
        for (var source : sources) {
            sourcesArray.add(source);
        }

        var recipe = new JsonObject();
        recipe.addProperty("type", RecipeTypeRegistry.REFORMATION.getId().toString());
        recipe.add("sources", sourcesArray);
        recipe.add("target", target);
        recipe.addProperty("mercury_flux", mercuryFlux);
        recipe.add("result", result);
        recipe.addProperty("reformation_time", reformationTime);
        return recipe;
    }

    @Override
    public String getName() {
        return "Reformation Recipes";
    }
}
