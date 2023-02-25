/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.registry.FluidRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;

import java.util.function.BiConsumer;

public class LiqueficationRecipeProvider extends JsonRecipeProvider {

    public LiqueficationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "liquefaction");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        var sulfurHelper = new JsonObject();
        sulfurHelper.addProperty(TheurgyConstants.Nbt.SULFUR_SOURCE_ID, this.locFor(Items.OAK_LOG).toString());

        var salAmmoniac = FluidRegistry.SAL_AMMONIAC.get();


        //Vanilla
        //Sulfurs with overrideTagSourceName
        this.makeRecipe("logs", ItemTags.LOGS, salAmmoniac, 10, recipeConsumer);

        //Crops
        this.makeRecipe("wheat", Items.WHEAT, salAmmoniac, 10, recipeConsumer);

        //Metals

        //Other Mods
    }

    public void makeRecipe(String sulfurName, Item ingredient, Fluid solvent, int solventAmount, BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe(sulfurName, 1, ingredient, solvent, solventAmount, LiquefactionRecipe.DEFAULT_LIQUEFACTION_TIME, recipeConsumer);
    }

    public void makeRecipe(String sulfurName, Item ingredient, Fluid solvent, int solventAmount, int liquefactionTime, BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe(sulfurName, 1, ingredient, solvent, solventAmount, liquefactionTime, recipeConsumer);
    }

    public void makeRecipe(String sulfurName, int resultAmount, Item ingredient, Fluid solvent, int solventAmount, BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe(sulfurName, resultAmount, ingredient, solvent, solventAmount, LiquefactionRecipe.DEFAULT_LIQUEFACTION_TIME, recipeConsumer);
    }

    public void makeRecipe(String sulfurName, int resultAmount, Item ingredient, Fluid solvent, int solventAmount, int liquefactionTime, BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {

        var nbt = this.makeSulfurNbt(ingredient);
        recipeConsumer.accept(
                this.modLoc(sulfurName),
                this.makeRecipeJson(
                        this.makeItemIngredient(this.locFor(ingredient)),
                        this.makeFluidIngredient(this.locFor(solvent), solventAmount),
                        this.makeResult(this.modLoc("alchemical_sulfur_" + sulfurName), resultAmount, nbt), liquefactionTime));

    }

    public void makeRecipe(String sulfurName, TagKey<Item> ingredient, Fluid solvent, int solventAmount, BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe(sulfurName, 1, ingredient, solvent, solventAmount, LiquefactionRecipe.DEFAULT_LIQUEFACTION_TIME, recipeConsumer);
    }

    public void makeRecipe(String sulfurName, TagKey<Item> ingredient, Fluid solvent, int solventAmount, int liquefactionTime, BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe(sulfurName, 1, ingredient, solvent, solventAmount, liquefactionTime, recipeConsumer);
    }

    public void makeRecipe(String sulfurName, int resultAmount, TagKey<Item> ingredient, Fluid solvent, int solventAmount, BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe(sulfurName, resultAmount, ingredient, solvent, solventAmount, LiquefactionRecipe.DEFAULT_LIQUEFACTION_TIME, recipeConsumer);
    }

    public void makeRecipe(String sulfurName, int resultAmount, TagKey<Item> ingredient, Fluid solvent, int solventAmount, int liquefactionTime, BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {

        var nbt = this.makeSulfurNbt(ingredient);
        recipeConsumer.accept(
                this.modLoc(sulfurName),
                this.makeRecipeJson(
                        this.makeTagIngredient(ingredient.location()),
                        this.makeFluidIngredient(this.locFor(solvent), solventAmount),
                        this.makeResult(this.modLoc("alchemical_sulfur_" + sulfurName), resultAmount, nbt), liquefactionTime));

    }

    public JsonObject makeSulfurNbt(Item ingredient) {
        var nbt = new JsonObject();
        nbt.addProperty(TheurgyConstants.Nbt.SULFUR_SOURCE_ID, this.locFor(ingredient).toString());
        return nbt;
    }

    public JsonObject makeSulfurNbt(TagKey<Item> ingredient) {
        var nbt = new JsonObject();
        nbt.addProperty(TheurgyConstants.Nbt.SULFUR_SOURCE_ID, "#" + ingredient.location());
        return nbt;
    }

    public JsonObject makeRecipeJson(JsonObject ingredient, JsonObject solvent, JsonObject result, int liquefactionTime) {
        var recipe = new JsonObject();
        recipe.addProperty("type", RecipeTypeRegistry.LIQUEFACTION.getId().toString());
        recipe.add("solvent", solvent);
        recipe.add("ingredient", ingredient);
        recipe.add("result", result);
        recipe.addProperty("liquefaction_time", liquefactionTime);
        return recipe;
    }

    @Override
    public String getName() {
        return "Liquefaction Recipes";
    }
}
