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

package com.klikli_dev.theurgy.integration.jei.categories;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.data.grafting_hedges.GraftingHedgeData;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class GraftingHedgeHarvestCategory implements IRecipeCategory<GraftingHedgeData> {

    private final ResourceLocation id = new ResourceLocation(Theurgy.MODID, "grafting_hedge_harvest");
    private final IDrawable background;
    private final Component localizedName;
    private final IDrawable overlay;
    private final IDrawable icon;
    private final ItemStack renderStack = new ItemStack(ItemRegistry.GRAFTING_HEDGE.get());
    //endregion Fields

    //region Initialization
    public GraftingHedgeHarvestCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(168, 46); //64
        this.localizedName = new TranslatableComponent(Theurgy.MODID + ".jei.spirit_fire");
        this.overlay = guiHelper.createDrawable(
                new ResourceLocation(Theurgy.MODID, "textures/gui/jei/spirit_fire.png"), 0, 0, 64, 46);
        this.icon = guiHelper.createDrawableIngredient(this.renderStack);
        this.renderStack.getOrCreateTag().putBoolean("RenderFull", true);
    }
    //endregion Initialization

    //region Overrides
    @Override
    public ResourceLocation getUid() {
        return id;
    }

    @Override
    public Class<? extends GraftingHedgeData> getRecipeClass() {
        return GraftingHedgeData.class;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
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
    public void setIngredients(GraftingHedgeData recipe, IIngredients ingredients) {
//        ingredients.setInputIngredients(recipe.getIngredients());
//        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, GraftingHedgeData recipe, IIngredients ingredients) {
        int index = 0;

        recipeLayout.getItemStacks().init(index, true, 40, 12);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        index++;

        recipeLayout.getItemStacks().init(index, true, 75, 12);
        recipeLayout.getItemStacks().set(index, this.renderStack);
        index++;

        recipeLayout.getItemStacks().init(index, false, 110, 12);
        recipeLayout.getItemStacks().set(index, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }

    @Override
    public void draw(GraftingHedgeData recipe, PoseStack poseStack, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        this.overlay.draw(poseStack, 48, 0);
    }
    //endregion Overrides
}
