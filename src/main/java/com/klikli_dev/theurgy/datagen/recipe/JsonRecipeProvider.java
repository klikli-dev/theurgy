// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.content.recipe.result.ItemRecipeResult;
import com.klikli_dev.theurgy.content.recipe.result.RecipeResult;
import com.klikli_dev.theurgy.content.recipe.result.TagRecipeResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class JsonRecipeProvider implements DataProvider {

    protected final PackOutput.PathProvider recipePathProvider;
    protected String modid;

    protected BiConsumer<ResourceLocation, JsonObject> recipeConsumer;

    public JsonRecipeProvider(PackOutput packOutput, String modid) {
        this(packOutput, modid, "");
    }

    /**
     * Creates a new recipe provider with the given sub path.
     */
    public JsonRecipeProvider(PackOutput packOutput, String modid, String recipeSubPath) {
        this.recipePathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "recipes/" + recipeSubPath);
        this.modid = modid;
    }

    protected String name(ItemStack item) {
        return this.name(item.getItem());
    }

    protected String name(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    protected String name(TagKey<Item> tag) {
        return tag.location().getPath().replace('/', '_');
    }

    protected String name(List<TagKey<Item>> tags) {
        return tags.stream().distinct().map(this::name).reduce("", (a, b) -> a + "_and_" + b).replaceFirst("_and_", "");
    }

    public ResourceLocation locFor(TagKey<Item> tag) {
        return tag.location();
    }

    public ResourceLocation locFor(ItemLike itemLike) {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem());
    }

    public ResourceLocation locFor(Fluid fluid) {
        return BuiltInRegistries.FLUID.getKey(fluid);
    }

    public TagKey<Item> tag(String tag) {
        return this.tag(new ResourceLocation(tag));
    }

    public TagKey<Item> tag(ResourceLocation tag) {
        return TagKey.create(Registries.ITEM, tag);
    }

    public ResourceLocation modLoc(String name) {
        return new ResourceLocation(this.modid, name);
    }

    public ResourceLocation mcLoc(String name) {
        return new ResourceLocation(name);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput pOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        List<CompletableFuture<?>> futures = new ArrayList<>();
        this.recipeConsumer = (id, recipe) -> {
            if (!recipe.has("category"))
                recipe.addProperty("category", CraftingBookCategory.MISC.getSerializedName());

            if (!set.add(id)) {
                throw new IllegalStateException("Duplicate recipe " + id);
            } else {
                futures.add(DataProvider.saveStable(pOutput, recipe, this.recipePathProvider.json(id)));
            }
        };
        this.buildRecipes(this.recipeConsumer);
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    public abstract void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer);

    protected static abstract class RecipeBuilder<T extends RecipeBuilder<T>> {

        protected JsonObject recipe = new JsonObject();

        protected RecipeBuilder(Holder<RecipeType<?>> type) {
            //noinspection OptionalGetWithoutIsPresent
            this.recipe.addProperty("type", type.unwrapKey().get().location().toString());
        }

        public T getThis() {
            //noinspection unchecked
            return (T) this;
        }

        public T time(int time) {
            this.recipe.addProperty("time", time);
            return this.getThis();
        }

        public T result(ItemStack result) {
            return this.result("result", result);
        }

        public T result(String propertyName, ItemStack result) {
            return this.result(propertyName, new ItemRecipeResult(result));
        }

        public T result(RecipeResult result) {
            return this.result("result", result);
        }

        public T result(String propertyName, RecipeResult result) {
            this.recipe.add(propertyName, RecipeResult.CODEC.encodeStart(JsonOps.INSTANCE, result).getOrThrow());

            if (result instanceof TagRecipeResult tagRecipeResult) {
                this.condition(new NotCondition(new TagEmptyCondition(tagRecipeResult.tag().location().toString())));
            }

            return this.getThis();
        }

        public T result(FluidStack result) {
            return this.result("result", result);
        }

        public T result(String propertyName, FluidStack result) {
            this.recipe.add(propertyName, FluidStack.CODEC.encodeStart(JsonOps.INSTANCE, result).getOrThrow());
            return this.getThis();
        }

        public T ingredient(TagKey<?> tag) {
            return this.ingredient(tag, -1);
        }

        public T ingredient(TagKey<?> tag, int count) {
            return this.ingredient("ingredient", tag, count);
        }

        public T ingredient(String propertyName, TagKey<?> tag) {
            return this.ingredient(propertyName, tag, -1);
        }

        public T ingredient(String propertyName, TagKey<?> tag, int count) {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("tag", tag.location().toString());
            if (count > -1)
                jsonobject.addProperty("count", count);
            this.recipe.add(propertyName, jsonobject);

            this.condition(new NotCondition(new TagEmptyCondition(tag.location().toString())));

            return this.getThis();
        }

        public T ingredient(ItemStack stack) {
            return this.ingredient("ingredient", stack);
        }

        public T ingredient(Holder<Item> itemHolder) {
            return this.ingredient("ingredient", itemHolder);
        }

        public T ingredient(Item item) {
            return this.ingredient("ingredient", item);
        }

        public T ingredient(Item item, int count) {
            return this.ingredient("ingredient", new ItemStack(item, count));
        }

        public T ingredient(Holder<Item> itemHolder, int count) {
            return this.ingredient("ingredient", itemHolder, count);
        }

        public T ingredient(String propertyName, Holder<Item> itemHolder, int count) {
            return this.ingredient(propertyName, new ItemStack(itemHolder, count));
        }

        public T ingredient(String propertyName, ItemStack stack) {
            JsonObject jsonobject = new JsonObject();
            //noinspection OptionalGetWithoutIsPresent
            jsonobject.addProperty("item", stack.getItemHolder().unwrapKey().get().location().toString());
            jsonobject.addProperty("count", stack.getCount());
            this.recipe.add(propertyName, jsonobject);
            return this.getThis();
        }

        public T ingredient(String propertyName, Fluid fluid) {
            JsonObject jsonobject = new JsonObject();
            //noinspection OptionalGetWithoutIsPresent,deprecation
            jsonobject.addProperty("fluid", fluid.builtInRegistryHolder().unwrapKey().get().location().toString());
            this.recipe.add(propertyName, jsonobject);
            return this.getThis();
        }

        public T ingredient(String propertyName, Item item) {
            //noinspection deprecation
            return this.ingredient(propertyName, item.builtInRegistryHolder());
        }

        public T ingredient(String propertyName, Holder<Item> itemHolder) {
            JsonObject jsonobject = new JsonObject();
            //noinspection OptionalGetWithoutIsPresent
            jsonobject.addProperty("item", itemHolder.unwrapKey().get().location().toString());
            this.recipe.add(propertyName, jsonobject);
            return this.getThis();
        }

        public T condition(ICondition condition) {
            if (!this.recipe.has("conditions"))
                this.recipe.add("conditions", new JsonArray());

            this.recipe.getAsJsonArray("conditions").add(
                    ICondition.CODEC.encodeStart(JsonOps.INSTANCE, condition).getOrThrow()
            );
            return this.getThis();
        }


        public JsonObject build() {
            if (!this.recipe.has("category"))
                this.recipe.addProperty("category", CraftingBookCategory.MISC.getSerializedName());

            return this.recipe;
        }
    }
}
