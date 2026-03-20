// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.content.recipe.condition.FluidTagEmptyCondition;
import com.klikli_dev.theurgy.content.recipe.result.ItemRecipeResult;
import com.klikli_dev.theurgy.content.recipe.result.RecipeResult;
import com.klikli_dev.theurgy.content.recipe.result.TagRecipeResult;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public abstract class JsonRecipeProvider implements DataProvider {

    protected final CompletableFuture<HolderLookup.Provider> lookupProvider;
    protected HolderLookup.Provider registries;
    protected RegistryOps<JsonElement> registryOps;
    protected HolderGetter<Item> items;
    protected HolderGetter<Fluid> fluids;
    protected final PackOutput.PathProvider recipePathProvider;
    protected String modid;

    protected BiConsumer<Identifier, JsonObject> recipeConsumer;

    public JsonRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, String modid) {
        this(packOutput, lookupProvider, modid, "");
    }

    /**
     * Creates a new recipe provider with the given sub path.
     */
    public JsonRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, String modid, String recipeSubPath) {
        this.lookupProvider = lookupProvider;
        this.recipePathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "recipe/" + recipeSubPath);
        this.modid = modid;
    }

    protected String name(ItemStack item) {
        return this.name(item.getItem());
    }

    protected String name(ItemLike item) {
        return itemId(item).getPath();
    }

    protected String name(TagKey<Item> tag) {
        return tag.location().getPath().replace('/', '_');
    }

    protected String name(List<TagKey<Item>> tags) {
        return tags.stream().distinct().map(this::name).reduce("", (a, b) -> a + "_and_" + b).replaceFirst("_and_", "");
    }

    public Identifier locFor(TagKey<Item> tag) {
        return tag.location();
    }

    public Identifier locFor(ItemLike itemLike) {
        return itemId(itemLike);
    }

    protected Identifier itemId(ItemLike itemLike) {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem());
    }

    protected ItemStack createItemStack(ItemLike itemLike, int count, DataComponentPatch patch) {
        var stack = new ItemStack(itemLike, count);
        if (patch != null) {
            stack.applyComponentsAndValidate(patch);
        }
        return stack;
    }

    public Identifier locFor(Fluid fluid) {
        return BuiltInRegistries.FLUID.getKey(fluid);
    }

    public TagKey<Item> tag(String tag) {
        return this.tag(Identifier.parse(tag));
    }

    public TagKey<Item> tag(Identifier tag) {
        return TagKey.create(Registries.ITEM, tag);
    }

    public Identifier modLoc(String name) {
        return Identifier.fromNamespaceAndPath(this.modid, name);
    }

    public Identifier mcLoc(String name) {
        return Identifier.parse(name);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput pOutput) {
        return this.lookupProvider.thenCompose(provider -> {
            this.registries = provider;
            this.registryOps = this.registries.createSerializationContext(JsonOps.INSTANCE);
            this.items = this.registries.lookupOrThrow(Registries.ITEM);
            this.fluids = this.registries.lookupOrThrow(Registries.FLUID);

            Set<Identifier> set = Sets.newHashSet();
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
        });
    }

    public abstract void buildRecipes(BiConsumer<Identifier, JsonObject> recipeConsumer);

    protected abstract class RecipeBuilder<T extends RecipeBuilder<T>> {

        protected JsonObject recipe = new JsonObject();

        protected RecipeBuilder(Holder<RecipeType<?>> type) {
            //noinspection OptionalGetWithoutIsPresent
            this.recipe.addProperty("type", type.unwrapKey().get().identifier().toString());
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
            this.recipe.add(propertyName, RecipeResult.CODEC.encodeStart(JsonRecipeProvider.this.registryOps, result).getOrThrow());

            if (result instanceof TagRecipeResult tagRecipeResult) {
                this.condition(new NotCondition(new TagEmptyCondition<>(tagRecipeResult.tag())));
            }

            return this.getThis();
        }

        public T result(FluidStack result) {
            return this.result("result", result);
        }

        public T result(String propertyName, FluidStack result) {
            this.recipe.add(propertyName, FluidStack.CODEC.encodeStart(JsonRecipeProvider.this.registryOps, result).getOrThrow());
            return this.getThis();
        }

        public T ingredient(TagKey<Item> tag) {
            return this.ingredient("ingredient", tag);
        }

        public T sizedIngredient(TagKey<Item> tag, int count) {
            return this.sizedIngredient("ingredient", tag, count);
        }

        public T sizedIngredient(ItemLike item, int count) {
            return this.sizedIngredient("ingredient", item, count);
        }

        public T ingredient(String propertyName, TagKey<Item> tag) {
            this.recipe.add(propertyName, Ingredient.CODEC.encodeStart(JsonRecipeProvider.this.registryOps, Ingredient.of(JsonRecipeProvider.this.items.get(tag).orElseThrow())).getOrThrow());

            this.condition(new NotCondition(new TagEmptyCondition<>(tag)));

            return this.getThis();
        }

        public T ingredient(ItemLike item) {
            return this.ingredient("ingredient", item);
        }

        public T ingredient(String propertyName, ItemLike item) {
            return this.ingredient(propertyName, Ingredient.of(item));
        }

        public T ingredient(String propertyName, Ingredient ingredient) {
            this.recipe.add(propertyName, Ingredient.CODEC.encodeStart(JsonRecipeProvider.this.registryOps, ingredient).getOrThrow());
            return this.getThis();
        }

        public T sizedIngredient(String propertyName, TagKey<Item> tag, int amount) {
            this.recipe.add(propertyName, SizedIngredient.NESTED_CODEC.encodeStart(JsonRecipeProvider.this.registryOps, new SizedIngredient(Ingredient.of(JsonRecipeProvider.this.items.getOrThrow(tag)), amount)).getOrThrow());
            this.condition(new NotCondition(new TagEmptyCondition<>(tag)));
            return this.getThis();
        }

        public T sizedIngredient(String propertyName, ItemLike item, int amount) {
            this.recipe.add(propertyName, SizedIngredient.NESTED_CODEC.encodeStart(JsonRecipeProvider.this.registryOps, new SizedIngredient(Ingredient.of(item), amount)).getOrThrow());
            return this.getThis();
        }

        public T sizedFluidIngredient(String propertyName, TagKey<Fluid> fluid, int amount) {
            this.recipe.add(propertyName, SizedFluidIngredient.CODEC.encodeStart(JsonRecipeProvider.this.registryOps, new SizedFluidIngredient(FluidIngredient.of(JsonRecipeProvider.this.fluids.get(fluid).orElseThrow()), amount)).getOrThrow());
            //TODO: enable once kubejs offers fluid tag cache access
//            this.condition(new NotCondition(new FluidTagEmptyCondition(fluid.location().toString())));
            return this.getThis();
        }

        public T sizedFluidIngredient(String propertyName, Fluid fluid, int amount) {
            this.recipe.add(propertyName, SizedFluidIngredient.CODEC.encodeStart(JsonRecipeProvider.this.registryOps, SizedFluidIngredient.of(fluid, amount)).getOrThrow());
            return this.getThis();
        }

        public T fluidIngredient(String propertyName, Fluid fluid) {
            this.recipe.add(propertyName, FluidIngredient.CODEC.encodeStart(JsonRecipeProvider.this.registryOps, FluidIngredient.of(fluid)).getOrThrow());
            return this.getThis();
        }

        public T ingredient(String propertyName, Item item) {
            return this.ingredient(propertyName, BuiltInRegistries.ITEM.wrapAsHolder(item));
        }

        public T ingredient(String propertyName, Holder<Item> itemHolder) {
            JsonObject jsonobject = new JsonObject();
            //noinspection OptionalGetWithoutIsPresent
            jsonobject.addProperty("item", itemHolder.unwrapKey().get().identifier().toString());
            this.recipe.add(propertyName, jsonobject);
            return this.getThis();
        }

        public T condition(ICondition condition) {
            if (!this.recipe.has("neoforge:conditions"))
                this.recipe.add("neoforge:conditions", new JsonArray());

            this.recipe.getAsJsonArray("neoforge:conditions").add(
                    ICondition.CODEC.encodeStart(JsonRecipeProvider.this.registryOps, condition).getOrThrow()
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
