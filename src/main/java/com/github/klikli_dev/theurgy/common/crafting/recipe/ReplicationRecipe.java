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
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ReplicationRecipe extends CrucibleRecipe {

    //region Fields
    public static final IRecipeSerializer<ReplicationRecipe> SERIALIZER =
            new CrucibleRecipeSerializer<>(ReplicationRecipe::new);
    //endregion Fields

    //region Initialization
    public ReplicationRecipe(ResourceLocation id, Ingredient input,
                             List<ItemStack> essentia, RecipeOutput output) {
        super(id, input, essentia, output);
    }
    //endregion Initialization

    //region Overrides
    @Override
    public ItemStack getCraftingResult(CrucibleItemStackFakeInventory inv) {
        //replication should return what it got, instead of the default tag output
        ItemStack copy = inv.getStackInSlot(0).copy();
        copy.setCount(this.getRecipeOutput().getCount());
        return copy;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeRegistry.REPLICATION_TYPE.get();
    }
    //endregion Overrides
}
