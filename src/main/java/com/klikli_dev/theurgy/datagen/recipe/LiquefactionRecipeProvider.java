// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.registry.FluidRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;

import java.util.function.BiConsumer;

public class LiquefactionRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = LiquefactionRecipe.DEFAULT_TIME;

    public LiquefactionRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "liquefaction");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        var salAmmoniac = FluidRegistry.SAL_AMMONIAC.get();

        //Vanilla
        //Sulfurs with overrideTagSourceName
        this.makeRecipe(SulfurRegistry.LOGS.get(), ItemTags.LOGS, salAmmoniac, 10);

        //Crops
        this.makeRecipe(SulfurRegistry.WHEAT.get(), Items.WHEAT, salAmmoniac, 10);

        //Common Metals Ore Sulfurs
        this.makeRecipe(SulfurRegistry.IRON.get(), 5, Tags.Items.ORES_IRON, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COPPER.get(), 5, Tags.Items.ORES_COPPER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GOLD.get(), 5, Tags.Items.ORES_GOLD, salAmmoniac, 15);
        //netherite has a custom recipe in vanilla, 4 scraps per ingot, so we only do 1 sulfur here to avoid insane duplication rates
        this.makeRecipe(SulfurRegistry.NETHERITE.get(), 1, Tags.Items.ORES_NETHERITE_SCRAP, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.URANIUM.get(), 5, this.tag("forge:ores/uranium"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SILVER.get(), 5, this.tag("forge:ores/silver"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.AZURE_SILVER.get(), 5, this.tag("forge:ores/azure_silver"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ZINC.get(), 5, this.tag("forge:ores/zinc"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OSMIUM.get(), 5, this.tag("forge:ores/osmium"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.NICKEL.get(), 5, this.tag("forge:ores/nickel"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.LEAD.get(), 5, this.tag("forge:ores/lead"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ALLTHEMODIUM.get(), 5, this.tag("forge:ores/allthemodium"), salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.UNOBTAINIUM.get(), 5, this.tag("forge:ores/unobtainium"), salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.IRIDIUM.get(), 5, this.tag("forge:ores/iridium"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TIN.get(), 5, this.tag("forge:ores/tin"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CINNABAR.get(), 5, this.tag("forge:ores/cinnabar"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CRIMSON_IRON.get(), 5, this.tag("forge:ores/crimson_iron"), salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.PLATINUM.get(), 5, this.tag("forge:ores/platinum"), salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.VIBRANIUM.get(), 5, this.tag("forge:ores/vibranium"), salAmmoniac, 20);

        //Common Gems Ore Sulfurs
        this.makeRecipe(SulfurRegistry.DIAMOND.get(), 4, Tags.Items.ORES_DIAMOND, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.EMERALD.get(), 4, Tags.Items.ORES_EMERALD, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.LAPIS.get(), 12, Tags.Items.ORES_LAPIS, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.QUARTZ.get(), 10, Tags.Items.ORES_QUARTZ, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.RUBY.get(), 8, this.tag("forge:ores/ruby"), salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.APATITE.get(), 9, this.tag("forge:ores/apatite"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PERIDOT.get(), 8, this.tag("forge:ores/peridot"), salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.FLUORITE.get(), 8, this.tag("forge:ores/fluorite"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SAPPHIRE.get(), 8, this.tag("forge:ores/sapphire"), salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.SAL_AMMONIAC.get(), 6, this.tag("forge:ores/sal_ammoniac"), salAmmoniac, 15);

        //Other Common Mineral Ores
        this.makeRecipe(SulfurRegistry.REDSTONE.get(), 9, Tags.Items.ORES_REDSTONE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COAL.get(), 4, Tags.Items.ORES_COAL, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SULFUR.get(), 6, this.tag("forge:ores/sulfur"), salAmmoniac, 10);

        //Common Raw Materials Sulfurs
        this.makeRecipe(SulfurRegistry.IRON.get(), 3, Tags.Items.RAW_MATERIALS_IRON, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COPPER.get(), 3, Tags.Items.RAW_MATERIALS_COPPER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GOLD.get(), 3, Tags.Items.RAW_MATERIALS_GOLD, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.URANIUM.get(), 3, this.tag("forge:raw_materials/uranium"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.AZURE_SILVER.get(), 3, this.tag("forge:raw_materials/azure_silver"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SILVER.get(), 3, this.tag("forge:raw_materials/silver"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ZINC.get(), 3, this.tag("forge:raw_materials/zinc"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OSMIUM.get(), 3, this.tag("forge:raw_materials/osmium"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.NICKEL.get(), 3, this.tag("forge:raw_materials/nickel"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.LEAD.get(), 3, this.tag("forge:raw_materials/lead"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ALLTHEMODIUM.get(), 3, this.tag("forge:raw_materials/allthemodium"), salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.UNOBTAINIUM.get(), 3, this.tag("forge:raw_materials/unobtainium"), salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.IRIDIUM.get(), 3, this.tag("forge:raw_materials/iridium"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TIN.get(), 3, this.tag("forge:raw_materials/tin"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CINNABAR.get(), 3, this.tag("forge:raw_materials/cinnabar"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CRIMSON_IRON.get(), 3, this.tag("forge:raw_materials/crimson_iron"), salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.PLATINUM.get(), 3, this.tag("forge:raw_materials/platinum"), salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.VIBRANIUM.get(), 3, this.tag("forge:raw_materials/vibranium"), salAmmoniac, 20);

        //Common Metal Ingots sulfurs
        this.makeRecipe(SulfurRegistry.IRON.get(), 1, Tags.Items.INGOTS_IRON, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COPPER.get(), 1, Tags.Items.INGOTS_COPPER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GOLD.get(), 1, Tags.Items.INGOTS_GOLD, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.NETHERITE.get(), 1, Tags.Items.INGOTS_NETHERITE, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.URANIUM.get(), 1, this.tag("forge:ingots/uranium"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SILVER.get(), 1, this.tag("forge:ingots/silver"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.AZURE_SILVER.get(), 1, this.tag("forge:ingots/azure_silver"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ZINC.get(), 1, this.tag("forge:ingots/zinc"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OSMIUM.get(), 1, this.tag("forge:ingots/osmium"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.NICKEL.get(), 1, this.tag("forge:ingots/nickel"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.LEAD.get(), 1, this.tag("forge:ingots/lead"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ALLTHEMODIUM.get(), 1, this.tag("forge:ingots/allthemodium"), salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.UNOBTAINIUM.get(), 1, this.tag("forge:ingots/unobtainium"), salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.IRIDIUM.get(), 1, this.tag("forge:ingots/iridium"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TIN.get(), 1, this.tag("forge:ingots/tin"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CINNABAR.get(), 1, this.tag("forge:ingots/cinnabar"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CRIMSON_IRON.get(), 1, this.tag("forge:ingots/crimson_iron"), salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.PLATINUM.get(), 1, this.tag("forge:ingots/platinum"), salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.VIBRANIUM.get(), 1, this.tag("forge:ingots/vibranium"), salAmmoniac, 20);

        //Common Gems Sulfurs
        this.makeRecipe(SulfurRegistry.DIAMOND.get(), 1, Tags.Items.GEMS_DIAMOND, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.EMERALD.get(), 1, Tags.Items.GEMS_EMERALD, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.LAPIS.get(), 1, Tags.Items.GEMS_LAPIS, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.QUARTZ.get(), 1, Tags.Items.GEMS_QUARTZ, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.AMETHYST.get(), 1, Tags.Items.GEMS_AMETHYST, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PRISMARINE.get(), 1, Tags.Items.GEMS_PRISMARINE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.RUBY.get(), 1, this.tag("forge:gems/ruby"), salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.APATITE.get(), 1, this.tag("forge:gems/apatite"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PERIDOT.get(), 1, this.tag("forge:gems/peridot"), salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.FLUORITE.get(), 1, this.tag("forge:gems/fluorite"), salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SAPPHIRE.get(), 1, this.tag("forge:gems/sapphire"), salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.SAL_AMMONIAC.get(), 1, this.tag("forge:gems/sal_ammoniac"), salAmmoniac, 15);

        //Other Common Minerals Sulfurs
        this.makeRecipe(SulfurRegistry.REDSTONE.get(), 1, Tags.Items.DUSTS_REDSTONE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COAL.get(), 1, ItemTags.COALS, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SULFUR.get(), 1, this.tag("forge:gems/sulfur"), salAmmoniac, 10);
    }


    public void makeRecipe(Item sulfurName, Item ingredient, Fluid solvent, int solventAmount) {
        this.makeRecipe(sulfurName, 1, ingredient, solvent, solventAmount, TIME);
    }

    public void makeRecipe(Item sulfur, Item ingredient, Fluid solvent, int solventAmount, int liquefactionTime) {
        this.makeRecipe(sulfur, 1, ingredient, solvent, solventAmount, liquefactionTime);
    }

    public void makeRecipe(Item sulfur, int resultCount, Item ingredient, Fluid solvent, int solventAmount) {
        this.makeRecipe(sulfur, resultCount, ingredient, solvent, solventAmount, TIME);
    }

    public void makeRecipe(Item sulfur, int resultCount, Item ingredient, Fluid solvent, int solventAmount, int liquefactionTime) {

        var nbt = this.makeSulfurNbt(ingredient);
        this.recipeConsumer.accept(
                this.modLoc(this.name(sulfur)),
                this.makeRecipeJson(
                        this.makeItemIngredient(this.locFor(ingredient)),
                        this.makeFluidIngredient(this.locFor(solvent)),
                        solventAmount,
                        this.makeItemResult(this.locFor(sulfur), resultCount, nbt), liquefactionTime));

    }

    public void makeRecipe(Item sulfurName, TagKey<Item> ingredient, Fluid solvent, int solventAmount) {
        this.makeRecipe(sulfurName, 1, ingredient, solvent, solventAmount, LiquefactionRecipe.DEFAULT_TIME);
    }

    public void makeRecipe(Item sulfurName, TagKey<Item> ingredient, Fluid solvent, int solventAmount, int liquefactionTime) {
        this.makeRecipe(sulfurName, 1, ingredient, solvent, solventAmount, liquefactionTime);
    }

    public void makeRecipe(Item sulfur, int resultCount, TagKey<Item> ingredient, Fluid solvent, int solventAmount) {
        this.makeRecipe(sulfur, resultCount, ingredient, solvent, solventAmount, LiquefactionRecipe.DEFAULT_TIME);
    }

    public void makeRecipe(Item sulfur, int resultCount, TagKey<Item> ingredient, Fluid solvent, int solventAmount, int liquefactionTime) {

        var name = this.name(sulfur) + "_from_" + this.name(ingredient);
        var nbt = this.makeSulfurNbt(ingredient);
        var recipe = this.makeRecipeJson(
                this.makeTagIngredient(ingredient.location()),
                this.makeFluidIngredient(this.locFor(solvent)),
                solventAmount,
                this.makeItemResult(this.locFor(sulfur), resultCount, nbt), liquefactionTime);

        var conditions = new JsonArray();
        conditions.add(this.makeTagNotEmptyCondition(ingredient.location().toString()));
        recipe.add("neoforge:conditions", conditions);

        this.recipeConsumer.accept(
                this.modLoc(name),
                recipe
        );

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

    public JsonObject makeRecipeJson(JsonObject ingredient, JsonObject solvent, int solventAmount, JsonObject result, int liquefactionTime) {
        var recipe = new JsonObject();
        recipe.addProperty("type", RecipeTypeRegistry.LIQUEFACTION.getId().toString());
        recipe.add("solvent", solvent);
        recipe.addProperty("solvent_amount", solventAmount);
        recipe.add("ingredient", ingredient);
        recipe.add("result", result);
        recipe.addProperty("time", liquefactionTime);
        return recipe;
    }

    @Override
    public String getName() {
        return "Liquefaction Recipes";
    }
}
