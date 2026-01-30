// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class ShapelessRecipeProvider extends JsonRecipeProvider {

    public ShapelessRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "crafting/shapeless");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {

        var hermeticaNbt = new JsonObject();
        hermeticaNbt.addProperty("modonomicon:book_id", "theurgy:the_hermetica");
        this.makeRecipe("the_hermetica",
                new RecipeBuilder(com.klikli_dev.modonomicon.registry.ItemRegistry.MODONOMICON.get(), 1, hermeticaNbt)
                        .requires(Items.BOOK)
                        .requires(Tags.Items.SAND, 2)
        );

        this.makeRecipe("sal_ammoniac_crystal_from_sal_ammoniac_bucket",
                new RecipeBuilder(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get(), 1)
                        .requires(ItemRegistry.SAL_AMMONIAC_BUCKET.get())
        );

        this.makeRecipe("mercury_shards_from_mercury_crystal",
                new RecipeBuilder(ItemRegistry.MERCURY_SHARD.get(), 4)
                        .requires(ItemRegistry.MERCURY_CRYSTAL.get())
        );

        this.makeRecipe("mercury_crystal_from_mercury_shards",
                new RecipeBuilder(ItemRegistry.MERCURY_CRYSTAL.get(), 1)
                        .requires(ItemRegistry.MERCURY_SHARD.get())
                        .requires(ItemRegistry.MERCURY_SHARD.get())
                        .requires(ItemRegistry.MERCURY_SHARD.get())
                        .requires(ItemRegistry.MERCURY_SHARD.get())
        );

        this.makeRecipe("crystallized_water_from_water_bucket",
                new RecipeBuilder(ItemRegistry.CRYSTALLIZED_WATER.get(), 1)
                        .requires(ItemTagRegistry.ALCHEMICAL_SALTS)
                        .requires(Items.WATER_BUCKET)
        );

        this.makeRecipe("crystallized_lava_from_lava_bucket",
                new RecipeBuilder(ItemRegistry.CRYSTALLIZED_LAVA.get(), 1)
                        .requires(ItemTagRegistry.ALCHEMICAL_SALTS)
                        .requires(Items.LAVA_BUCKET)
        );

        this.makeRecipe("water_bucket_from_crystallized_water",
                new RecipeBuilder(Items.WATER_BUCKET, 1)
                        .requires(ItemRegistry.CRYSTALLIZED_WATER.get())
                        .requires(Items.BUCKET)
        );

        this.makeRecipe("lava_bucket_from_crystallized_lava",
                new RecipeBuilder(Items.LAVA_BUCKET, 1)
                        .requires(ItemRegistry.CRYSTALLIZED_LAVA.get())
                        .requires(Items.BUCKET)
        );
    }

    public void makeRecipe(String name, RecipeBuilder recipe) {
        this.recipeConsumer.accept(this.modLoc(name), recipe.build());
    }

    @Override
    public String getName() {
        return "Shapeless Crafting Recipes";
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
            this(ShapelessRecipeProvider.this.makeItemResult(ShapelessRecipeProvider.this.locFor(result), count, nbt));
        }

        public RecipeBuilder(JsonObject result) {
            this.recipe = new JsonObject();
            this.recipe.addProperty("type",
                    ForgeRegistries.RECIPE_SERIALIZERS.getKey(RecipeSerializer.SHAPELESS_RECIPE).toString());
            this.recipe.add("result", result);
            this.recipe.add("ingredients", new JsonArray());
        }

        public RecipeBuilder requires(TagKey<Item> tag, int count) {
            for (int i = 0; i < count; ++i) {
                this.requires(tag);
            }

            return this;
        }

        public RecipeBuilder requires(TagKey<Item> tag) {
            return this.requires(ShapelessRecipeProvider.this.makeTagIngredient(ShapelessRecipeProvider.this.locFor(tag)));
        }

        public RecipeBuilder requires(ItemLike item, int count) {
            for (int i = 0; i < count; ++i) {
                this.requires(item);
            }

            return this;
        }

        public RecipeBuilder requires(ItemLike item) {
            return this.requires(ShapelessRecipeProvider.this.makeItemIngredient(ShapelessRecipeProvider.this.locFor(item)));
        }

        public RecipeBuilder requires(JsonObject ingredient) {
            this.recipe.getAsJsonArray("ingredients").add(ingredient);
            return this;
        }

        public JsonObject build() {
            return this.recipe;
        }

    }
}
