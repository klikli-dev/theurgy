// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.AlchemicalSaltItem;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class CalcinationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = CalcinationRecipe.DEFAULT_CALCINATION_TIME;

    public CalcinationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "calcination");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_stone", Tags.Items.STONE);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_sandstone", Tags.Items.SANDSTONE);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_cobblestone", Tags.Items.COBBLESTONE);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_dirt", ItemTags.DIRT);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_sand", ItemTags.SAND);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_gravel", 1, Items.GRAVEL, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_clay", 4, Items.CLAY, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_clay_ball", 1, Items.CLAY_BALL, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_netherrack", 1, Tags.Items.NETHERRACK, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_soul_sand", 1, Items.SOUL_SAND, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_soul_soil", 1, Items.SOUL_SOIL, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_blackstone", 1, Items.BLACKSTONE, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_terracotta", 2, Items.TERRACOTTA, 1, TIME);
        // CONCRETES tag did not exist in 1.20, workaround needed
        //this.makeRecipe(SaltRegistry.STRATA.get(), "from_concrete", 2, Tags.Items.CONCRETES, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_crimson_nylium", 2, Items.CRIMSON_NYLIUM, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_warped_nylium", 2, Items.WARPED_NYLIUM, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_end_stone", 2, Items.END_STONE, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_purpur_block", 2, Items.PURPUR_BLOCK, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_mycelium", 2, Items.MYCELIUM, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_obsidian", 2, Items.OBSIDIAN, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_crying_obsidian", 3, Items.CRYING_OBSIDIAN, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_snowball", 1, Items.SNOWBALL, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_ice", 1, Items.ICE, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_packed_ice", 9, Items.PACKED_ICE, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_blue_ice", 64, Items.BLUE_ICE, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_magma_block", 2, Items.MAGMA_BLOCK, 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_crystallized_water", 4, ItemRegistry.CRYSTALLIZED_WATER.get(), 1, TIME);
        this.makeRecipe(SaltRegistry.STRATA.get(), "from_crystallized_lava", 8, ItemRegistry.CRYSTALLIZED_LAVA.get(), 1, TIME);

        this.makeRecipe(SaltRegistry.MINERAL.get(), "from_ores", Tags.Items.ORES);
        this.makeRecipe(SaltRegistry.MINERAL.get(), "from_raw_materials", Tags.Items.RAW_MATERIALS);
        this.makeRecipe(SaltRegistry.MINERAL.get(), "from_ingots", 2, Tags.Items.INGOTS);
        this.makeRecipe(SaltRegistry.MINERAL.get(), "from_gems", 2, Tags.Items.GEMS);
        this.makeRecipe(SaltRegistry.MINERAL.get(), "from_other_minerals", 2, ItemTagRegistry.OTHER_MINERALS);
        this.makeRecipe(SaltRegistry.MINERAL.get(), "from_strata_salt", 1, SaltRegistry.STRATA.get(), 5, TIME);

        this.makeRecipe(SaltRegistry.PLANT.get(), "from_crops", Tags.Items.CROPS);
        this.makeRecipe(SaltRegistry.PLANT.get(), "from_logs", ItemTags.LOGS);
        this.makeRecipe(SaltRegistry.PLANT.get(), "from_leaves", ItemTags.LEAVES);
        this.makeRecipe(SaltRegistry.PLANT.get(), "from_saplings", ItemTags.SAPLINGS);

        this.makeRecipe(SaltRegistry.CREATURE.get(), "from_plant_salt", 1, SaltRegistry.PLANT.get(), 2, TIME);
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
