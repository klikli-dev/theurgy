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
import com.klikli_dev.theurgy.integration.jei.JeiDrawables;
import com.klikli_dev.theurgy.integration.jei.JeiRecipeTypes;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
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
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class DigestionCategory implements IRecipeCategory<RecipeHolder<DigestionRecipe>> {
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
                    public @NotNull IDrawableAnimated load(@NotNull Integer cookTime) {
                        return JeiDrawables.asAnimatedDrawable(guiHelper, GuiTextures.JEI_ARROW_RIGHT_FULL, cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    public static IRecipeSlotRichTooltipCallback addFluidTooltip(int overrideAmount) {
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

    protected IDrawableAnimated getAnimatedArrow(RecipeHolder<DigestionRecipe> recipe) {
        int cookTime = recipe.value().getTime();
        if (cookTime <= 0) {
            cookTime = DigestionRecipe.DEFAULT_TIME;
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
    public void draw(@NotNull RecipeHolder<DigestionRecipe> recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(guiGraphics, 45, 8);
        this.getAnimatedArrow(recipe).draw(guiGraphics, 45, 8);

        this.drawCookTime(recipe, guiGraphics, 34);
    }

    protected void drawCookTime(RecipeHolder<DigestionRecipe> recipe, GuiGraphics guiGraphics, int y) {
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

    public void addToSlot(IRecipeSlotBuilder builder, int ingredientIndex, List<SizedIngredient> ingredients) {
        if (ingredientIndex >= ingredients.size())
            return;

        var ingredient = ingredients.get(ingredientIndex);

        builder.addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(ingredient.ingredient().getItems()).map(i -> i.copyWithCount(ingredient.count())).toList());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<DigestionRecipe> recipe, @NotNull IFocusGroup focuses) {
        var topLeft = builder.addSlot(INPUT, 1, 1)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1);
        var topRight = builder.addSlot(INPUT, 1 + 18, 1)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1);
        var bottomLeft = builder.addSlot(INPUT, 1, 1 + 18)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1);


        this.addToSlot(topLeft, 0, recipe.value().getSizedIngredients());
        this.addToSlot(topRight, 1, recipe.value().getSizedIngredients());
        this.addToSlot(bottomLeft, 2, recipe.value().getSizedIngredients());

        builder.addSlot(OUTPUT, 81, 9)
                .setBackground(JeiDrawables.OUTPUT_SLOT, -5, -5)
                .addItemStack(recipe.value().getResultItem(RegistryAccess.EMPTY));

        builder.addSlot(INPUT, 1 + 18, 1 + 18)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addIngredients(NeoForgeTypes.FLUID_STACK, this.getFluids(recipe))
                .setFluidRenderer(1000, false, 16, 16)
                .addRichTooltipCallback(addFluidTooltip(recipe.value().getFluidAmount()));

        //now add the bucket to the recipe lookup for the output fluid
        builder.addInvisibleIngredients(INPUT).addItemStacks(Arrays.stream(recipe.value().getFluid().getFluids()).map(f -> new ItemStack(f.getFluid().getBucket())).toList());
    }

    public List<FluidStack> getFluids(RecipeHolder<DigestionRecipe> recipe) {
        return Arrays.stream(recipe.value().getFluid().getFluids())
                .map(f -> {
                    var stack = f.copy();
                    f.setAmount(recipe.value().getFluidAmount());
                    return stack;
                }).toList();
    }

    @Override
    public @NotNull RecipeType<RecipeHolder<DigestionRecipe>> getRecipeType() {
        return JeiRecipeTypes.DIGESTION;
    }

}
