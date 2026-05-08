// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.incubation;

import com.klikli_dev.modonomicon.client.gui.book.entry.BookEntryScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.klikli_dev.theurgy.content.gui.GuiSprites;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.content.recipe.display.IncubationRecipeDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

import java.util.List;

public class BookIncubationRecipePageRenderer extends BookRecipePageRenderer<IncubationRecipe, BookIncubationRecipePage> {

    public BookIncubationRecipePageRenderer(BookIncubationRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 65;
    }

    @Override
    protected void drawRecipe(GuiGraphicsExtractor guiGraphics, RecipeDisplayEntry recipeDisplayEntry, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        recipeY += 10;

        if (!(recipeDisplayEntry.display() instanceof IncubationRecipeDisplay display))
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


        GuiSprites.MODONOMICON_SLOT.extractRenderState(guiGraphics, recipeX, recipeY); //render the mercury input slot
        this.parentScreen.renderIngredient(guiGraphics, recipeX + 3, recipeY + 3, mouseX, mouseY, display.mercury());

        GuiSprites.MODONOMICON_SLOT.extractRenderState(guiGraphics, recipeX + 24, recipeY); //render the salt input slot
        this.parentScreen.renderIngredient(guiGraphics, recipeX + 24 + 3, recipeY + 3, mouseX, mouseY, display.salt());

        GuiSprites.MODONOMICON_SLOT.extractRenderState(guiGraphics, recipeX, recipeY + 24); //render the sulfur input slot
        ItemStack[] sulfurs = display.sulfur().items().map(ItemStack::new).toArray(ItemStack[]::new);
        if (sulfurs != null && sulfurs.length > 0) {
            this.parentScreen.renderItemStacks(guiGraphics, recipeX + 3, recipeY + 24 + 3, mouseX, mouseY, List.of(sulfurs));
        }


        GuiSprites.MODONOMICON_SLOT.extractRenderState(guiGraphics, recipeX + 61 + 14, recipeY); //render the output slot
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 61 + 3 + 14, recipeY + 3, mouseX, mouseY, display.output().getStack());

        GuiSprites.MODONOMICON_ARROW_RIGHT.extractRenderState(guiGraphics, recipeX + 40 + 16, recipeY + 7); //render the arrow
        var level = Minecraft.getInstance().level;
        ItemStack craftingStation = level != null ? display.craftingStation().resolveForFirstStack(SlotDisplayContext.fromLevel(level)) : ItemStack.EMPTY;
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 36 + 16, recipeY + 24, mouseX, mouseY, craftingStation);
    }
}
