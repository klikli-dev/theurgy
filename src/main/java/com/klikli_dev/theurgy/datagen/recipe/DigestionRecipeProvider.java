// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
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

public class DigestionRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = FermentationRecipe.DEFAULT_TIME;

    public DigestionRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "digestion");
    }


    public void makeTierConversion(AlchemicalSulfurItem lower, AlchemicalSulfurItem higher, int conversionFactor, int salAmmoniacAmount) {
        this.makeRecipe(FluidRegistry.SAL_AMMONIAC.get(), salAmmoniacAmount, List.of(
                Pair.of(lower, conversionFactor),
                Pair.of(ItemRegistry.PURIFIED_GOLD.get(), 1)
        ), higher, 1, TIME * 5, "_from_" + lower.tier().name().toLowerCase());

        this.makeRecipe(FluidRegistry.SAL_AMMONIAC.get(), salAmmoniacAmount, List.of(
                Pair.of(higher, 1)
        ), lower, conversionFactor, TIME * 5, "_from_" + higher.tier().name().toLowerCase());
    }
    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipeWithTags(Fluids.WATER, 1000, List.of(
                Tags.Items.INGOTS_GOLD,
                ItemTagRegistry.ALCHEMICAL_SALTS
        ), ItemRegistry.PURIFIED_GOLD.get(), 20, TIME * 5);

        this.makeTierConversion(SulfurRegistry.GEMS_ABUNDANT.get(), SulfurRegistry.GEMS_COMMON.get(), 4, 10);
        this.makeTierConversion(SulfurRegistry.GEMS_COMMON.get(), SulfurRegistry.GEMS_RARE.get(), 4, 15);
        this.makeTierConversion(SulfurRegistry.GEMS_RARE.get(), SulfurRegistry.GEMS_PRECIOUS.get(), 4, 50);

        this.makeTierConversion(SulfurRegistry.METALS_ABUNDANT.get(), SulfurRegistry.METALS_COMMON.get(), 4, 10);
        this.makeTierConversion(SulfurRegistry.METALS_COMMON.get(), SulfurRegistry.METALS_RARE.get(), 4, 15);
        this.makeTierConversion(SulfurRegistry.METALS_RARE.get(), SulfurRegistry.METALS_PRECIOUS.get(), 4, 50);

        this.makeTierConversion(SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), SulfurRegistry.OTHER_MINERALS_COMMON.get(), 4, 10);
        this.makeTierConversion(SulfurRegistry.OTHER_MINERALS_COMMON.get(), SulfurRegistry.OTHER_MINERALS_RARE.get(), 4, 15);
        this.makeTierConversion(SulfurRegistry.OTHER_MINERALS_RARE.get(), SulfurRegistry.OTHER_MINERALS_PRECIOUS.get(), 4, 50);
    }

    public void makeRecipe(Fluid fluid, int fluidAmount, List<Pair<Item, Integer>> ingredients, Item result, int resultCount, int time, String postFix) {
        this.makeRecipe(this.name(result) + postFix, fluid, fluidAmount, ingredients, result, resultCount, time);
    }

    public void makeRecipe(Fluid fluid, int fluidAmount, List<Pair<Item, Integer>> ingredients, Item result, int resultCount, int time) {
        this.makeRecipe(this.name(result), fluid, fluidAmount, ingredients, result, resultCount, time);
    }

    public void makeRecipe(String name, Fluid fluid, int fluidAmount, List<Pair<Item, Integer>> ingredients, Item result, int resultCount, int time) {
        var recipe = this.makeRecipeJson(
                this.makeFluidTagIngredient(this.locFor(fluid)),
                fluidAmount,
                ingredients.stream().map(i -> this.makeItemIngredient(this.locFor(i.getFirst()), i.getSecond())).toList(),
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
