// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import com.klikli_dev.theurgy.registry.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
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
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
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
            this.recipe.addProperty("fluidAmount", amount);
            return this.ingredient("fluid", tag, -1);
        }

        public Builder fluid(Fluid fluid, int amount) {
            this.recipe.addProperty("fluidAmount", amount);
            return this.ingredient("fluid", fluid);
        }

        public Builder ingredients(ItemLike item) {
            //noinspection deprecation
            return this.ingredients(item.asItem().builtInRegistryHolder());
        }

        public Builder ingredients(ItemLike item, int count) {
            //noinspection deprecation
            return this.ingredients(item.asItem().builtInRegistryHolder(), 1);
        }

        public Builder ingredients(Holder<Item> itemHolder) {
            return this.ingredients(itemHolder, 1);
        }

        public Builder ingredients(Holder<Item> itemHolder, int count) {
            if (!this.recipe.has("ingredients"))
                this.recipe.add("ingredients", new JsonArray());

            JsonObject jsonobject = new JsonObject();
            //noinspection OptionalGetWithoutIsPresent
            jsonobject.addProperty("item", itemHolder.unwrapKey().get().location().toString());
            jsonobject.addProperty("count", count);

            this.recipe.getAsJsonArray("ingredients").add(jsonobject);

            return this.getThis();
        }


        public Builder ingredients(TagKey<?> tag, int count) {
            if (!this.recipe.has("ingredients"))
                this.recipe.add("ingredients", new JsonArray());

            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("tag", tag.location().toString());
            if (count > -1)
                jsonobject.addProperty("count", count);

            this.recipe.getAsJsonArray("ingredients").add(jsonobject);

            this.condition(new NotCondition(new TagEmptyCondition(tag.location().toString())));

            return this.getThis();
        }
    }
}
