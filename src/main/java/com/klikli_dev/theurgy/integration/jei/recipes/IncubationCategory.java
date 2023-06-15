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

import java.util.Arrays;
import java.util.stream.Collectors;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class IncubationCategory implements IRecipeCategory<IncubationRecipe> {
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

    protected IDrawableAnimated getAnimatedArrow(IncubationRecipe recipe) {
        int cookTime = recipe.getIncubationTime();
        if (cookTime <= 0) {
            cookTime = IncubationRecipe.DEFAULT_INCUBATION_TIME;
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
    public void draw(IncubationRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        GuiTextures.JEI_FIRE_EMPTY.render(guiGraphics, 28, 44);
        this.animatedFire.draw(guiGraphics, 28, 44);

        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(guiGraphics, 24, 22);
        this.getAnimatedArrow(recipe).draw(guiGraphics, 24, 22);

        this.drawCookTime(recipe, guiGraphics, 47);
    }

    protected void drawCookTime(IncubationRecipe recipe, GuiGraphics guiGraphics, int y) {
        int cookTime = recipe.getIncubationTime();
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
    public void setRecipe(IRecipeLayoutBuilder builder, IncubationRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 1)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addIngredients(recipe.getMercury());

        //sulfurs in the recipe are in most cases specified only via the item id (as one sulfur = one item), but for rendering we need the nbt, so we get it from the corresponding recipe.
        var recipeManager = Minecraft.getInstance().level.getRecipeManager();
        var liquefactionRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get()).stream().filter(r -> r.getResultItem(Minecraft.getInstance().level.registryAccess()) != null).collect(Collectors.toList());

        var sulfurIngredients = Arrays.stream(recipe.getSulfur().getItems()).map(sulfur -> {
            if (sulfur.hasTag())
                return sulfur;

            var sulfurItem = sulfur.getItem();
            var sulfurWithNbt = liquefactionRecipes.stream()
                    .filter(r -> r.getResultItem(Minecraft.getInstance().level.registryAccess()).getItem() == sulfurItem).map(r -> r.getResultItem(Minecraft.getInstance().level.registryAccess())).findFirst();

            if (sulfurWithNbt.isPresent()) {
                sulfur = sulfur.copy();
                sulfur.setTag(sulfurWithNbt.get().getTag());
            }

            return sulfur;
        }).toList();

        builder.addSlot(INPUT, 1, 21)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addItemStacks(sulfurIngredients);

        builder.addSlot(INPUT, 1, 42)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addIngredients(recipe.getSalt());

        builder.addSlot(OUTPUT, 61, 22)
                .setBackground(JeiDrawables.OUTPUT_SLOT, -5, -5)
                .addItemStacks(Arrays.asList(recipe.getResult().getStacks()));
    }

    @Override
    public RecipeType<IncubationRecipe> getRecipeType() {
        return JeiRecipeTypes.INCUBATION;
    }
}
