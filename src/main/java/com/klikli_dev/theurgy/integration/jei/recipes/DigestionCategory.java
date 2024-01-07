// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei.recipes;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.recipe.DigestionRecipe;
import com.klikli_dev.theurgy.content.recipe.DigestionRecipe;
import com.klikli_dev.theurgy.integration.jei.JeiDrawables;
import com.klikli_dev.theurgy.integration.jei.JeiRecipeTypes;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class DigestionCategory implements IRecipeCategory<DigestionRecipe> {
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedAnimatedArrow;

    public DigestionCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(102, 43);

        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.DIGESTION_VAT.get()));
        this.localizedName = Component.translatable(TheurgyConstants.I18n.JEI.DIGESTION_CATEGORY);

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

    public static IRecipeSlotTooltipCallback addFluidTooltip(int overrideAmount) {
        return (view, tooltip) -> {
            var displayed = view.getDisplayedIngredient(ForgeTypes.FLUID_STACK);
            if (displayed.isEmpty())
                return;

            var fluidStack = displayed.get();

            var amount = overrideAmount == -1 ? fluidStack.getAmount() : overrideAmount;
            var text = Component.translatable(TheurgyConstants.I18n.Misc.UNIT_MILLIBUCKETS, amount).withStyle(ChatFormatting.GOLD);
            if (tooltip.isEmpty())
                tooltip.add(0, text);
            else {
                List<Component> siblings = tooltip.get(0).getSiblings();
                siblings.add(Component.literal(" "));
                siblings.add(text);
            }
        };
    }

    protected IDrawableAnimated getAnimatedArrow(DigestionRecipe recipe) {
        int cookTime = recipe.getTime();
        if (cookTime <= 0) {
            cookTime = DigestionRecipe.DEFAULT_TIME;
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
    public void draw(DigestionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(guiGraphics, 45, 8);
        this.getAnimatedArrow(recipe).draw(guiGraphics, 45, 8);

        this.drawCookTime(recipe, guiGraphics, 34);
    }

    protected void drawCookTime(DigestionRecipe recipe, GuiGraphics guiGraphics, int y) {
        int cookTime = recipe.getTime();
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
    public void setRecipe(IRecipeLayoutBuilder builder, DigestionRecipe recipe, IFocusGroup focuses) {
        var topLeft = builder.addSlot(INPUT, 1, 1)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1);
        var topRight = builder.addSlot(INPUT, 1 + 18, 1)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1);
        var bottomLeft = builder.addSlot(INPUT, 1, 1 + 18)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1);

        if (recipe.getIngredients().size() > 0)
            topLeft.addIngredients(recipe.getIngredients().get(0));

        if (recipe.getIngredients().size() > 1)
            topRight.addIngredients(recipe.getIngredients().get(1));

        if (recipe.getIngredients().size() > 2)
            bottomLeft.addIngredients(recipe.getIngredients().get(2));

        builder.addSlot(OUTPUT, 81, 9)
                .setBackground(JeiDrawables.OUTPUT_SLOT, -5, -5)
                .addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));

        builder.addSlot(INPUT, 1 + 18, 1 + 18)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addIngredients(ForgeTypes.FLUID_STACK, this.getFluids(recipe))
                .setFluidRenderer(1000, false, 16, 16)
                .addTooltipCallback(addFluidTooltip(recipe.getFluidAmount()));

        //now add the bucket to the recipe lookup for the output fluid
        builder.addInvisibleIngredients(INPUT).addItemStacks(Arrays.stream(recipe.getFluid().getFluids()).map(f -> new ItemStack(f.getFluid().getBucket())).toList());
    }

    public List<FluidStack> getFluids(DigestionRecipe recipe) {
        return Arrays.stream(recipe.getFluid().getFluids())
                .map(f -> {
                    var stack = f.copy();
                    f.setAmount(recipe.getFluidAmount());
                    return stack;
                }).toList();
    }

    @Override
    public RecipeType<DigestionRecipe> getRecipeType() {
        return JeiRecipeTypes.DIGESTION;
    }

}
