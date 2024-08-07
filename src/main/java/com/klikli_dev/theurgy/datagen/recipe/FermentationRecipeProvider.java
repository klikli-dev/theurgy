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
import com.mojang.serialization.JsonOps;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
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
import org.jetbrains.annotations.NotNull;

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
        var recipe = new Builder(new ItemStack(ItemRegistry.FERMENTATION_STARTER.get(), 20))
                .fluid(FluidRegistry.SAL_AMMONIAC.get(), 100)
                .ingredients(cropTag)
                .ingredients(ItemTagRegistry.SUGARS)
                .time(TIME);

        this.makeRecipe("_from_" + this.name(cropTag), recipe);
    }

    public void makeRecipesForCropTag(TagKey<Item> cropTag) {
        this.makeRecipe(Fluids.WATER, 125, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT,
                cropTag
        ), NiterRegistry.GEMS_ABUNDANT.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 250, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON,
                cropTag
        ), NiterRegistry.GEMS_COMMON.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 500, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE,
                cropTag
        ), NiterRegistry.GEMS_RARE.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 1000, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS,
                cropTag
        ), NiterRegistry.GEMS_PRECIOUS.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 125, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT,
                cropTag
        ), NiterRegistry.METALS_ABUNDANT.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 250, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON,
                cropTag
        ), NiterRegistry.METALS_COMMON.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 500, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE,
                cropTag
        ), NiterRegistry.METALS_RARE.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 1000, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS,
                cropTag
        ), NiterRegistry.METALS_PRECIOUS.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 125, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT,
                cropTag
        ), NiterRegistry.OTHER_MINERALS_ABUNDANT.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 250, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON,
                cropTag
        ), NiterRegistry.OTHER_MINERALS_COMMON.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 500, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE,
                cropTag
        ), NiterRegistry.OTHER_MINERALS_RARE.get(), 1, TIME, "_using_" + this.name(cropTag));

        this.makeRecipe(Fluids.WATER, 1000, List.of(
                ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS,
                cropTag
        ), NiterRegistry.OTHER_MINERALS_PRECIOUS.get(), 1, TIME, "_using_" + this.name(cropTag));


        this.makeRecipe("_using_" + this.name(cropTag), new Builder(NiterRegistry.LOGS_ABUNDANT)
                .fluid(Fluids.WATER, 125)
                .ingredients(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_ABUNDANT)
                        .ingredients(cropTag)
                .time(TIME));

        this.makeRecipe("_using_" + this.name(cropTag), new Builder(NiterRegistry.CROPS_ABUNDANT)
                .fluid(Fluids.WATER, 125)
                .ingredients(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_ABUNDANT)
                .ingredients(cropTag)
                .time(TIME));

        this.makeRecipe("_using_" + this.name(cropTag), new Builder(NiterRegistry.ANIMALS_ABUNDANT)
                .fluid(Fluids.WATER, 125)
                .ingredients(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_ABUNDANT)
                .ingredients(cropTag)
                .time(TIME));
        this.makeRecipe("_using_" + this.name(cropTag), new Builder(NiterRegistry.ANIMALS_COMMON)
                .fluid(Fluids.WATER, 250)
                .ingredients(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_COMMON)
                .ingredients(cropTag)
                .time(TIME));
        this.makeRecipe("_using_" + this.name(cropTag), new Builder(NiterRegistry.ANIMALS_RARE)
                .fluid(Fluids.WATER, 500)
                .ingredients(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_RARE)
                .ingredients(cropTag)
                .time(TIME));
    }

    public void makeRecipe(Fluid fluid, int fluidAmount, List<TagKey<Item>> ingredients, Item result, int resultCount, int time) {
        this.makeRecipe(this.name(result), fluid, fluidAmount, ingredients, result, resultCount, time);
    }

    public void makeRecipe(Fluid fluid, int fluidAmount, List<TagKey<Item>> ingredients, Item result, int resultCount, int time, String postFix) {
        this.makeRecipe(this.name(result) + postFix, fluid, fluidAmount, ingredients, result, resultCount, time);
    }

    public void makeRecipe(String name, Fluid fluid, int fluidAmount, List<TagKey<Item>> ingredients, Item result, int resultCount, int time) {

        var recipe = new Builder(new ItemStack(result, resultCount))
                .fluid(fluid, fluidAmount)
                .time(time);

        ingredients.forEach(recipe::ingredients);

        this.recipeConsumer.accept(this.modLoc(name), recipe.build());
    }

    protected void makeRecipe(String suffix, Builder recipe) {
        this.recipeConsumer.accept(this.modLoc(this.name(recipe.result()) + suffix), recipe.build());
    }


    @Override
    public @NotNull String getName() {
        return "Fermentation Recipes";
    }

    protected static class Builder extends RecipeBuilder<Builder> {
        private final ItemStack result;

        protected Builder(ItemLike result) {
            this(new ItemStack(result, 1));
        }

        protected Builder(ItemStack result) {
            super(RecipeTypeRegistry.FERMENTATION);
            this.result(result);
            this.result = result;
            this.time(TIME);
        }

        public ItemStack result() {
            return this.result;
        }

        public Builder fluid(TagKey<Fluid> tag, int amount) {
            return this.sizedFluidIngredient("fluid", tag, amount);
        }

        public Builder fluid(Fluid fluid, int amount) {
            return this.sizedFluidIngredient("fluid", fluid, amount);
        }

        public Builder ingredients(ItemLike item) {
            //noinspection deprecation
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

        public Builder ingredients(TagKey<Item> tag) {
            if (!this.recipe.has("ingredients"))
                this.recipe.add("ingredients", new JsonArray());

            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("tag", tag.location().toString());

            this.recipe.getAsJsonArray("ingredients").add(jsonobject);

            this.condition(new NotCondition(new TagEmptyCondition(tag.location().toString())));

            return this.getThis();
        }
    }
}
