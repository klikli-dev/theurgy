/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class JsonRecipeProvider implements DataProvider {

    protected final DataGenerator.PathProvider recipePathProvider;
    protected String modid;

    public JsonRecipeProvider(DataGenerator pGenerator, String modid) {
        this(pGenerator, modid, "");
    }

    /**
     * Creates a new recipe provider with the given sub path.
     *
     * @param pGenerator
     * @param recipeSubPath e.g. "calcination"
     */
    public JsonRecipeProvider(DataGenerator pGenerator, String modid, String recipeSubPath) {
        this.recipePathProvider = pGenerator.createPathProvider(DataGenerator.Target.DATA_PACK, "recipes/" + recipeSubPath);
        this.modid = modid;
    }

    private static void saveRecipe(CachedOutput pOutput, JsonObject pRecipeJson, Path pPath) {
        try {
            DataProvider.saveStable(pOutput, pRecipeJson, pPath);
        } catch (IOException ioexception) {
            Theurgy.LOGGER.error("Couldn't save recipe {}", pPath, ioexception);
        }
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
    public void run(CachedOutput pOutput) throws IOException {
        Set<ResourceLocation> set = Sets.newHashSet();
        this.buildRecipes((id, recipe) -> {
            if (!set.add(id)) {
                throw new IllegalStateException("Duplicate recipe " + id);
            } else {
                saveRecipe(pOutput, recipe, this.recipePathProvider.json(id));
            }
        });
    }

    abstract void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer);
}
