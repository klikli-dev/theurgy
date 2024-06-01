// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.salt.AlchemicalSaltItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.registry.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.function.BiConsumer;

public class IncubationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = IncubationRecipe.DEFAULT_TIME;

    public IncubationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "incubation");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe(Items.WHEAT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CROPS.get(), SulfurRegistry.WHEAT.get());

        //metal ingots from sulfurs
        this.makeRecipe(Tags.Items.INGOTS_IRON, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.IRON.get());
        this.makeRecipe(Tags.Items.INGOTS_COPPER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.COPPER.get());
        this.makeRecipe(Tags.Items.INGOTS_GOLD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.GOLD.get());
        this.makeRecipe(Tags.Items.INGOTS_NETHERITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.NETHERITE.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_URANIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.URANIUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_SILVER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.SILVER.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_AZURE_SILVER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.AZURE_SILVER.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_ZINC, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.ZINC.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_OSMIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.OSMIUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_NICKEL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.NICKEL.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_LEAD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.LEAD.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_ALLTHEMODIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.ALLTHEMODIUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_UNOBTAINIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.UNOBTAINIUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_IRIDIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.IRIDIUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_TIN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.TIN.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_CINNABAR, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.CINNABAR.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_CRIMSON_IRON, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.CRIMSON_IRON.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_PLATINUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.PLATINUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_VIBRANIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.VIBRANIUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_DESH, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.DESH.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_OSTRUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.OSTRUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_CALORITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.CALORITE.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_IESNIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.IESNIUM.get());

        //gems from sulfurs
        this.makeRecipe(Tags.Items.GEMS_DIAMOND, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.DIAMOND.get());
        this.makeRecipe(Tags.Items.GEMS_EMERALD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.EMERALD.get());
        this.makeRecipe(Tags.Items.GEMS_LAPIS, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.LAPIS.get());
        this.makeRecipe(Tags.Items.GEMS_QUARTZ, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.QUARTZ.get());
        this.makeRecipe(Tags.Items.GEMS_AMETHYST, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.AMETHYST.get());
        this.makeRecipe(Tags.Items.GEMS_PRISMARINE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.PRISMARINE.get());
        this.makeRecipe(ItemTagRegistry.GEMS_RUBY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.RUBY.get());
        this.makeRecipe(ItemTagRegistry.GEMS_APATITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.APATITE.get());
        this.makeRecipe(ItemTagRegistry.GEMS_PERIDOT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.PERIDOT.get());
        this.makeRecipe(ItemTagRegistry.GEMS_FLUORITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.FLUORITE.get());
        this.makeRecipe(ItemTagRegistry.GEMS_SAPPHIRE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.SAPPHIRE.get());
        this.makeRecipe(ItemTagRegistry.GEMS_SAL_AMMONIAC, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.SAL_AMMONIAC.get());
        this.makeRecipe(ItemTagRegistry.GEMS_CERTUS_QUARTZ, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.CERTUS_QUARTZ.get());
        this.makeRecipe(ItemTagRegistry.GEMS_FLUIX, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.FLUIX.get());
        this.makeRecipe(ItemTagRegistry.GEMS_NITER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.NITER.get());

        //other common minerals from sulfur
        this.makeRecipe(Tags.Items.DUSTS_REDSTONE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.REDSTONE.get());
        this.makeRecipe(ItemTags.COALS, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.COAL.get());
        this.makeRecipe(this.tag("forge:gems/sulfur"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.SULFUR.get());
    }

    public void makeRecipe(TagKey<Item> result, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur) {
        this.makeRecipe(result, 1, mercury, salt, sulfur, TIME);
    }

    public void makeRecipe(TagKey<Item> result, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        this.makeRecipe(result, 1, mercury, salt, sulfur, incubationTime);
    }

    public void makeRecipe(TagKey<Item> result, int resultCount, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        this.makeRecipe(this.name(result) + "_from_" + this.name(sulfur), result, resultCount, mercury, salt, sulfur, incubationTime);
    }

    public void makeRecipe(String recipeName, TagKey<Item> result, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        this.makeRecipe(recipeName, result, 1, mercury, salt, sulfur, incubationTime);
    }

    public void makeRecipe(String recipeName, TagKey<Item> result, int resultCount, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {

        var recipe = this.makeRecipeJson(
                this.makeItemIngredient(this.locFor(mercury)),
                this.makeItemIngredient(this.locFor(salt)),
                this.makeItemIngredient(this.locFor(sulfur)),
                this.makeTagResult(this.locFor(result), resultCount), incubationTime);

        var conditions = new JsonArray();
        conditions.add(this.makeTagNotEmptyCondition(result.location().toString()));
        recipe.add("neoforge:conditions", conditions);

        this.recipeConsumer.accept(
                this.modLoc(recipeName),
                recipe
        );
    }

    public void makeRecipe(Item result, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur) {
        this.makeRecipe(result, 1, mercury, salt, sulfur, TIME);
    }

    public void makeRecipe(Item result, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        this.makeRecipe(result, 1, mercury, salt, sulfur, incubationTime);
    }

    public void makeRecipe(Item result, int resultCount, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        this.makeRecipe(this.name(result), result, resultCount, mercury, salt, sulfur, incubationTime);
    }

    public void makeRecipe(String recipeName, Item result, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        this.makeRecipe(recipeName, result, 1, mercury, salt, sulfur, incubationTime);
    }

    public void makeRecipe(String recipeName, Item result, int resultCount, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        this.recipeConsumer.accept(
                this.modLoc(recipeName),
                this.makeRecipeJson(
                        this.makeItemIngredient(this.locFor(mercury)),
                        this.makeItemIngredient(this.locFor(salt)),
                        this.makeItemIngredient(this.locFor(sulfur)),
                        this.makeItemResult(this.locFor(result), resultCount), incubationTime));

    }

    public JsonObject makeRecipeJson(JsonObject mercury, JsonObject salt, JsonObject sulfur, JsonObject result, int incubationTime) {
        var recipe = new JsonObject();
        recipe.addProperty("type", RecipeTypeRegistry.INCUBATION.getId().toString());
        recipe.add("mercury", mercury);
        recipe.add("salt", salt);
        recipe.add("sulfur", sulfur);
        recipe.add("result", result);
        recipe.addProperty("time", incubationTime);
        return recipe;
    }

    @Override
    public String getName() {
        return "Incubation Recipes";
    }
}
