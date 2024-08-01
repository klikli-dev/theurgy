// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.niter.AlchemicalNiterItem;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import com.klikli_dev.theurgy.registry.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;
import java.util.function.BiConsumer;

public class DigestionRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = FermentationRecipe.DEFAULT_TIME;

    public DigestionRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "digestion");
    }


    public void makeTierConversion(AlchemicalNiterItem lower, AlchemicalNiterItem higher, int conversionFactor, int salAmmoniacAmount) {
        this.makeRecipe(FluidRegistry.SAL_AMMONIAC.get(), salAmmoniacAmount, List.of(
                Pair.of(lower, conversionFactor),
                Pair.of(ItemRegistry.PURIFIED_GOLD.get(), 1)
        ), higher, 1, TIME * 2, "_from_" + lower.tier().name().toLowerCase());

        this.makeRecipe(FluidRegistry.SAL_AMMONIAC.get(), salAmmoniacAmount, List.of(
                Pair.of(higher, 1)
        ), lower, conversionFactor, TIME, "_from_" + higher.tier().name().toLowerCase());
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipeWithTags(Fluids.WATER, 1000, List.of(
                Tags.Items.INGOTS_GOLD,
                ItemTagRegistry.ALCHEMICAL_SALTS
        ), ItemRegistry.PURIFIED_GOLD.get(), 20, TIME * 2);

        this.makeTierConversion(NiterRegistry.GEMS_ABUNDANT.get(), NiterRegistry.GEMS_COMMON.get(), 4, 10);
        this.makeTierConversion(NiterRegistry.GEMS_COMMON.get(), NiterRegistry.GEMS_RARE.get(), 4, 15);
        this.makeTierConversion(NiterRegistry.GEMS_RARE.get(), NiterRegistry.GEMS_PRECIOUS.get(), 4, 50);

        this.makeTierConversion(NiterRegistry.METALS_ABUNDANT.get(), NiterRegistry.METALS_COMMON.get(), 4, 10);
        this.makeTierConversion(NiterRegistry.METALS_COMMON.get(), NiterRegistry.METALS_RARE.get(), 4, 15);
        this.makeTierConversion(NiterRegistry.METALS_RARE.get(), NiterRegistry.METALS_PRECIOUS.get(), 4, 50);

        this.makeTierConversion(NiterRegistry.OTHER_MINERALS_ABUNDANT.get(), NiterRegistry.OTHER_MINERALS_COMMON.get(), 4, 10);
        this.makeTierConversion(NiterRegistry.OTHER_MINERALS_COMMON.get(), NiterRegistry.OTHER_MINERALS_RARE.get(), 4, 15);
        this.makeTierConversion(NiterRegistry.OTHER_MINERALS_RARE.get(), NiterRegistry.OTHER_MINERALS_PRECIOUS.get(), 4, 50);
    }

    public void makeRecipe(Fluid fluid, int fluidAmount, List<Pair<Item, Integer>> ingredients, Item result, int resultCount, int time, String postFix) {
        this.makeRecipe(this.name(result) + postFix, fluid, fluidAmount, ingredients, result, resultCount, time);
    }

    public void makeRecipe(Fluid fluid, int fluidAmount, List<Pair<Item, Integer>> ingredients, Item result, int resultCount, int time) {
        this.makeRecipe(this.name(result), fluid, fluidAmount, ingredients, result, resultCount, time);
    }

    public void makeRecipe(String name, Fluid fluid, int fluidAmount, List<Pair<Item, Integer>> ingredients, Item result, int resultCount, int time) {

        var recipe = new Builder(new ItemStack(result, resultCount))
                .fluid(fluid, fluidAmount)
                .time(time);

        ingredients.forEach(i -> recipe.ingredients(i.getFirst(), i.getSecond()));

        this.recipeConsumer.accept(this.modLoc(name), recipe.build());
    }

    public void makeRecipeWithTags(Fluid fluid, int fluidAmount, List<TagKey<Item>> ingredients, Item result, int resultCount, int time) {
        this.makeRecipeWithTags(this.name(result), fluid, fluidAmount, ingredients, result, resultCount, time);
    }

    public void makeRecipeWithTags(String name, Fluid fluid, int fluidAmount, List<TagKey<Item>> ingredients, Item result, int resultCount, int time) {

        var recipe = new Builder(new ItemStack(result, resultCount))
                .fluid(fluid, fluidAmount)
                .time(time);

        ingredients.forEach(i -> recipe.ingredients(i, 1));

        this.recipeConsumer.accept(this.modLoc(name), recipe.build());
    }

    @Override
    public String getName() {
        return "Digestion Recipes";
    }

    protected static class Builder extends RecipeBuilder<Builder> {
        protected Builder(ItemStack result) {
            super(RecipeTypeRegistry.DIGESTION);
            this.result(result);
            this.time(TIME);
        }

        public Builder fluid(TagKey<Fluid> tag, int amount) {
            return this.sizedFluidIngredient("fluid", tag, amount);
        }

        public Builder fluid(Fluid fluid, int amount) {
            return this.sizedFluidIngredient("fluid", fluid, amount);
        }

        public Builder ingredients(ItemLike item) {
            return this.ingredients(item, 1);
        }

        public Builder ingredients(ItemLike item, int count) {
            if (!this.recipe.has("ingredients"))
                this.recipe.add("ingredients", new JsonArray());

            this.recipe.getAsJsonArray("ingredients").add(
                    SizedIngredient.NESTED_CODEC.encodeStart(JsonOps.INSTANCE, SizedIngredient.of(item, count)).getOrThrow());

            return this.getThis();
        }

        public Builder ingredients(TagKey<Item> tag, int count) {
            if (!this.recipe.has("ingredients"))
                this.recipe.add("ingredients", new JsonArray());

            this.recipe.getAsJsonArray("ingredients").add(SizedIngredient.NESTED_CODEC.encodeStart(JsonOps.INSTANCE, SizedIngredient.of(tag, count)).getOrThrow());

            this.condition(new NotCondition(new TagEmptyCondition(tag.location().toString())));

            return this.getThis();
        }
    }
}
