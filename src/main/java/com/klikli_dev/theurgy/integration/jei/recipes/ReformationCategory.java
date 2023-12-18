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
import com.klikli_dev.theurgy.content.recipe.ReformationRecipe;
import com.klikli_dev.theurgy.integration.jei.JeiDrawables;
import com.klikli_dev.theurgy.integration.jei.JeiRecipeTypes;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.stream.Collectors;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class ReformationCategory implements IRecipeCategory<ReformationRecipe> {
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedAnimatedArrow;

    public ReformationCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(180, 100);

        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get()));
        this.localizedName = Component.translatable(TheurgyConstants.I18n.JEI.REFORMATION_CATEGORY);

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

    protected IDrawableAnimated getAnimatedArrow(ReformationRecipe recipe) {
        int cookTime = recipe.getReformationTime();
        if (cookTime <= 0) {
            cookTime = ReformationRecipe.DEFAULT_REFORMATION_TIME;
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
    public void draw(ReformationRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(guiGraphics, 90, 19);
        this.getAnimatedArrow(recipe).draw(guiGraphics, 90, 19);

        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(guiGraphics, 19, 19);

        this.drawCookTime(recipe, guiGraphics, 57);
        this.drawFlux(recipe, guiGraphics, 77);
        this.drawSourcePedestalCount(recipe, guiGraphics, 57);
    }

    protected void drawCookTime(ReformationRecipe recipe, GuiGraphics guiGraphics, int y) {
        int cookTime = recipe.getReformationTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            int stringWidth = font.width(timeString);
            guiGraphics.drawString(font, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    protected void drawFlux(ReformationRecipe recipe, GuiGraphics guiGraphics, int y) {
        int flux = recipe.getMercuryFlux();
        Component timeString =  Component.translatable(TheurgyConstants.I18n.JEI.MERCURY_FLUX, flux);
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        guiGraphics.drawString(font, timeString, 1, y, 0xFF808080, false);
    }

    protected void drawSourcePedestalCount(ReformationRecipe recipe, GuiGraphics guiGraphics, int y) {
        int count = recipe.getSources().size();
        Component timeString =  Component.translatable(TheurgyConstants.I18n.JEI.SOURCE_PEDESTAL_COUNT, count);
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        int stringWidth = font.width(timeString);
        guiGraphics.drawString(font, timeString, 58 - stringWidth, y, 0xFF808080, false);
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ReformationRecipe recipe, IFocusGroup focuses) {

        //TODO: draw the emitter
        //TODO: draw the required flux
        //TODO: draw target pedestal + target sulfur
        //TODO: draw source pedestal + up to six slots
        //TODO: draw result pedestal + result sulfur

        builder.addSlot(RecipeIngredientRole.CATALYST, 1, 17)
                .addItemStack(new ItemStack(ItemRegistry.SULFURIC_FLUX_EMITTER.get()));


        builder.addSlot(RecipeIngredientRole.CATALYST, 19, 19)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addIngredients(recipe.getTarget());
        builder.addSlot(RecipeIngredientRole.CATALYST, 19, 35)
                .addItemStack(new ItemStack(ItemRegistry.REFORMATION_TARGET_PEDESTAL.get()));

        //6 source slots, 2 columns, 3 rows
        int sourceSlotX = 50;
        int sourceSlotY = 37; // Start from the bottom

        for (int i = 0; i < 6; i++) {
            var slot = builder.addSlot(RecipeIngredientRole.CATALYST, sourceSlotX, sourceSlotY).setBackground(JeiDrawables.INPUT_SLOT, -1, -1);

            if(i < recipe.getSources().size())
                slot.addIngredients(recipe.getSources().get(i));

            sourceSlotY -= 18; // Move upwards
         if (i % 3 == 2) {
                sourceSlotY = 37; // Reset to the bottom
                sourceSlotX += 18; // Move to the right
            }
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, 59, 55)
                .addItemStack(new ItemStack(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get()));


        builder.addSlot(RecipeIngredientRole.CATALYST, 135, 17)
                .setBackground(JeiDrawables.OUTPUT_SLOT, -4, -4)
                .addIngredients(recipe.getTarget());
        builder.addSlot(RecipeIngredientRole.CATALYST, 135, 41)
                .addItemStack(new ItemStack(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get()));
    }

    @Override
    public RecipeType<ReformationRecipe> getRecipeType() {
        return JeiRecipeTypes.REFORMATION;
    }
}
