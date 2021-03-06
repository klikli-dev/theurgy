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

package com.github.klikli_dev.theurgy.integration.jei.recipes;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.crafting.recipe.EssentiaRecipe;
import com.github.klikli_dev.theurgy.registry.BlockRegistry;
import com.github.klikli_dev.theurgy.registry.RecipeRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class EssentiaRecipeCategory implements IRecipeCategory<EssentiaRecipe> {

    //region Fields
    private final IDrawable background;
    private final IDrawable icon;
    private final ItemStack renderStack = new ItemStack(BlockRegistry.CRUCIBLE.get());
    //endregion Fields

    //region Initialization
    public EssentiaRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(168, 86);
        this.icon = guiHelper.createDrawableIngredient(this.renderStack);
        this.renderStack.getOrCreateTag().putBoolean("RenderFull", true);
    }
    //endregion Initialization

    //region Overrides
    @Override
    public ResourceLocation getUid() {
        return RecipeRegistry.ESSENTIA.getId();
    }

    @Override
    public Class<? extends EssentiaRecipe> getRecipeClass() {
        return EssentiaRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("jei." + Theurgy.MODID + ".essentia_recipe");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(EssentiaRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getEssentia());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, EssentiaRecipe recipe, IIngredients ingredients) {
        int index = 0;
        int iconWidth = this.icon.getWidth();
        int x = this.background.getWidth() / 2 - iconWidth / 2;
        int y = 12;
        recipeLayout.getItemStacks().init(index, true, x, y);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        index++;
        y += 10;

        recipeLayout.getItemStacks().init(index, true, x, y);
        recipeLayout.getItemStacks().set(index, this.renderStack);
        index++;
        y += 20;


        List<List<ItemStack>> essentia = ingredients.getOutputs(VanillaTypes.ITEM);
        int essentiaSlotOffset = 10;
        if (essentia.size() > 1)
            x = x - (essentia.size() * (iconWidth + essentiaSlotOffset)) / 2 + (iconWidth + essentiaSlotOffset) / 2;
        for (int i = 0; i < essentia.size(); i++) {
            recipeLayout.getItemStacks().init(index + i, false, x + i * (iconWidth + essentiaSlotOffset), y);
            recipeLayout.getItemStacks().set(index + i, essentia.get(i));
        }
        ;
    }

    //endregion Overrides
}
