// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei.recipes;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.gui.GuiSprites;
import com.klikli_dev.theurgy.content.recipe.AccumulationRecipe;
import com.klikli_dev.theurgy.integration.jei.JeiDrawables;
import com.klikli_dev.theurgy.integration.jei.JeiRecipeTypes;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
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
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class AccumulationCategory implements IRecipeCategory<RecipeHolder<AccumulationRecipe>> {
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedAnimatedArrow;

    public AccumulationCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(82, 40);

        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get()));
        this.localizedName = Component.translatable(TheurgyConstants.I18n.JEI.ACCUMULATION_CATEGORY);

        //We need different animations for different cook times, hence the cache
        this.cachedAnimatedArrow = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public @NotNull IDrawableAnimated load(@NotNull Integer cookTime) {
                        return JeiDrawables.asAnimatedDrawable(guiHelper, GuiSprites.JEI_ARROW_RIGHT_FULL, cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    public static void addFluidTooltip(IRecipeSlotsView view, List<Component> tooltip, long overrideAmount) {
        var displayed = view.getSlotViews(OUTPUT).get(0).getDisplayedIngredient(NeoForgeTypes.FLUID_STACK);
        if (displayed.isEmpty())
            return;

        var fluidStack = displayed.get();

        var amount = overrideAmount == -1 ? fluidStack.getAmount() : overrideAmount;
        var text = Component.translatable(TheurgyConstants.I18n.Misc.UNIT_MILLIBUCKETS, amount).withStyle(ChatFormatting.GOLD);
        tooltip.add(text);
    }

    protected IDrawableAnimated getAnimatedArrow(RecipeHolder<AccumulationRecipe> recipe) {
        int cookTime = recipe.value().time();
        if (cookTime <= 0) {
            cookTime = AccumulationRecipe.DEFAULT_TIME;
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
    public void draw(RecipeHolder<AccumulationRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        GuiSprites.JEI_ARROW_RIGHT_EMPTY.extractRenderState(guiGraphics, 24, 2);
        this.getAnimatedArrow(recipe).draw(guiGraphics, 24, 2);

        this.drawCookTime(recipe, guiGraphics, 29);
    }

    protected void drawCookTime(RecipeHolder<AccumulationRecipe> recipe, GuiGraphicsExtractor guiGraphics, int y) {
        int cookTime = recipe.value().time();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable(TheurgyConstants.I18n.Gui.SMELTING_TIME_SECONDS, cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            int stringWidth = font.width(timeString);
            guiGraphics.text(font, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<AccumulationRecipe> recipe, @NotNull IFocusGroup focuses) {
        if (recipe.value().hasEvaporant()) {
            builder.addSlot(INPUT, 1, 1)
                    .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                    .addIngredients(NeoForgeTypes.FLUID_STACK, recipe.value().evaporant().ingredient().fluids().stream()
                            .map(f -> new FluidStack(f.value(), recipe.value().getEvaporantAmount())).toList())
                    .setFluidRenderer(1000, false, 16, 16);
        }

        if (recipe.value().hasSolute()) {
            assert recipe.value().solute() != null;
            builder.addSlot(INPUT, 1, 21)
                    .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                    .add(recipe.value().solute());
        }

        builder.addSlot(OUTPUT, 56, 1)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .add(recipe.value().result().fluid().value(), recipe.value().result().amount());

        //now add the bucket to the recipe lookup for the output fluid
        builder.addInvisibleIngredients(OUTPUT).add(new ItemStack(recipe.value().result().fluid().value().getBucket()));
    }

    @Override
    public @NotNull IRecipeType<RecipeHolder<AccumulationRecipe>> getRecipeType() {
        return JeiRecipeTypes.ACCUMULATION;
    }

}
