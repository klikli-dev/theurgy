// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class SmeltingRecipeProvider extends JsonRecipeProvider {

    public SmeltingRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "smelting");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
//        this.makeRecipe("sal_ammoniac_crystal_from_sal_ammoniac_bucket",
//                new RecipeBuilder(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get(), 1)
//                        .requires(ItemRegistry.SAL_AMMONIAC_BUCKET.get())
//        ); //opted against this as it voids the bucket
    }

    public void makeRecipe(String name, RecipeBuilder recipe) {
        this.recipeConsumer.accept(this.modLoc(name), recipe.build());
    }

    @Override
    public String getName() {
        return "Smelting Recipes";
    }

    private class RecipeBuilder {

        JsonObject recipe;

        public RecipeBuilder(ItemLike result) {
            this(result, 1);
        }

        public RecipeBuilder(ItemLike result, int count) {
            this(result, count, null);
        }

        public RecipeBuilder(ItemLike result, int count, @Nullable JsonObject nbt) {
            this(SmeltingRecipeProvider.this.makeItemResult(SmeltingRecipeProvider.this.locFor(result), count, nbt));
        }

        public RecipeBuilder(JsonObject result) {
            this.recipe = new JsonObject();
            this.recipe.addProperty("type",
                    BuiltInRegistries.RECIPE_SERIALIZER.getKey(RecipeSerializer.SMELTING_RECIPE).toString());
            this.recipe.add("result", result);
            this.recipe.addProperty("cookingtime", 200);
            this.recipe.addProperty("experience", 0.7f);
        }

        public RecipeBuilder time(int time) {
            this.recipe.addProperty("cookingtime", time);
            return this;
        }

        public RecipeBuilder experience(float exp) {
            this.recipe.addProperty("experience", exp);
            return this;
        }

        public RecipeBuilder requires(TagKey<Item> tag) {
            return this.requires(SmeltingRecipeProvider.this.makeTagIngredient(SmeltingRecipeProvider.this.locFor(tag)));
        }

        public RecipeBuilder requires(ItemLike item) {
            return this.requires(SmeltingRecipeProvider.this.makeItemIngredient(SmeltingRecipeProvider.this.locFor(item)));
        }

        public RecipeBuilder requires(JsonObject ingredient) {
            this.recipe.add("ingredient", ingredient);
            return this;
        }

        public JsonObject build() {
            if(!this.recipe.has("category"))
                this.recipe.addProperty("category", CraftingBookCategory.MISC.getSerializedName());

            return this.recipe;
        }

    }
}
