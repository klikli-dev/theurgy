// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei.recipes;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.integration.jei.JeiDrawables;
import com.klikli_dev.theurgy.integration.jei.JeiRecipeTypes;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class IncubationCategory implements IRecipeCategory<RecipeHolder<IncubationRecipe>> {
    private final IDrawableAnimated animatedFire;
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedAnimatedArrow;

    public IncubationCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(82, 60);

        this.animatedFire = JeiDrawables.asAnimatedDrawable(guiHelper, GuiTextures.JEI_FIRE_FULL, 300, IDrawableAnimated.StartDirection.TOP, true);

        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.INCUBATOR.get()));
        this.localizedName = Component.translatable(TheurgyConstants.I18n.JEI.INCUBATION_CATEGORY);

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

    protected IDrawableAnimated getAnimatedArrow(RecipeHolder<IncubationRecipe> recipe) {
        int cookTime = recipe.value().getTime();
        if (cookTime <= 0) {
            cookTime = IncubationRecipe.DEFAULT_TIME;
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
    public void draw(RecipeHolder<IncubationRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        GuiTextures.JEI_FIRE_EMPTY.render(guiGraphics, 28, 44);
        this.animatedFire.draw(guiGraphics, 28, 44);

        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(guiGraphics, 24, 22);
        this.getAnimatedArrow(recipe).draw(guiGraphics, 24, 22);

        this.drawCookTime(recipe, guiGraphics, 47);
    }

    protected void drawCookTime(RecipeHolder<IncubationRecipe> recipe, GuiGraphics guiGraphics, int y) {
        int cookTime = recipe.value().getTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            int stringWidth = font.width(timeString);
            guiGraphics.drawString(font, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<IncubationRecipe> recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 1)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addIngredients(recipe.value().getMercury());

        builder.addSlot(INPUT, 1, 21)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addItemStacks(Arrays.asList(recipe.value().getSulfur().getItems()));

        builder.addSlot(INPUT, 1, 42)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addIngredients(recipe.value().getSalt());

        builder.addSlot(OUTPUT, 61, 22)
                .setBackground(JeiDrawables.OUTPUT_SLOT, -5, -5)
                .addItemStacks(Arrays.asList(recipe.value().getResult().getStacks()));
    }

    @Override
    public RecipeType<RecipeHolder<IncubationRecipe>> getRecipeType() {
        return JeiRecipeTypes.INCUBATION;
    }
}
