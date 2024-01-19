// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei.recipes;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.recipe.ReformationRecipe;
import com.klikli_dev.theurgy.integration.jei.JeiDrawables;
import com.klikli_dev.theurgy.integration.jei.JeiRecipeTypes;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class ReformationCategory implements IRecipeCategory<RecipeHolder<ReformationRecipe>> {
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

    protected IDrawableAnimated getAnimatedArrow(RecipeHolder<ReformationRecipe> recipe) {
        int cookTime = recipe.value().getTime();
        if (cookTime <= 0) {
            cookTime = ReformationRecipe.DEFAULT_TIME;
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
    public void draw(RecipeHolder<ReformationRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(guiGraphics, 19, 19);

        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(guiGraphics, 130, 19);
        this.getAnimatedArrow(recipe).draw(guiGraphics, 130, 19);

        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(guiGraphics, 65, 19);

        this.drawCookTime(recipe, guiGraphics, 37);
        this.drawFlux(recipe, guiGraphics, 90);
        this.drawSourcePedestalCount(recipe, guiGraphics, 78);

        //the barrier in combination with the tooltip handling in the tooltip method shows the user that target sulfur will not be consumed
        RenderSystem.enableDepthTest();
        var barrier = new ItemStack(Items.BARRIER);
        Font font = Minecraft.getInstance().font;
        guiGraphics.renderFakeItem(barrier, 45, 1);
        guiGraphics.renderItemDecorations(font, barrier, 45, 1);
        RenderSystem.disableBlend();
    }

    protected void drawCookTime(RecipeHolder<ReformationRecipe> recipe, GuiGraphics guiGraphics, int y) {
        int cookTime = recipe.value().getTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            int stringWidth = font.width(timeString);
            guiGraphics.drawString(font, timeString, 140 - stringWidth / 2, y, 0xFF808080, false);
        }
    }

    protected void drawFlux(RecipeHolder<ReformationRecipe> recipe, GuiGraphics guiGraphics, int y) {
        int flux = recipe.value().getMercuryFlux();
        Component timeString = Component.translatable(TheurgyConstants.I18n.JEI.MERCURY_FLUX, flux);
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        guiGraphics.drawString(font, timeString, 1, y, 0xFF808080, false);
    }

    protected void drawSourcePedestalCount(RecipeHolder<ReformationRecipe> recipe, GuiGraphics guiGraphics, int y) {
        int count = recipe.value().getSources().size();
        Component timeString = Component.translatable(TheurgyConstants.I18n.JEI.SOURCE_PEDESTAL_COUNT, count);
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        int stringWidth = font.width(timeString);
        guiGraphics.drawString(font, timeString, 95 - stringWidth, y, 0xFF808080, false);
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ReformationRecipe> recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.CATALYST, 1, 15)
                .addItemStack(new ItemStack(ItemRegistry.SULFURIC_FLUX_EMITTER.get()));


        builder.addSlot(RecipeIngredientRole.CATALYST, 45, 19)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addIngredients(recipe.value().getTarget());

        builder.addSlot(RecipeIngredientRole.CATALYST, 45, 35)
                .addItemStack(new ItemStack(ItemRegistry.REFORMATION_TARGET_PEDESTAL.get()));

        //8 source slots, 2 columns, 4 rows
        int sourceSlotX = 90;
        int startY = 55;
        int sourceSlotY = startY; // Start from the bottom

        for (int i = 0; i < 8; i++) {
            var slot = builder.addSlot(INPUT, sourceSlotX, sourceSlotY).setBackground(JeiDrawables.INPUT_SLOT, -1, -1);

            if (i < recipe.value().getSources().size())
                slot.addIngredients(recipe.value().getSources().get(i));

            sourceSlotY -= 18; // Move upwards
            if (i % 4 == 3) {
                sourceSlotY = startY; // Reset to the bottom
                sourceSlotX += 18; // Move to the right
            }
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, 90 + 9, startY + 18)
                .addItemStack(new ItemStack(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get()));


        builder.addSlot(OUTPUT, 160, 19)
                .setBackground(JeiDrawables.OUTPUT_SLOT, -5, -5)
                .addItemStack(recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess()));
        builder.addSlot(RecipeIngredientRole.CATALYST, 160, 42)
                .addItemStack(new ItemStack(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get()));
    }

    @Override
    public RecipeType<RecipeHolder<ReformationRecipe>> getRecipeType() {
        return JeiRecipeTypes.REFORMATION;
    }

    @Override
    public List<Component> getTooltipStrings(RecipeHolder<ReformationRecipe> recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        // builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 45, 1)
        if (mouseX > 45 && mouseX < 45 + 18 && mouseY > 1 && mouseY < 1 + 18) {
            return List.of(Component.translatable(TheurgyConstants.I18n.JEI.TARGET_SULFUR_TOOLTIP).withStyle(ChatFormatting.ITALIC));
        }

        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }
}
