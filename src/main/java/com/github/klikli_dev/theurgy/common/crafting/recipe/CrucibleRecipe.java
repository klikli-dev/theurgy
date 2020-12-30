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

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CrucibleRecipe implements IRecipe<CrucibleItemStackFakeInventory> {
    //region Fields

    protected final ResourceLocation id;
    protected final Ingredient input;
    protected final RecipeOutput output;

    protected final List<ItemStack> essentia;
    protected final NonNullList<Ingredient> ingredients;
    //endregion Fields

    //region Initialization
    public CrucibleRecipe(ResourceLocation id, Ingredient input, List<ItemStack> essentia,
                          RecipeOutput output) {
        this.id = id;
        this.input = input;
        this.essentia = essentia;
        this.output = output;
        List<Ingredient> allIngredients = new ArrayList<>();
        allIngredients.add(input); //first slot is "trigger" item
        //remainder is essentia
        allIngredients.addAll(this.essentia.stream().map(Ingredient::fromStacks).collect(Collectors.toList()));
        this.ingredients = NonNullList.from(Ingredient.EMPTY, allIngredients.stream().toArray(Ingredient[]::new));
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
        return this.output.getStack();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    //endregion Overrides
}