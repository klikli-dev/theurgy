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

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.ArrayList;
import java.util.List;

public class RecipeJsonHelper {
    //region Fields
    //Same Gson settings as in CraftingHelper
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    //endregion Fields

    //region Static Methods

    /**
     * Reads a json array of item stacks
     *
     * @param itemStackArray the item stack json array
     * @return a list of item stacks.
     */
    public static List<ItemStack> readItemStackArray(JsonArray itemStackArray) {
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < itemStackArray.size(); ++i) {
            ItemStack stack = CraftingHelper.getItemStack(itemStackArray.get(i).getAsJsonObject(), false);
            list.add(stack);
        }

        return list;
    }

    /**
     * Reads an nbt compound from a given json element. Supports json nbt object and json nbt string.
     * Based on {@link CraftingHelper#getItemStack(JsonObject, boolean)}'s nbt reading logic
     *
     * @param element the json element to read.
     * @return the compound.
     */
    public static CompoundNBT readNBT(JsonElement element) {
        try {
            CompoundNBT nbt;
            if (element.isJsonObject())
                nbt = JsonToNBT.getTagFromJson(GSON.toJson(element));
            else
                nbt = JsonToNBT.getTagFromJson(JSONUtils.getString(element, "nbt"));

            if (nbt.contains("ForgeCaps")) {
                nbt.remove("ForgeCaps");
            }
            return nbt;
        } catch (CommandSyntaxException e) {
            throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
        }
    }
    //endregion Static Methods
}
