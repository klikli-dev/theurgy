// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei.recipes;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.recipe.DigestionRecipe;
import com.klikli_dev.theurgy.integration.jei.JeiIngredients;
import com.klikli_dev.theurgy.integration.jei.JeiRecipeTypes;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class DigestionCategory implements IRecipeCategory<RecipeHolder<DigestionRecipe>> {
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedAnimatedArrow;
    private final IDrawable emptyArrow;

    public DigestionCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(102, 43);

        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.DIGESTION_VAT.get()));
        this.localizedName = Component.translatable(TheurgyConstants.I18n.JEI.DIGESTION_CATEGORY);

        //We need different animations for different cook times, hence the cache
        this.cachedAnimatedArrow = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public @NotNull IDrawableAnimated load(@NotNull Integer cookTime) {
                        return guiHelper.createAnimatedRecipeArrow(cookTime);
                    }
                });
        this.emptyArrow = guiHelper.getRecipeArrow();
    }

    public static void addFluidTooltip(IRecipeSlotsView view, List<Component> tooltip, long overrideAmount) {
        var displayed = view.getSlotViews(INPUT).get(3).getDisplayedIngredient(NeoForgeTypes.FLUID_STACK);
        if (displayed.isEmpty())
            return;

        var fluidStack = displayed.get();

        var amount = overrideAmount == -1 ? fluidStack.getAmount() : overrideAmount;
        var text = Component.translatable(TheurgyConstants.I18n.Misc.UNIT_MILLIBUCKETS, amount).withStyle(ChatFormatting.GOLD);
        tooltip.add(text);
    }

    protected IDrawableAnimated getAnimatedArrow(RecipeHolder<DigestionRecipe> recipe) {
        int cookTime = recipe.value().getTime();
        if (cookTime <= 0) {
            cookTime = DigestionRecipe.DEFAULT_TIME;
        }
        return this.cachedAnimatedArrow.getUnchecked(cookTime);
    }

    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull Component getTitle() {
        return this.localizedName;
    }

    @Override
    public int getWidth() {
        return this.background.getWidth();
    }

    @Override
    public int getHeight() {
        return this.background.getHeight();
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(RecipeHolder<DigestionRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.emptyArrow.draw(guiGraphics, 45, 8);
        this.getAnimatedArrow(recipe).draw(guiGraphics, 45, 8);

        this.drawCookTime(recipe, guiGraphics, 34);
    }

    protected void drawCookTime(RecipeHolder<DigestionRecipe> recipe, GuiGraphicsExtractor guiGraphics, int y) {
        int cookTime = recipe.value().getTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable(TheurgyConstants.I18n.Gui.SMELTING_TIME_SECONDS, cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            int stringWidth = font.width(timeString);
            guiGraphics.text(font, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    public void addToSlot(IRecipeSlotBuilder builder, int ingredientIndex, List<SizedIngredient> ingredients) {
        if (ingredientIndex >= ingredients.size())
            return;

        var ingredient = ingredients.get(ingredientIndex);

        builder.addItemStacks(JeiIngredients.getStacks(ingredient));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<DigestionRecipe> recipe, @NotNull IFocusGroup focuses) {
        var topLeft = builder.addSlot(INPUT, 1, 1)
                .setStandardSlotBackground();
        var topRight = builder.addSlot(INPUT, 1 + 18, 1)
                .setStandardSlotBackground();
        var bottomLeft = builder.addSlot(INPUT, 1, 1 + 18)
                .setStandardSlotBackground();


        this.addToSlot(topLeft, 0, recipe.value().getSizedIngredients());
        this.addToSlot(topRight, 1, recipe.value().getSizedIngredients());
        this.addToSlot(bottomLeft, 2, recipe.value().getSizedIngredients());

        builder.addSlot(OUTPUT, 81, 9)
                .setOutputSlotBackground()
                .add(recipe.value().getResultItem(RegistryAccess.EMPTY));

        builder.addSlot(INPUT, 1 + 18, 1 + 18)
                .setStandardSlotBackground()
                .addIngredients(NeoForgeTypes.FLUID_STACK, this.getFluids(recipe))
                .setFluidRenderer(1000, false, 16, 16);

        //now add the bucket to the bucket to the recipe lookup for the input fluid
        builder.addInvisibleIngredients(INPUT).addItemStacks(recipe.value().getFluid().ingredient().fluids().stream().map(f -> new ItemStack(f.value().getBucket())).toList());
    }

    public List<FluidStack> getFluids(RecipeHolder<DigestionRecipe> recipe) {
        return recipe.value().getFluid().ingredient().fluids().stream()
                .map(f -> {
                    return new FluidStack(f.value(), recipe.value().getFluidAmount());
                }).toList();
    }

    @Override
    public @NotNull IRecipeType<RecipeHolder<DigestionRecipe>> getRecipeType() {
        return JeiRecipeTypes.DIGESTION;
    }

}
