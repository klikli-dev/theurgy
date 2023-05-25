/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.jei.recipes;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.integration.jei.JeiDrawables;
import com.klikli_dev.theurgy.integration.jei.JeiRecipeTypes;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class CalcinationCategory implements IRecipeCategory<CalcinationRecipe> {
    private final IDrawableAnimated animatedFire;
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedAnimatedArrow;

    public CalcinationCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(82, 43);

        this.animatedFire = JeiDrawables.asAnimatedDrawable(guiHelper, GuiTextures.JEI_FIRE_FULL, 300, IDrawableAnimated.StartDirection.TOP, true);

        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.CALCINATION_OVEN.get()));
        this.localizedName = Component.translatable(TheurgyConstants.I18n.JEI.CALCINATION_CATEGORY);

        //We need different animations for different cook times, hence the cache
        this.cachedAnimatedArrow = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return JeiDrawables.asAnimatedDrawable(guiHelper, GuiTextures.JEI_ARROW_RIGHT_FULL, cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    protected IDrawableAnimated getAnimatedArrow(CalcinationRecipe recipe) {
        int cookTime = recipe.getCalcinationTime();
        if (cookTime <= 0) {
            cookTime = CalcinationRecipe.DEFAULT_CALCINATION_TIME;
        }
        return this.cachedAnimatedArrow.getUnchecked(cookTime);
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
    public void draw(CalcinationRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        GuiTextures.JEI_FIRE_EMPTY.render(poseStack, 1, 20);
        this.animatedFire.draw(poseStack, 1, 20);

        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(poseStack, 24, 8);
        this.getAnimatedArrow(recipe).draw(poseStack, 24, 8);

        this.drawCookTime(recipe, poseStack, 34);
    }

    protected void drawCookTime(CalcinationRecipe recipe, PoseStack poseStack, int y) {
        int cookTime = recipe.getCalcinationTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(poseStack, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CalcinationRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 1)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(recipe.getIngredients().get(0).getItems()).map(i -> i.copyWithCount(recipe.getIngredientCount())).toList());

        builder.addSlot(OUTPUT, 61, 9)
                .setBackground(JeiDrawables.OUTPUT_SLOT, -5, -5)
                .addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }

    @Override
    public RecipeType<CalcinationRecipe> getRecipeType() {
        return JeiRecipeTypes.CALCINATION;
    }
}
