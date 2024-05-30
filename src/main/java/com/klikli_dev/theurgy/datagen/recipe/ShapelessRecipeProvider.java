// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.function.BiConsumer;

public class ShapelessRecipeProvider extends JsonRecipeProvider {

    public ShapelessRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "crafting/shapeless");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe("the_hermetica",
                new ShapelessRecipeBuilder(com.klikli_dev.modonomicon.registry.ItemRegistry.MODONOMICON.get(), 1, DataComponentPatch.builder().set(DataComponentRegistry.BOOK_ID.get(), Theurgy.loc("the_hermetica")).build())
                        .requires(Items.BOOK)
                        .requires(Tags.Items.SANDS, 2)
        );

        this.makeRecipe("sal_ammoniac_crystal_from_sal_ammoniac_bucket",
                new ShapelessRecipeBuilder(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get(), 1)
                        .requires(ItemRegistry.SAL_AMMONIAC_BUCKET.get())
        );

        this.makeRecipe("logistics_item_extractor_from_inserter",
                new ShapelessRecipeBuilder(ItemRegistry.LOGISTICS_ITEM_EXTRACTOR.get(), 1)
                        .requires(ItemRegistry.LOGISTICS_ITEM_INSERTER.get())
        );

        this.makeRecipe("logistics_item_inserter_from_extractor",
                new ShapelessRecipeBuilder(ItemRegistry.LOGISTICS_ITEM_INSERTER.get(), 1)
                        .requires(ItemRegistry.LOGISTICS_ITEM_EXTRACTOR.get())
        );
    }

    protected void makeRecipe(ShapelessRecipeBuilder recipe) {
        this.makeRecipe(this.name(recipe.result.getItem()), recipe);
    }

    protected void makeRecipe(ItemLike result, ShapelessRecipeBuilder recipe) {
        this.makeRecipe(this.name(result.asItem()), recipe);
    }

    protected void makeRecipe(String name, ShapelessRecipeBuilder recipe) {
        this.recipeConsumer.accept(this.modLoc(name), recipe.build());
    }

    @Override
    public String getName() {
        return "Shapeless Crafting Recipes";
    }

    protected static class ShapelessRecipeBuilder {

        private final JsonObject recipe;
        private final ItemStack result;

        public ShapelessRecipeBuilder(ItemLike result) {
            this(result, 1);
        }

        public ShapelessRecipeBuilder(ItemLike result, int count) {
            this(result, count, null);
        }

        public ShapelessRecipeBuilder(ItemLike result, int count, DataComponentPatch patch) {
            //noinspection deprecation
            this(new ItemStack(result.asItem().builtInRegistryHolder(), count, patch));
        }

        public ShapelessRecipeBuilder(ItemStack result) {
            this.result = result;
            this.recipe = new JsonObject();
            //noinspection DataFlowIssue
            this.recipe.addProperty("type",
                    BuiltInRegistries.RECIPE_SERIALIZER.getKey(RecipeSerializer.SHAPELESS_RECIPE).toString());
            this.recipe.add("result", ItemStack.STRICT_CODEC.encodeStart(JsonOps.INSTANCE, result).getOrThrow());
            this.recipe.add("ingredients", new JsonArray());
        }

        public ShapelessRecipeBuilder requires(TagKey<Item> tag, int count) {
            for (int i = 0; i < count; ++i) {
                this.requires(tag);
            }

            return this;
        }

        private JsonObject ingredient(TagKey<Item> tag) {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("tag", tag.location().toString());
            return jsonobject;
        }

        public ShapelessRecipeBuilder requires(TagKey<Item> tag) {
            return this.requires(this.ingredient(tag));
        }

        public ShapelessRecipeBuilder requires(ItemLike item, int count) {
            for (int i = 0; i < count; ++i) {
                this.requires(item);
            }

            return this;
        }

        private JsonObject ingredient(ItemLike item) {
            JsonObject jsonobject = new JsonObject();
            //noinspection deprecation,OptionalGetWithoutIsPresent
            jsonobject.addProperty("item", item.asItem().builtInRegistryHolder().unwrapKey().get().location().toString());
            return jsonobject;
        }

        public ShapelessRecipeBuilder requires(ItemLike item) {
            return this.requires(this.ingredient(item));
        }

        public ShapelessRecipeBuilder requires(JsonObject ingredient) {
            this.recipe.getAsJsonArray("ingredients").add(ingredient);
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
