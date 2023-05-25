/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.BiConsumer;

public class CalcinationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = CalcinationRecipe.DEFAULT_CALCINATION_TIME;

    public CalcinationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "calcination");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_stone", Tags.Items.STONE);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_sandstone", Tags.Items.SANDSTONE);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_cobblestone", Tags.Items.COBBLESTONE);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_dirt", ItemTags.DIRT);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_sand", ItemTags.SAND);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_gravel", 1, Items.GRAVEL, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_clay", 4, Items.CLAY, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_clay_ball", 1, Items.CLAY_BALL, 1, TIME);
        this.makeRecipe(SaltRegistry.MINERAL.get(), "from_ores", Tags.Items.ORES);
        this.makeRecipe(SaltRegistry.MINERAL.get(), "from_raw_materials", Tags.Items.RAW_MATERIALS);
        this.makeRecipe(SaltRegistry.MINERAL.get(), "from_ingots", 2, Tags.Items.INGOTS);
        this.makeRecipe(SaltRegistry.MINERAL.get(), "from_gems", 2, Tags.Items.GEMS);
        this.makeRecipe(SaltRegistry.CROPS.get(), "", Tags.Items.CROPS);
        this.makeRecipe(SaltRegistry.MINERAL.get(), "", 1, SaltRegistry.STRATA.get(), 20, TIME);
    }


    public void makeRecipe(Item salt, String suffix, TagKey<Item> ingredient) {
        this.makeRecipe(salt, suffix, ingredient, TIME);
    }

    public void makeRecipe(Item salt, String suffix, TagKey<Item> ingredient, int calcinationTime) {
        this.makeRecipe(salt, suffix, 1, ingredient, 1, calcinationTime);
    }

    public void makeRecipe(Item salt, String suffix, int resultCount, TagKey<Item> ingredient) {
        this.makeRecipe(salt, suffix, resultCount, ingredient, 1);
    }


    public void makeRecipe(Item salt, String suffix, int resultCount, TagKey<Item> ingredient, int ingredientCount) {
        this.makeRecipe(salt, suffix, resultCount, ingredient, ingredientCount, TIME);
    }

    public void makeRecipe(Item salt, String suffix, int resultCount, Item ingredient, int ingredientCount, int calcinationTime) {
        var name = this.name(salt).replace("alchemical_salt_", "");
        if (suffix != null && !suffix.isEmpty())
            name += "_" + suffix;

        var recipe = this.makeRecipeJson(
                this.makeItemIngredient(this.locFor(ingredient)), ingredientCount,
                this.makeItemResult(this.locFor(salt), resultCount), calcinationTime);

        this.recipeConsumer.accept(
                this.modLoc(name),
                recipe
        );

    }

    public void makeRecipe(Item salt, String suffix, int resultCount, TagKey<Item> ingredient, int ingredientCount, int calcinationTime) {
        var name = this.name(salt).replace("alchemical_salt_", "");
        if (suffix != null && !suffix.isEmpty())
            name += "_" + suffix;

        var recipe = this.makeRecipeJson(
                this.makeTagIngredient(this.locFor(ingredient)), ingredientCount,
                this.makeItemResult(this.locFor(salt), resultCount), calcinationTime);

        var conditions = new JsonArray();
        conditions.add(this.makeTagNotEmptyCondition(ingredient.location().toString()));
        recipe.add("conditions", conditions);

        this.recipeConsumer.accept(
                this.modLoc(name),
                recipe
        );

    }

    public JsonObject makeRecipeJson(JsonObject ingredient, int ingredientCount, JsonObject result, int calcinationTime) {
        var recipe = new JsonObject();
        recipe.addProperty("type", RecipeTypeRegistry.CALCINATION.getId().toString());
        recipe.add("ingredient", ingredient);
        recipe.addProperty("ingredient_count", ingredientCount);
        recipe.add("result", result);
        recipe.addProperty("calcination_time", calcinationTime);
        return recipe;
    }

    @Override
    public String getName() {
        return "Calcination Recipes";
    }
}
