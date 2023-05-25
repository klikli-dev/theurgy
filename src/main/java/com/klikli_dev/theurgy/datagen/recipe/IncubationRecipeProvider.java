/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.AlchemicalSaltItem;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.BiConsumer;

public class IncubationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = IncubationRecipe.DEFAULT_INCUBATION_TIME;

    public IncubationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "incubation");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe(Items.WHEAT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CROPS.get(), SulfurRegistry.WHEAT.get());

        //metal ingots from sulfurs
        this.makeRecipe(Tags.Items.INGOTS_IRON, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.IRON.get());
        this.makeRecipe(Tags.Items.INGOTS_COPPER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.COPPER.get());
        this.makeRecipe(Tags.Items.INGOTS_GOLD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.GOLD.get());
        this.makeRecipe(Tags.Items.INGOTS_NETHERITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.NETHERITE.get());
        this.makeRecipe(this.tag("forge:ingots/uranium"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.URANIUM.get());
        this.makeRecipe(this.tag("forge:ingots/silver"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.SILVER.get());
        this.makeRecipe(this.tag("forge:ingots/azure_silver"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.AZURE_SILVER.get());
        this.makeRecipe(this.tag("forge:ingots/zinc"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.ZINC.get());
        this.makeRecipe(this.tag("forge:ingots/osmium"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.OSMIUM.get());
        this.makeRecipe(this.tag("forge:ingots/nickel"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.NICKEL.get());
        this.makeRecipe(this.tag("forge:ingots/lead"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.LEAD.get());
        this.makeRecipe(this.tag("forge:ingots/allthemodium"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.ALLTHEMODIUM.get());
        this.makeRecipe(this.tag("forge:ingots/unobtainium"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.UNOBTAINIUM.get());
        this.makeRecipe(this.tag("forge:ingots/iridium"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.IRIDIUM.get());
        this.makeRecipe(this.tag("forge:ingots/tin"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.TIN.get());
        this.makeRecipe(this.tag("forge:ingots/cinnabar"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.CINNABAR.get());
        this.makeRecipe(this.tag("forge:ingots/crimson_iron"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.CRIMSON_IRON.get());
        this.makeRecipe(this.tag("forge:ingots/platinum"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.PLATINUM.get());
        this.makeRecipe(this.tag("forge:ingots/vibranium"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.VIBRANIUM.get());

        //gems from sulfurs
        this.makeRecipe(this.tag("forge:gems/diamond"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.DIAMOND.get());
        this.makeRecipe(this.tag("forge:gems/emerald"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.EMERALD.get());
        this.makeRecipe(this.tag("forge:gems/lapis"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.LAPIS.get());
        this.makeRecipe(this.tag("forge:gems/quartz"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.QUARTZ.get());
        this.makeRecipe(this.tag("forge:gems/ruby"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.RUBY.get());
        this.makeRecipe(this.tag("forge:gems/apatite"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.APATITE.get());
        this.makeRecipe(this.tag("forge:gems/peridot"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.PERIDOT.get());
        this.makeRecipe(this.tag("forge:gems/fluorite"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.FLUORITE.get());
        this.makeRecipe(this.tag("forge:gems/sapphire"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.SAPPHIRE.get());

        //other common minerals from sulfur
        this.makeRecipe(this.tag("forge:gems/redstone"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.REDSTONE.get());
        this.makeRecipe(this.tag("forge:gems/coal"), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.COAL.get());
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
        recipe.add("conditions", conditions);

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
        recipe.addProperty("incubation_time", incubationTime);
        return recipe;
    }

    @Override
    public String getName() {
        return "Incubation Recipes";
    }
}
