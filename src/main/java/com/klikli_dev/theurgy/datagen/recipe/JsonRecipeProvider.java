/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class JsonRecipeProvider implements DataProvider {

    protected final PackOutput.PathProvider recipePathProvider;
    protected String modid;

    public JsonRecipeProvider(PackOutput packOutput, String modid) {
        this(packOutput, modid, "");
    }

    /**
     * Creates a new recipe provider with the given sub path.
     *
     * @param packOutput
     * @param recipeSubPath e.g. "calcination"
     */
    public JsonRecipeProvider(PackOutput packOutput, String modid, String recipeSubPath) {
        this.recipePathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "recipes/" + recipeSubPath);
        this.modid = modid;
    }

    public ResourceLocation modLoc(String name) {
        return new ResourceLocation(this.modid, name);
    }

    public ResourceLocation mcLoc(String name) {
        return new ResourceLocation(name);
    }

    public ResourceLocation forgeLoc(String name) {
        return new ResourceLocation("forge", name);
    }

    public JsonObject makeTagIngredient(ResourceLocation tag) {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("tag", tag.toString());
        return jsonobject;
    }

    public JsonObject makeItemIngredient(ResourceLocation item) {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("item", item.toString());
        return jsonobject;
    }

    public JsonObject makeResult(ResourceLocation item) {
        return this.makeResult(item, 1);
    }

    public JsonObject makeResult(ResourceLocation item, int count) {
        return this.makeResult(item, count, null);
    }

    public JsonObject makeResult(ResourceLocation item, int count, CompoundTag nbt) {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("item", item.toString());
        jsonobject.addProperty("count", count);
        if (nbt != null) {
            jsonobject.addProperty("nbt", nbt.toString());
        }
        return jsonobject;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        List<CompletableFuture<?>> futures = new ArrayList<>();
        this.buildRecipes((id, recipe) -> {
            if (!set.add(id)) {
                throw new IllegalStateException("Duplicate recipe " + id);
            } else {
                futures.add(DataProvider.saveStable(pOutput, recipe, this.recipePathProvider.json(id)));
            }
        });
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    abstract void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer);
}
