// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.function.BiConsumer;

public class CalcinationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = CalcinationRecipe.DEFAULT_TIME;

    public CalcinationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "calcination");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe("_from_stone", new Builder(SaltRegistry.STRATA).sizedIngredient(Tags.Items.STONES));
        this.makeRecipe("_from_sandstone", new Builder(SaltRegistry.STRATA).sizedIngredient(Tags.Items.SANDSTONE_BLOCKS));
        this.makeRecipe("_from_cobblestone", new Builder(SaltRegistry.STRATA).sizedIngredient(Tags.Items.COBBLESTONES));
        this.makeRecipe("_from_dirt", new Builder(SaltRegistry.STRATA).sizedIngredient(ItemTags.DIRT));
        this.makeRecipe("_from_sand", new Builder(SaltRegistry.STRATA).sizedIngredient(ItemTags.SAND));
        this.makeRecipe("_from_gravel", new Builder(SaltRegistry.STRATA).sizedIngredient(Items.GRAVEL));
        this.makeRecipe("_from_clay", new Builder(SaltRegistry.STRATA, 4).sizedIngredient(Items.CLAY));
        this.makeRecipe("_from_clay_ball", new Builder(SaltRegistry.STRATA).sizedIngredient(Items.CLAY_BALL));
        this.makeRecipe("_from_ores", new Builder(SaltRegistry.MINERAL).sizedIngredient(Tags.Items.ORES));
        this.makeRecipe("_from_raw_materials", new Builder(SaltRegistry.MINERAL).sizedIngredient(Tags.Items.RAW_MATERIALS));
        this.makeRecipe("_from_ingots", new Builder(SaltRegistry.MINERAL, 2).sizedIngredient(Tags.Items.INGOTS));
        this.makeRecipe("_from_gems", new Builder(SaltRegistry.MINERAL, 2).sizedIngredient(Tags.Items.GEMS));
        this.makeRecipe("_from_other_minerals", new Builder(SaltRegistry.MINERAL, 2).sizedIngredient(ItemTagRegistry.OTHER_MINERALS));
        this.makeRecipe("_from_strata_salt", new Builder(SaltRegistry.MINERAL).sizedIngredient(SaltRegistry.STRATA.get(), 20));
        this.makeRecipe("_from_crops", new Builder(SaltRegistry.PLANT).sizedIngredient(Tags.Items.CROPS));
        this.makeRecipe("_from_logs", new Builder(SaltRegistry.PLANT).sizedIngredient(ItemTags.LOGS));
        this.makeRecipe("_from_leaves", new Builder(SaltRegistry.PLANT).sizedIngredient(ItemTags.LEAVES));
        this.makeRecipe("_from_saplings", new Builder(SaltRegistry.PLANT).sizedIngredient(ItemTags.SAPLINGS));
    }


    protected void makeRecipe(String suffix, Builder recipe) {
        this.recipeConsumer.accept(this.modLoc(this.name(recipe.result()) + suffix), recipe.build());
    }

    @Override
    public String getName() {
        return "Calcination Recipes";
    }

    protected static class Builder extends RecipeBuilder<Builder> {

        private final ItemStack result;

        protected Builder(ItemLike result) {
            this(result, 1);
        }

        protected Builder(ItemLike result, int count) {
            this(new ItemStack(result, count));
        }

        protected Builder(ItemStack result) {
            super(RecipeTypeRegistry.CALCINATION);
            this.result(result);
            this.time(TIME);
            this.result = result;
        }

        public ItemStack result() {
            return this.result;
        }

        public Builder sizedIngredient(TagKey<Item> tag) {
            return super.sizedIngredient(tag, 1);
        }

        public Builder sizedIngredient(ItemLike item) {
            return super.sizedIngredient(item, 1);
        }
    }
}
