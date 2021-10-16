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

import com.google.common.collect.ImmutableList;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.api.TheurgyConstants;
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
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collections;
import java.util.List;

public class GraftingHedgeGraftingCategory implements IRecipeCategory<GraftingHedgeData> {

    public static final ResourceLocation ID = new ResourceLocation(Theurgy.MODID, "grafting_hedge_grat");
    private final IDrawable background;
    private final Component localizedName;
    private final IDrawable rightArrow;
    private final IDrawable plus;
    private final IDrawable icon;
    private final ItemStack hedge;

    public GraftingHedgeGraftingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(168, 46);
        this.localizedName = new TranslatableComponent(TheurgyConstants.I18n.JEI_GRAFTING_HEDGE_GRAFTING_CATEGORY);
        this.rightArrow = guiHelper
                .drawableBuilder(Theurgy.id("textures/gui/jei/arrow_right.png"), 0, 0, 32, 32)
                .setTextureSize(32, 32).build();
        this.plus = guiHelper
                .drawableBuilder(Theurgy.id("textures/gui/jei/plus.png"), 0, 0, 32, 32)
                .setTextureSize(32, 32).build();
        this.hedge = new ItemStack(ItemRegistry.GRAFTING_HEDGE.get());
        this.hedge.getOrCreateTag().putBoolean("RenderFull", true);
        this.icon = guiHelper.createDrawableIngredient(this.hedge);

    }

    @Override
    public ResourceLocation getUid() {
        return ID;
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
        Ingredient graftingHedge = Ingredient.of(new ItemStack(ItemRegistry.GRAFTING_HEDGE.get()));
        ingredients.setInputIngredients(ImmutableList.of(recipe.itemToGraft, graftingHedge));

        ItemStack specificHedge = new ItemStack(ItemRegistry.GRAFTING_HEDGE.get());
        specificHedge.getOrCreateTagElement("BlockEntityTag")
                .putString(TheurgyConstants.Nbt.GRAFTING_HEDGE_DATA, recipe.id.toString());

        ingredients.setOutput(VanillaTypes.ITEM, specificHedge);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, GraftingHedgeData recipe, IIngredients ingredients) {
        int index = 0;

        recipeLayout.getItemStacks().init(index, true, 15, 12);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(VanillaTypes.ITEM).get(1));
        index++;

        recipeLayout.getItemStacks().init(index, true, 60, 12);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        index++;

        recipeLayout.getItemStacks().init(index, false, 115, 12);
        recipeLayout.getItemStacks().set(index, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }

    @Override
    public void draw(GraftingHedgeData recipe, PoseStack poseStack, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        this.plus.draw(poseStack, 30, 6);
        this.rightArrow.draw(poseStack, 80  , 6);
        RenderSystem.disableBlend();
    }

    @Override
    public List<Component> getTooltipStrings(GraftingHedgeData recipe, double mouseX, double mouseY) {
        if (mouseX > 30 - 32 / 2.0f && mouseX < 30 + 32 && mouseY > 6 && mouseY < 6 + 32 || //over plus
                mouseX > 80 - 32 / 2.0f && mouseX < 80 + 32 && mouseY > 6 && mouseY < 6 + 32 //over arrow
        )
            return ImmutableList.of(new TranslatableComponent(TheurgyConstants.I18n.JEI_GRAFTING_HEDGE_GRAFTING_CATEGORY_TOOLTIP));
        return Collections.emptyList();
    }
}
