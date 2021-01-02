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

package com.github.klikli_dev.theurgy.datagen.recipe;

import com.github.klikli_dev.theurgy.common.crafting.recipe.RecipeOutput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class CrucibleRecipeBuilder {
    //region Fields
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    //endregion Fields

    public class Result implements IFinishedRecipe {

        //region Fields
        private final ResourceLocation id;
        private final IRecipeSerializer<?> serializer;
        private final RecipeOutput result;
        private final int count;
        private final List<ItemStack> essentia;
        private final Ingredient ingredient;
        private final String group;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;
        //endregion Fields

        //region Initialization
        public Result(ResourceLocation id, IRecipeSerializer<?> serializer, String group, RecipeOutput result,
                      int count,
                      List<ItemStack> essentia, Ingredient ingredient, Advancement.Builder advancementBuilder,
                      ResourceLocation advancementId) {
            this.id = id;
            this.serializer = serializer;
            this.group = group;
            this.result = result;
            this.count = count;
            this.essentia = essentia;
            this.ingredient = ingredient;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
        }
        //endregion Initialization

        //region Overrides
        @Override
        public void serialize(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            json.add("ingredient", this.ingredient.serialize());

            JsonArray essentiaJson = new JsonArray();
            for (ItemStack essentiaStack : this.essentia) {
                JsonObject essentiaStackjson = new JsonObject();
                essentiaStackjson.addProperty("item", ForgeRegistries.ITEMS.getKey(essentiaStack.getItem()).toString());
                essentiaStackjson.addProperty("count", essentiaStack.getCount());
                essentiaJson.add(essentiaStackjson);
            }

            json.add("essentia", essentiaJson);

            JsonObject resultJson = (JsonObject) this.result.ingredient.serialize();
            resultJson.addProperty("count", this.count);
            if (this.result.nbt != null)
                resultJson.add("nbt", GSON.toJsonTree(this.result.nbt.toString()));

            json.add("result", resultJson);
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return this.serializer;
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return this.advancementBuilder.serialize();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return this.advancementId;
        }
        //endregion Overrides
    }
}
