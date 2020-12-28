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

import com.github.klikli_dev.theurgy.registry.RecipeRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class CrucibleRecipe implements IRecipe<CrucibleItemStackFakeInventory> {
    //region Fields
    public static Serializer SERIALIZER = new Serializer();

    protected final ResourceLocation id;
    protected final Ingredient input;
    protected final ItemStack output;

    protected final List<ItemStack> essentia;
    //endregion Fields

    //region Initialization
    public CrucibleRecipe(ResourceLocation id, Ingredient input, List<ItemStack> essentia,
                          ItemStack output) {
        this.id = id;
        this.input = input;
        this.essentia = essentia;
        this.output = output;
    }
    //endregion Initialization

    //region Getter / Setter
    public List<ItemStack> getEssentia() {
        return this.essentia;
    }
    //endregion Getter / Setter

    //region Overrides
    @Override
    public boolean matches(CrucibleItemStackFakeInventory inv, World worldIn) {
        //match the trigger ingredient
        if (!this.input.test(inv.getStackInSlot(0)))
            return false;

        //match each essentia, exit if any essentia is too little
        for (ItemStack essentia : this.essentia) {
            if (inv.essentiaCache.get(essentia.getItem()) < essentia.getCount())
                return false;
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(CrucibleItemStackFakeInventory inv) {
        return this.getRecipeOutput().copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.from(Ingredient.EMPTY, this.input);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeRegistry.CRUCIBLE_TYPE.get();
    }
    //endregion Overrides

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrucibleRecipe> {

        //region Overrides
        @Override
        public CrucibleRecipe read(ResourceLocation recipeId, JsonObject json) {
            JsonElement ingredientElement = JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json,
                    "ingredient") : JSONUtils.getJsonObject(json, "ingredient");
            Ingredient ingredient = Ingredient.deserialize(ingredientElement);

            List<ItemStack> essentia = readEssentiaList(JSONUtils.getJsonArray(json, "essentia"));
            if (essentia.isEmpty()) {
                throw new JsonParseException("No essentia specified for crucible recipe");
            }
            ItemStack result = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "result"), true);

            return new CrucibleRecipe(recipeId, ingredient, essentia, result);
        }

        @Override
        public CrucibleRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {

            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();

            List<ItemStack> essentia = new ArrayList<>();

            int length = buffer.readVarInt();
            for (int i = 0; i < length; i++) {
                essentia.add(buffer.readItemStack());
            }

            return new CrucibleRecipe(recipeId, ingredient, essentia, result);
        }

        @Override
        public void write(PacketBuffer buffer, CrucibleRecipe recipe) {
            recipe.input.write(buffer);
            buffer.writeItemStack(recipe.output);
            buffer.writeVarInt(recipe.essentia.size());
            recipe.essentia.forEach(buffer::writeItemStack);
        }
        //endregion Overrides

        //region Static Methods
        private static List<ItemStack> readEssentiaList(JsonArray essentiaArray) {
            List<ItemStack> list = new ArrayList<>();

            for (int i = 0; i < essentiaArray.size(); ++i) {
                ItemStack stack = CraftingHelper.getItemStack(essentiaArray.get(i).getAsJsonObject(), false);
                list.add(stack);
            }

            return list;
        }
        //endregion Static Methods
    }
}