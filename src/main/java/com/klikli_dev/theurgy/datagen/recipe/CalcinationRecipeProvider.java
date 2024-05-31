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
        this.makeRecipe("_from_stone", new Builder(SaltRegistry.STRATA).ingredient(Tags.Items.STONES));
        this.makeRecipe("_from_sandstone", new Builder(SaltRegistry.STRATA).ingredient(Tags.Items.SANDSTONE_BLOCKS));
        this.makeRecipe("_from_cobblestone", new Builder(SaltRegistry.STRATA).ingredient(Tags.Items.COBBLESTONES));
        this.makeRecipe("_from_dirt", new Builder(SaltRegistry.STRATA).ingredient(ItemTags.DIRT));
        this.makeRecipe("_from_sand", new Builder(SaltRegistry.STRATA).ingredient(ItemTags.SAND));
        this.makeRecipe("_from_gravel", new Builder(SaltRegistry.STRATA).ingredient(Items.GRAVEL));
        this.makeRecipe("_from_clay", new Builder(SaltRegistry.STRATA, 4).ingredient(Items.CLAY));
        this.makeRecipe("_from_clay_ball", new Builder(SaltRegistry.STRATA).ingredient(Items.CLAY_BALL));
        this.makeRecipe("_from_ores", new Builder(SaltRegistry.MINERAL).ingredient(Tags.Items.ORES));
        this.makeRecipe("_from_raw_materials", new Builder(SaltRegistry.MINERAL).ingredient(Tags.Items.RAW_MATERIALS));
        this.makeRecipe("_from_ingots", new Builder(SaltRegistry.MINERAL, 2).ingredient(Tags.Items.INGOTS));
        this.makeRecipe("_from_gems", new Builder(SaltRegistry.MINERAL, 2).ingredient(Tags.Items.GEMS));
        this.makeRecipe("_from_other_minerals", new Builder(SaltRegistry.MINERAL, 2).ingredient(ItemTagRegistry.OTHER_MINERALS));
        this.makeRecipe("", new Builder(SaltRegistry.MINERAL).ingredient(SaltRegistry.STRATA.get(), 20));
        this.makeRecipe("", new Builder(SaltRegistry.CROPS).ingredient(Tags.Items.CROPS));
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

        @Override
        public Builder ingredient(TagKey<?> tag) {
            return this.ingredient(tag, 1);
        }

        @Override
        public Builder ingredient(Item item) {
            return this.ingredient(item, 1);
        }

        @Override
        public Builder ingredient(TagKey<?> tag, int amount) {
            this.recipe.addProperty("ingredientCount", amount);
            return this.ingredient("ingredient", tag, -1);
        }

        @Override
        public Builder ingredient(Item item, int amount) {
            this.recipe.addProperty("ingredientCount", amount);
            return this.ingredient("ingredient", item);
        }

        public ItemStack result() {
            return this.result;
        }
    }
}
