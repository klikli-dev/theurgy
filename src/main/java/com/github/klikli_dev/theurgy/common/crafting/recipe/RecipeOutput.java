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

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

public class RecipeOutput {

    //region Fields
    public Ingredient ingredient;
    public int count;
    public CompoundNBT nbt;

    protected ItemStack stack;
    //endregion Fields

    //region Initialization
    public RecipeOutput(Ingredient ingredient, int count, CompoundNBT nbt) {
        this.ingredient = ingredient;
        this.count = count;
        this.nbt = nbt;
        this.stack = null;
    }
    //endregion Initialization

    //region Getter / Setter
    public ItemStack getStack() {
        if (this.stack == null) {
            ItemStack[] matchingStacks = this.ingredient.getMatchingStacks();
            if (matchingStacks.length > 0) {
                this.stack = matchingStacks[0].copy();
                this.stack.setCount(this.count);
                if (this.nbt != null)
                    this.stack.setTag(this.nbt);
            }
            else {
                //if no matching stacks found, return empty stack
                this.stack = ItemStack.EMPTY;
            }
        }
        return this.stack;
    }
    //endregion Getter / Setter

    //region Static Methods

    /**
     * Deserializes a RecipeOutput from Json.
     * Json object needs an ingredient and optionally a count field.
     *
     * @param json the json object.
     * @return the recipe output.
     */
    public static RecipeOutput fromJson(JsonObject json) {
        Ingredient ingredient = Ingredient.deserialize(json);
        int count = JSONUtils.getInt(json, "count", 1);
        CompoundNBT nbt = json.has("nbt") ? RecipeJsonHelper.readNBT(json.get("nbt")) : null;
        return new RecipeOutput(ingredient, count, nbt);
    }

    public static RecipeOutput read(PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.read(buffer);
        int count = buffer.readVarInt();
        CompoundNBT nbt = buffer.readBoolean() ? buffer.readCompoundTag() : null;
        return new RecipeOutput(ingredient, count, nbt);
    }
    //endregion Static Methods

    //region Methods
    public void write(PacketBuffer buffer) {
        this.ingredient.write(buffer);
        buffer.writeVarInt(this.count);
        buffer.writeBoolean(this.nbt != null);
        if (this.nbt != null)
            buffer.writeCompoundTag(this.nbt);
    }
    //endregion Methods
}
