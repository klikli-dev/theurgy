// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei.recipes;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.integration.jei.JeiDrawables;
import com.klikli_dev.theurgy.integration.jei.JeiRecipeTypes;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class LiquefactionCategory implements IRecipeCategory<RecipeHolder<LiquefactionRecipe>> {
    private final IDrawableAnimated animatedFire;
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedAnimatedArrow;

    public LiquefactionCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(102, 43);

        this.animatedFire = JeiDrawables.asAnimatedDrawable(guiHelper, GuiTextures.JEI_FIRE_FULL, 300, IDrawableAnimated.StartDirection.TOP, true);

        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.LIQUEFACTION_CAULDRON.get()));
        this.localizedName = Component.translatable(TheurgyConstants.I18n.JEI.LIQUEFACTION_CATEGORY);

        //We need different animations for different cook times, hence the cache
        this.cachedAnimatedArrow = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public @NotNull IDrawableAnimated load(@NotNull Integer cookTime) {
                        return JeiDrawables.asAnimatedDrawable(guiHelper, GuiTextures.JEI_ARROW_RIGHT_FULL, cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    public static IRecipeSlotTooltipCallback addFluidTooltip(int overrideAmount) {
        return (view, tooltip) -> {
            var displayed = view.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK);
            if (displayed.isEmpty())
                return;

            var fluidStack = displayed.get();

            var amount = overrideAmount == -1 ? fluidStack.getAmount() : overrideAmount;
            var text = Component.translatable(TheurgyConstants.I18n.Misc.UNIT_MILLIBUCKETS, amount).withStyle(ChatFormatting.GOLD);

            tooltip.add(text);
        };
    }

    protected IDrawableAnimated getAnimatedArrow(RecipeHolder<LiquefactionRecipe> recipe) {
        int cookTime = recipe.value().getTime();
        if (cookTime <= 0) {
            cookTime = LiquefactionRecipe.DEFAULT_TIME;
        }
        return this.cachedAnimatedArrow.getUnchecked(cookTime);
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(@NotNull RecipeHolder<LiquefactionRecipe> recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        GuiTextures.JEI_FIRE_EMPTY.render(guiGraphics, 12, 20);
        this.animatedFire.draw(guiGraphics, 12, 20);

        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(guiGraphics, 45, 8);
        this.getAnimatedArrow(recipe).draw(guiGraphics, 45, 8);

        this.drawCookTime(recipe, guiGraphics, 34);
    }

    protected void drawCookTime(RecipeHolder<LiquefactionRecipe> recipe, GuiGraphics guiGraphics, int y) {
        int cookTime = recipe.value().getTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable(TheurgyConstants.I18n.Gui.SMELTING_TIME_SECONDS, cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            int stringWidth = font.width(timeString);
            guiGraphics.drawString(font, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    @Override
    public @NotNull Component getTitle() {
        return this.localizedName;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, @NotNull RecipeHolder<LiquefactionRecipe> recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 1)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addIngredients(NeoForgeTypes.FLUID_STACK, this.getFluids(recipe))
                .setFluidRenderer(1000, false, 16, 16)
                .addTooltipCallback(addFluidTooltip(recipe.value().getSolventAmount()));

        builder.addSlot(INPUT, 19, 1)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addIngredients(recipe.value().getIngredients().getFirst());
        builder.addSlot(OUTPUT, 81, 9)
                .setBackground(JeiDrawables.OUTPUT_SLOT, -5, -5)
                .addItemStack(recipe.value().getResultItem(RegistryAccess.EMPTY));

        //now add the bucket to the recipe lookup for the output fluid
        builder.addInvisibleIngredients(INPUT).addItemStacks(Arrays.stream(recipe.value().getSolvent().getFluids()).map(f -> new ItemStack(f.getFluid().getBucket())).toList());
    }

    public List<FluidStack> getFluids(RecipeHolder<LiquefactionRecipe> recipe) {
        return Arrays.stream(recipe.value().getSolvent().getFluids())
                .map(f -> {
                    var stack = f.copy();
                    f.setAmount(recipe.value().getSolventAmount());
                    return stack;
                }).toList();
    }

    @Override
    public @NotNull RecipeType<RecipeHolder<LiquefactionRecipe>> getRecipeType() {
        return JeiRecipeTypes.LIQUEFACTION;
    }

}
