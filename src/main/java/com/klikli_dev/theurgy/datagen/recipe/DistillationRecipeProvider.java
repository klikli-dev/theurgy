// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.function.BiConsumer;

public class DistillationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = DistillationRecipe.DEFAULT_TIME;

    public DistillationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "distillation");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeMercuryShardRecipe(1, Tags.Items.STONE, 10);
        this.makeMercuryShardRecipe(1, Tags.Items.SANDSTONE, 10);
        this.makeMercuryShardRecipe(1, ItemTags.STONE_BRICKS, 10);
        this.makeMercuryShardRecipe(1, Tags.Items.COBBLESTONE, 10);
        this.makeMercuryShardRecipe(1, Tags.Items.GRAVEL, 15);
        this.makeMercuryShardRecipe(1, ItemTags.DIRT, 25);
        this.makeMercuryShardRecipe(1, Tags.Items.SAND, 15);
        this.makeMercuryShardRecipe(1, Tags.Items.GLASS, 10);

        this.makeMercuryShardRecipe(5, ItemTagRegistry.LOW_MERCURY_ORES, 1);
        this.makeMercuryShardRecipe(25, ItemTagRegistry.MEDIUM_MERCURY_ORES, 1);
        this.makeMercuryShardRecipe(50, ItemTagRegistry.HIGH_MERCURY_ORES, 1);

        this.makeMercuryShardRecipe(1, ItemTagRegistry.LOW_MERCURY_RAW_MATERIALS, 1);
        this.makeMercuryShardRecipe(5, ItemTagRegistry.MEDIUM_MERCURY_RAW_MATERIALS, 1);
        this.makeMercuryShardRecipe(10, ItemTagRegistry.HIGH_MERCURY_RAW_MATERIALS, 1);

        this.makeMercuryShardRecipe(1, ItemTagRegistry.LOW_MERCURY_METALS, 1);
        this.makeMercuryShardRecipe(5, ItemTagRegistry.MEDIUM_MERCURY_METALS, 1);
        this.makeMercuryShardRecipe(10, ItemTagRegistry.HIGH_MERCURY_METALS, 1);

        this.makeMercuryShardRecipe(1, ItemTagRegistry.LOW_MERCURY_GEMS, 1);
        this.makeMercuryShardRecipe(5, ItemTagRegistry.MEDIUM_MERCURY_GEMS, 1);
        this.makeMercuryShardRecipe(10, ItemTagRegistry.HIGH_MERCURY_GEMS, 1);

        this.makeMercuryShardRecipe(1, ItemTagRegistry.LOW_MERCURY_OTHER_MINERALS, 1);
        this.makeMercuryShardRecipe(5, ItemTagRegistry.MEDIUM_MERCURY_OTHER_MINERALS, 1);
        this.makeMercuryShardRecipe(10, ItemTagRegistry.HIGH_MERCURY_OTHER_MINERALS, 1);

        this.makeMercuryShardRecipe(1, Tags.Items.CROPS, 3);
        this.makeMercuryShardRecipe(1, Tags.Items.DYES, 3);
        this.makeMercuryShardRecipe(1, Items.PORKCHOP, 2);
        this.makeMercuryShardRecipe(1, Items.COOKED_PORKCHOP, 1);
        this.makeMercuryShardRecipe(1, Items.BEEF, 2);
        this.makeMercuryShardRecipe(1, Items.COOKED_BEEF, 1);
        this.makeMercuryShardRecipe(1, Items.CHICKEN, 2);
        this.makeMercuryShardRecipe(1, Items.COOKED_CHICKEN, 1);
        this.makeMercuryShardRecipe(1, Items.COD, 2);
        this.makeMercuryShardRecipe(1, Items.COOKED_COD, 1);
        this.makeMercuryShardRecipe(1, Items.SALMON, 2);
        this.makeMercuryShardRecipe(1, Items.COOKED_SALMON, 1);
        this.makeMercuryShardRecipe(1, Items.RABBIT, 2);
        this.makeMercuryShardRecipe(1, Items.COOKED_RABBIT, 1);
        this.makeMercuryShardRecipe(1, Items.BREAD, 1);
        this.makeMercuryShardRecipe(1, ItemTags.FLOWERS, 3);
        this.makeMercuryShardRecipe(1, ItemTags.SAPLINGS, 3);
        this.makeMercuryShardRecipe(1, ItemTags.LEAVES, 5);
        this.makeMercuryShardRecipe(1, ItemTags.LOGS, 2);
        this.makeMercuryShardRecipe(1, ItemTags.PLANKS, 8);
        this.makeMercuryShardRecipe(1, ItemTags.WOOL, 1);

    }

    public void makeMercuryShardRecipe(int resultCount, TagKey<Item> ingredient, int ingredientCount) {
        this.makeMercuryShardRecipe(resultCount, ingredient, ingredientCount, TIME);
    }

    public void makeMercuryShardRecipe(int resultCount, TagKey<Item> ingredient, int ingredientCount, int distillationTime) {
        this.makeMercuryShardRecipe(ingredient.location().getPath().replace("/", "."), resultCount, ingredient, ingredientCount, distillationTime);
    }

    public void makeMercuryShardRecipe(String recipeName, int resultCount, TagKey<Item> ingredient, int ingredientCount, int distillationTime) {

        var recipe = this.makeRecipeJson(
                this.makeTagIngredient(ingredient.location()),
                ingredientCount,
                this.makeItemResult(this.locFor(ItemRegistry.MERCURY_SHARD.get()), resultCount), distillationTime);

        var conditions = new JsonArray();
        conditions.add(this.makeTagNotEmptyCondition(ingredient.location().toString()));
        recipe.add("neoforge:conditions", conditions);

        this.recipeConsumer.accept(
                this.modLoc(recipeName),
                recipe
        );

    }

    public void makeMercuryShardRecipe(int resultCount, Item ingredient, int ingredientCount) {
        this.makeMercuryShardRecipe(resultCount, ingredient, ingredientCount, TIME);
    }

    public void makeMercuryShardRecipe(int resultCount, Item ingredient, int ingredientCount, int distillationTime) {
        this.makeMercuryShardRecipe(BuiltInRegistries.ITEM.getKey(ingredient).getPath(), resultCount, ingredient, ingredientCount, distillationTime);
    }

    public void makeMercuryShardRecipe(String recipeName, int resultCount, Item ingredient, int ingredientCount, int distillationTime) {
        this.recipeConsumer.accept(
                this.modLoc(recipeName),
                this.makeRecipeJson(
                        this.makeItemIngredient(this.locFor(ingredient)),
                        ingredientCount,
                        this.makeItemResult(this.locFor(ItemRegistry.MERCURY_SHARD.get()), resultCount), distillationTime));

    }

    public JsonObject makeRecipeJson(JsonObject ingredient, int ingredientCount, JsonObject result, int distillationTime) {
        var recipe = new JsonObject();
        recipe.addProperty("type", RecipeTypeRegistry.DISTILLATION.getId().toString());
        recipe.add("ingredient", ingredient);
        recipe.addProperty("ingredientCount", ingredientCount);
        recipe.add("result", result);
        recipe.addProperty("time", distillationTime);
        return recipe;
    }

    @Override
    public String getName() {
        return "Distillation Recipes";
    }
}
