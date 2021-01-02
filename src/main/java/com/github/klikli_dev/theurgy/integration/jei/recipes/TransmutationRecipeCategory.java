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
import com.github.klikli_dev.theurgy.common.crafting.recipe.TransmutationRecipe;
import com.github.klikli_dev.theurgy.registry.ItemRegistry;
import com.github.klikli_dev.theurgy.registry.RecipeRegistry;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TransmutationRecipeCategory extends CrucibleRecipeCategory<TransmutationRecipe> {
    //region Initialization
    public TransmutationRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, new ItemStack(ItemRegistry.PRIMA_MATERIA_CRYSTAL_STIRRER.get()));
    }
    //endregion Initialization

    //region Overrides
    @Override
    public ResourceLocation getUid() {
        return RecipeRegistry.TRANSMUTATION.getId();
    }

    @Override
    public Class<? extends TransmutationRecipe> getRecipeClass() {
        return TransmutationRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("jei." + Theurgy.MODID + ".transmutation_recipe");
    }
    //endregion Overrides
}
