// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.distillation;

import com.klikli_dev.modonomicon.client.gui.book.entry.BookEntryScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.klikli_dev.theurgy.content.gui.GuiSprites;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.content.recipe.display.DistillationRecipeDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

public class BookDistillationRecipePageRenderer extends BookRecipePageRenderer<DistillationRecipe, BookDistillationRecipePage> {
    public BookDistillationRecipePageRenderer(BookDistillationRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 60;
    }

    @Override
    protected void drawRecipe(GuiGraphicsExtractor guiGraphics, RecipeDisplayEntry recipeDisplayEntry, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        recipeY += 10;

        if (!(recipeDisplayEntry.display() instanceof DistillationRecipeDisplay display))
            return;

        if (!second) {
            if (!this.page.getTitle1().isEmpty()) {
                this.renderTitle(guiGraphics, this.page.getTitle1(), false, BookEntryScreen.PAGE_WIDTH / 2, 0);
            }
        } else {
            if (!this.page.getTitle2().isEmpty()) {
                this.renderTitle(guiGraphics, this.page.getTitle2(), false, BookEntryScreen.PAGE_WIDTH / 2,
                        recipeY - (this.page.getTitle2().getString().isEmpty() ? 10 : 0) - 10);
            }
        }

        GuiSprites.MODONOMICON_SLOT.extractRenderState(guiGraphics, recipeX, recipeY); //render the input slot
        this.parentScreen.renderIngredient(guiGraphics, recipeX + 3, recipeY + 3, mouseX, mouseY, display.ingredient().ingredient(), display.ingredient().count());

        GuiSprites.MODONOMICON_SLOT.extractRenderState(guiGraphics, recipeX + 61, recipeY); //render the output slot
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 61 + 3, recipeY + 3, mouseX, mouseY, display.output().create());

        GuiSprites.MODONOMICON_ARROW_RIGHT.extractRenderState(guiGraphics, recipeX + 40, recipeY + 7); //render the arrow
        var level = Minecraft.getInstance().level;
        ItemStack craftingStation = level != null ? display.craftingStation().resolveForFirstStack(SlotDisplayContext.fromLevel(level)) : ItemStack.EMPTY;
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 36, recipeY + 20, mouseX, mouseY, craftingStation);
    }
}
