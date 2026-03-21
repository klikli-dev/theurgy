// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.function.BiConsumer;

public class SmeltingRecipeProvider extends JsonRecipeProvider {

    public SmeltingRecipeProvider(PackOutput packOutput, java.util.concurrent.CompletableFuture<net.minecraft.core.HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, Theurgy.MODID, "smelting");
    }

    @Override
    public void buildRecipes(BiConsumer<Identifier, JsonObject> recipeConsumer) {
//        this.makeRecipe("sal_ammoniac_crystal_from_sal_ammoniac_bucket",
//                new RecipeBuilder(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get(), 1)
//                        .requires(ItemRegistry.SAL_AMMONIAC_BUCKET.get())
//        ); //opted against this as it voids the bucket
    }

    protected void makeRecipe(SmeltingRecipeBuilder recipe) {
        this.makeRecipe(this.name(recipe.result), recipe);
    }

    protected void makeRecipe(ItemLike result, SmeltingRecipeBuilder recipe) {
        this.makeRecipe(this.name(result.asItem()), recipe);
    }

    protected void makeRecipe(String name, SmeltingRecipeBuilder recipe) {
        this.recipeConsumer.accept(this.modLoc(name), recipe.build());
    }

    @Override
    public String getName() {
        return "Smelting Recipes";
    }

    protected class SmeltingRecipeBuilder {

        private final JsonObject recipe;
        private final ItemStackTemplate result;

        public SmeltingRecipeBuilder(ItemLike result) {
            this(result, 1);
        }

        public SmeltingRecipeBuilder(ItemLike result, int count) {
            this(result, count, null);
        }

        public SmeltingRecipeBuilder(ItemLike result, int count, DataComponentPatch patch) {
            this(SmeltingRecipeProvider.this.createItemStack(result, count, patch));
        }

        public SmeltingRecipeBuilder(ItemStackTemplate result) {
            this.result = result;
            this.recipe = new JsonObject();
            //noinspection DataFlowIssue
            this.recipe.addProperty("type",
                    "minecraft:smelting");
            this.recipe.add("result", ItemStackTemplate.CODEC.encodeStart(SmeltingRecipeProvider.this.registryOps, result).getOrThrow());
            this.recipe.addProperty("cookingtime", 200);
            this.recipe.addProperty("experience", 0.7f);
        }

        public SmeltingRecipeBuilder time(int time) {
            this.recipe.addProperty("cookingtime", time);
            return this;
        }

        public SmeltingRecipeBuilder experience(float exp) {
            this.recipe.addProperty("experience", exp);
            return this;
        }

        private JsonElement ingredient(TagKey<Item> tag) {
            return Ingredient.CODEC.encodeStart(SmeltingRecipeProvider.this.registryOps, Ingredient.of(SmeltingRecipeProvider.this.items.getOrThrow(tag))).getOrThrow();
        }

        public SmeltingRecipeBuilder requires(TagKey<Item> tag) {
            return this.requires(this.ingredient(tag));
        }

        private JsonElement ingredient(ItemLike item) {
            return Ingredient.CODEC.encodeStart(SmeltingRecipeProvider.this.registryOps, Ingredient.of(item)).getOrThrow();
        }

        public SmeltingRecipeBuilder requires(ItemLike item) {
            return this.requires(this.ingredient(item));
        }

        public SmeltingRecipeBuilder requires(JsonElement ingredient) {
            this.recipe.add("ingredient", ingredient);
            return this;
        }

        public JsonObject build() {
            if (!this.recipe.has("category"))
                this.recipe.addProperty("category", CraftingBookCategory.MISC.getSerializedName());

            return this.recipe;
        }

        public ItemStackTemplate result() {
            return this.result;
        }
    }
}
