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

package com.github.klikli_dev.theurgy.client.gui.patchouli;

import com.github.klikli_dev.theurgy.common.crafting.recipe.CrucibleRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class CrucibleRecipeProcessor implements IComponentProcessor {

    //region Fields
    protected CrucibleRecipe recipe;
    //endregion Fields

    //region Overrides
    @Override
    public void setup(IVariableProvider variables) {
        String recipeId = variables.get("recipe").asString();
        this.recipe = (CrucibleRecipe) Minecraft.getInstance().world.getRecipeManager()
                                               .getRecipe(new ResourceLocation(recipeId))
                                               .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public IVariable process(String key) {
        if (key.equals("input")) {
            Ingredient ingredient = this.recipe.getIngredients().get(0);
            ItemStack[] stacks = ingredient.getMatchingStacks();
            ItemStack stack = stacks.length == 0 ? ItemStack.EMPTY : stacks[0];
            return IVariable.from(stack);
        }

        if (key.equals("output")) {
            ItemStack stack = this.recipe.getRecipeOutput();
            return IVariable.from(stack);
        }

        if (key.startsWith("essentia")) {
            int index = Integer.parseInt(key.substring("essentia".length())) - 1;
            ItemStack essentia = recipe.getEssentia().get(index);
            return IVariable.from(essentia);
        }

        return null;
    }
    //endregion Overrides
}
