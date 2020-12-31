/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
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

package com.github.klikli_dev.theurgy.common.crafting.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class CrucibleRecipeSerializer<T extends CrucibleRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

    //region Fields
    public ICrucibleRecipeFactory<T> factory;
    //endregion Fields

    //region Initialization
    public CrucibleRecipeSerializer(
            ICrucibleRecipeFactory<T> factory) {
        this.factory = factory;
    }
    //endregion Initialization

    //region Overrides
    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {
        JsonElement ingredientElement = JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json,
                "ingredient") : JSONUtils.getJsonObject(json, "ingredient");
        Ingredient ingredient = Ingredient.deserialize(ingredientElement);

        List<ItemStack> essentia = RecipeJsonHelper.readItemStackArray(JSONUtils.getJsonArray(json, "essentia"));
        if (essentia.isEmpty()) {
            throw new JsonParseException("No essentia specified for purification recipe");
        }

        RecipeOutput result = RecipeOutput.fromJson(JSONUtils.getJsonObject(json, "result"));

        return this.factory.create(recipeId, ingredient, essentia, result);
    }

    @Override
    public T read(ResourceLocation recipeId, PacketBuffer buffer) {

        Ingredient ingredient = Ingredient.read(buffer);
        RecipeOutput result = RecipeOutput.read(buffer);

        List<ItemStack> essentia = new ArrayList<>();

        int length = buffer.readVarInt();
        for (int i = 0; i < length; i++) {
            essentia.add(buffer.readItemStack());
        }

        return this.factory.create(recipeId, ingredient, essentia, result);
    }

    @Override
    public void write(PacketBuffer buffer, T recipe) {
        recipe.input.write(buffer);
        recipe.output.write(buffer);
        buffer.writeVarInt(recipe.essentia.size());
        recipe.essentia.forEach(buffer::writeItemStack);
    }
    //endregion Overrides

    public interface ICrucibleRecipeFactory<T> {
        //region Methods
        T create(ResourceLocation id, Ingredient input,
                 List<ItemStack> essentia, RecipeOutput output);
        //endregion Methods
    }
}