// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import com.klikli_dev.theurgy.registry.*;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public class FermentationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = FermentationRecipe.DEFAULT_TIME;

    public FermentationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "fermentation");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeFermentationStarterRecipeForTag(Tags.Items.CROPS);
        this.makeFermentationStarterRecipeForTag(Tags.Items.SEEDS);
        this.makeFermentationStarterRecipeForTag(ItemTags.SAPLINGS);
        this.makeFermentationStarterRecipeForTag(ItemTags.FLOWERS);
        this.makeFermentationStarterRecipeForTag(Tags.Items.EGGS);
        this.makeFermentationStarterRecipeForTag(ItemTags.LOGS);
        this.makeFermentationStarterRecipeForTag(ItemTags.PLANKS);

        this.makeRecipesForCropTag(ItemTagRegistry.FERMENTATION_STARTERS);
        this.makeRecipesForCropTag(ItemTagRegistry.SUGARS);
        this.makeRecipesForCropTag(Tags.Items.CROPS);
        this.makeRecipesForCropTag(Tags.Items.SEEDS);
        this.makeRecipesForCropTag(ItemTags.SAPLINGS);
        this.makeRecipesForCropTag(ItemTags.FLOWERS);
        this.makeRecipesForCropTag(Tags.Items.EGGS);
        this.makeRecipesForCropTag(ItemTags.LOGS);
        this.makeRecipesForCropTag(ItemTags.PLANKS);
    }

    public void makeFermentationStarterRecipeForTag(TagKey<Item> cropTag) {
        this.makeRecipe(FluidRegistry.SAL_AMMONIAC.get(), 100, List.of(
                ItemTagRegistry.SUGARS,
                cropTag
        ), ItemRegistry.FERMENTATION_STARTER.get(), 20, TIME, "_from_" + this.name(cropTag));
    }
    
    public void makeRecipesForCropTag(TagKey<Item> cropTag) {
        this.makeRecipe(Fluids.WATER, 125, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_ABUNDANT,
                cropTag
        ), SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 250, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_COMMON,
                cropTag
        ), SulfurRegistry.EARTHEN_MATTERS_COMMON.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 125, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT,
                cropTag
        ), SulfurRegistry.GEMS_ABUNDANT.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 250, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON,
                cropTag
        ), SulfurRegistry.GEMS_COMMON.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 500, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE,
                cropTag
        ), SulfurRegistry.GEMS_RARE.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 1000, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS,
                cropTag
        ), SulfurRegistry.GEMS_PRECIOUS.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 125, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT,
                cropTag
        ), SulfurRegistry.METALS_ABUNDANT.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 250, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON,
                cropTag
        ), SulfurRegistry.METALS_COMMON.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 500, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE,
                cropTag
        ), SulfurRegistry.METALS_RARE.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 1000, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS,
                cropTag
        ), SulfurRegistry.METALS_PRECIOUS.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 125, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT,
                cropTag
        ), SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 250, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON,
                cropTag
        ), SulfurRegistry.OTHER_MINERALS_COMMON.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 500, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE,
                cropTag
        ), SulfurRegistry.OTHER_MINERALS_RARE.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 1000, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS,
                cropTag
        ), SulfurRegistry.OTHER_MINERALS_PRECIOUS.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 125, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_ABUNDANT,
                cropTag
        ), SulfurRegistry.LOGS_ABUNDANT.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 125, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_ABUNDANT,
                cropTag
        ), SulfurRegistry.CROPS_ABUNDANT.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 125, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_ABUNDANT,
                cropTag
        ), SulfurRegistry.ANIMALS_ABUNDANT.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 250, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_COMMON,
                cropTag
        ), SulfurRegistry.ANIMALS_COMMON.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 500, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_RARE,
                cropTag
        ), SulfurRegistry.ANIMALS_RARE.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 125, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_ABUNDANT,
                cropTag
        ), SulfurRegistry.MOBS_ABUNDANT.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 250, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_COMMON_FOR_AUTOMATIC_RECIPES,
                cropTag
        ), SulfurRegistry.MOBS_COMMON.get(), 1, TIME, "_using_" + this.name(cropTag));
        this.makeRecipe(Fluids.WATER, 250,
                SulfurRegistry.SKELETON_SKULL.get(),
                cropTag,
        SulfurRegistry.MOBS_COMMON.get(), 2, TIME, "_from_" + this.name(SulfurRegistry.SKELETON_SKULL.get()) + "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 500, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_RARE_FOR_AUTOMATIC_RECIPES,
                cropTag
        ), SulfurRegistry.MOBS_RARE.get(), 1, TIME, "_using_" + this.name(cropTag));
        this.makeRecipe(Fluids.WATER, 500,
                SulfurRegistry.WITHER_SKELETON_SKULL.get(),
                cropTag,
                SulfurRegistry.MOBS_RARE.get(), 2, TIME, "_from_" + this.name(SulfurRegistry.WITHER_SKELETON_SKULL.get()) + "_using_" + this.name(cropTag));
        this.makeRecipe(Fluids.WATER, 500,
                SulfurRegistry.GHAST_TEAR.get(),
                cropTag,
                SulfurRegistry.MOBS_RARE.get(), 2, TIME, "_from_" + this.name(SulfurRegistry.GHAST_TEAR.get()) + "_using_" + this.name(cropTag));
        this.makeRecipe(Fluids.WATER, 500,
                SulfurRegistry.SHULKER_SHELL.get(),
                cropTag,
                SulfurRegistry.MOBS_RARE.get(), 2, TIME, "_from_" + this.name(SulfurRegistry.SHULKER_SHELL.get()) + "_using_" + this.name(cropTag));
        this.makeRecipe(Fluids.WATER, 500,
                SulfurRegistry.ELYTRA.get(),
                cropTag,
                SulfurRegistry.MOBS_RARE.get(), 2, TIME, "_from_" + this.name(SulfurRegistry.ELYTRA.get()) + "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 1000, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_PRECIOUS_FOR_AUTOMATIC_RECIPES,
                cropTag
        ), SulfurRegistry.MOBS_PRECIOUS.get(), 1, TIME, "_using_" + this.name(cropTag));
        this.makeRecipe(Fluids.WATER, 1000,
                SulfurRegistry.NETHER_STAR.get(),
                cropTag,
                SulfurRegistry.MOBS_PRECIOUS.get(), 2, TIME, "_from_" + this.name(SulfurRegistry.NETHER_STAR.get()) + "_using_" + this.name(cropTag));
        this.makeRecipe(Fluids.WATER, 1000,
                SulfurRegistry.HEART_OF_THE_SEA.get(),
                cropTag,
                SulfurRegistry.MOBS_PRECIOUS.get(), 2, TIME, "_from_" + this.name(SulfurRegistry.HEART_OF_THE_SEA.get()) + "_using_" + this.name(cropTag));
        this.makeRecipe(Fluids.WATER, 1000,
                SulfurRegistry.DRAGON_EGG.get(),
                cropTag,
                SulfurRegistry.MOBS_PRECIOUS.get(), 2, TIME, "_from_" + this.name(SulfurRegistry.DRAGON_EGG.get()) + "_using_" + this.name(cropTag));
    }

    public void makeRecipe(Fluid fluid, int fluidAmount, List<TagKey<Item>> ingredients, Item result, int resultCount, int time) {
        this.makeRecipe(this.name(result), fluid, fluidAmount, ingredients, result, resultCount, time);
    }

    public void makeRecipe(Fluid fluid, int fluidAmount, List<TagKey<Item>> ingredients, Item result, int resultCount, int time, String postFix) {
        this.makeRecipe(this.name(result) + postFix, fluid, fluidAmount, ingredients, result, resultCount, time);
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

    public void makeRecipe(Fluid fluid, int fluidAmount, Item itemIngredient, TagKey<Item> tagIngredient, Item result, int resultCount, int time, String postFix) {
        this.makeRecipe(this.name(result) + postFix, fluid, fluidAmount, itemIngredient, tagIngredient, result, resultCount, time);
    }

    public void makeRecipe(String name, Fluid fluid, int fluidAmount, Item itemIngredient, TagKey<Item> tagIngredient, Item result, int resultCount, int time) {
        var ingredients = List.of(
                this.makeItemIngredient(this.locFor(itemIngredient)),
                this.makeTagIngredient(this.locFor(tagIngredient))
        );

        var recipe = this.makeRecipeJson(
                this.makeFluidTagIngredient(this.locFor(fluid)),
                fluidAmount,
                ingredients,
                this.makeItemStackCodecResult(this.locFor(result), resultCount),
                time);

        var conditions = new JsonArray();
        conditions.add(this.makeTagNotEmptyCondition(tagIngredient.location().toString()));
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
