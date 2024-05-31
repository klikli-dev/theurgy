// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

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

    protected void makeRecipe(SmeltingRecipeBuilder recipe) {
        this.makeRecipe(this.name(recipe.result.getItem()), recipe);
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
        private final ItemStack result;

        public SmeltingRecipeBuilder(ItemLike result) {
            this(result, 1);
        }

        public SmeltingRecipeBuilder(ItemLike result, int count) {
            this(result, count, null);
        }

        public SmeltingRecipeBuilder(ItemLike result, int count, DataComponentPatch patch) {
            //noinspection deprecation
            this(new ItemStack(result.asItem().builtInRegistryHolder(), count, patch));
        }

        public SmeltingRecipeBuilder(ItemStack result) {
            this.result = result;
            this.recipe = new JsonObject();
            //noinspection DataFlowIssue
            this.recipe.addProperty("type",
                    BuiltInRegistries.RECIPE_SERIALIZER.getKey(RecipeSerializer.SMELTING_RECIPE).toString());
            this.recipe.add("result", ItemStack.STRICT_CODEC.encodeStart(JsonOps.INSTANCE, result).getOrThrow());
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

        private JsonObject ingredient(TagKey<Item> tag) {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("tag", tag.location().toString());
            return jsonobject;
        }

        public SmeltingRecipeBuilder requires(TagKey<Item> tag) {
            return this.requires(this.ingredient(tag));
        }

        private JsonObject ingredient(ItemLike item) {
            JsonObject jsonobject = new JsonObject();
            //noinspection deprecation,OptionalGetWithoutIsPresent
            jsonobject.addProperty("item", item.asItem().builtInRegistryHolder().unwrapKey().get().location().toString());
            return jsonobject;
        }

        public SmeltingRecipeBuilder requires(ItemLike item) {
            return this.requires(this.ingredient(item));
        }

        public SmeltingRecipeBuilder requires(JsonObject ingredient) {
            this.recipe.add("ingredient", ingredient);
            return this;
        }

        public JsonObject build() {
            if (!this.recipe.has("category"))
                this.recipe.addProperty("category", CraftingBookCategory.MISC.getSerializedName());

            return this.recipe;
        }

        public ItemStack result() {
            return this.result;
        }
    }
}
