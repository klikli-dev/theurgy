/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.klikli_dev.theurgy.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.data.grafting_hedges.GraftingHedgeData;
import com.klikli_dev.theurgy.data.grafting_hedges.GraftingHedgeManager;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GraftingHedgeGenerator implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<String, JsonElement> toSerialize = new HashMap<>();
    private final DataGenerator generator;

    public GraftingHedgeGenerator(DataGenerator generator) {
        this.generator = generator;
    }

    private void start() {
        this.add("apple", Ingredient.of(Items.APPLE), new ItemStack(Items.APPLE));
        this.add("melon_slice", Ingredient.of(Items.MELON_SLICE), new ItemStack(Items.MELON_SLICE));
        this.add("sweet_berries", Ingredient.of(Items.SWEET_BERRIES), new ItemStack(Items.SWEET_BERRIES));
    }

    private void add(String path, Ingredient itemToGraft, ItemStack itemToGrow) {
        add(path, itemToGraft, itemToGrow, null);
    }

    private void addForMod(String modId, String path,  JsonObject itemToGraft, JsonObject itemToGrow){
        this.addForMod(path, itemToGraft, itemToGrow, new ModLoadedCondition(modId));
    }

    private void addForMod(String path, JsonObject itemToGraft, JsonObject itemToGrow, ICondition condition) {
        ResourceLocation id = Theurgy.id(path);

        JsonObject json = new JsonObject();
        json.add("item_to_graft", itemToGraft);
        json.add("item_to_grow", itemToGrow);
        if (condition != null) {
            json.add("condition", CraftingHelper.serialize(condition));
        }
        this.toSerialize.put(id.getPath(), json);
    }

    private void add(String path, Ingredient itemToGraft, ItemStack itemToGrow, ICondition condition) {
        ResourceLocation id = Theurgy.id(path);
        JsonObject json = new GraftingHedgeData(id, itemToGraft, itemToGrow, condition).toJson();
        this.toSerialize.put(id.getPath(), json);
    }

    private JsonObject itemStack(ResourceLocation item, int count, CompoundTag tag) {
        JsonObject json = new JsonObject();
        json.addProperty("item", item.toString());
        json.addProperty("count", count);
        if (tag != null)
            json.add("tag", CompoundTag.CODEC.encodeStart(JsonOps.INSTANCE, tag).get().orThrow());
        return json;
    }

    private JsonObject itemIngredient(ResourceLocation item) {
        JsonObject json = new JsonObject();
        json.addProperty("item", item.toString());
        return json;
    }

    private JsonObject tagIngredient(ResourceLocation tag) {
        JsonObject json = new JsonObject();
        json.addProperty("tag", tag.toString());
        return json;
    }

    private ResourceLocation rl(String loc){
        return new ResourceLocation(loc);
    }

    @Override
    public void run(HashCache cache) throws IOException {
        Path folder = this.generator.getOutputFolder();
        this.start();

        this.toSerialize.forEach((name, json) -> {
            Path path = folder.resolve("data/" + Theurgy.MODID + "/" + GraftingHedgeManager.FOLDER + "/" + name + ".json");
            try {
                DataProvider.save(GSON, cache, json, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save pentacle {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "Grafting Hedges:" + Theurgy.MODID;
    }

}
