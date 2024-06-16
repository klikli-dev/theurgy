// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.incubation;

import com.klikli_dev.modonomicon.client.gui.book.entry.BookEntryScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookIncubationRecipePageRenderer extends BookRecipePageRenderer<IncubationRecipe, BookIncubationRecipePage> {

    protected Map<ResourceLocation, ItemStack[]> renderableSulfurIngredients = new HashMap<>();

    public BookIncubationRecipePageRenderer(BookIncubationRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 65;
    }

    @Override
    public void onBeginDisplayPage(BookEntryScreen parentScreen, int left, int top) {
        super.onBeginDisplayPage(parentScreen, left, top);

        if (this.page.getRecipe1() != null)
            this.renderableSulfurIngredients.put(this.page.getRecipe1().id(), this.getRenderableSulfurIngredients(this.page.getRecipe1()));
        if (this.page.getRecipe2() != null)
            this.renderableSulfurIngredients.put(this.page.getRecipe2().id(), this.getRenderableSulfurIngredients(this.page.getRecipe2()));
    }

    protected ItemStack[] getRenderableSulfurIngredients(RecipeHolder<IncubationRecipe> recipe) {
        return Arrays.stream(recipe.value().getSulfur().getItems()).map(sulfur -> sulfur.copyWithCount(1)).toArray(ItemStack[]::new);
    }

    @Override
    protected void drawRecipe(GuiGraphics guiGraphics, RecipeHolder<IncubationRecipe> recipeHolder, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        recipeY += 10;
        var recipe = recipeHolder.value();

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


        GuiTextures.MODONOMICON_SLOT.render(guiGraphics, recipeX, recipeY); //render the mercury input slot
        this.parentScreen.renderIngredient(guiGraphics, recipeX + 3, recipeY + 3, mouseX, mouseY, recipe.getMercury());

        GuiTextures.MODONOMICON_SLOT.render(guiGraphics, recipeX + 24, recipeY); //render the salt input slot
        this.parentScreen.renderIngredient(guiGraphics, recipeX + 24 + 3, recipeY + 3, mouseX, mouseY, recipe.getSalt());

        GuiTextures.MODONOMICON_SLOT.render(guiGraphics, recipeX, recipeY + 24); //render the sulfur input slot
        var sulfurs = this.renderableSulfurIngredients.get(recipeHolder.id());
        if (sulfurs != null && sulfurs.length > 0) {
            this.parentScreen.renderItemStacks(guiGraphics, recipeX + 3, recipeY + 24 + 3, mouseX, mouseY, List.of(sulfurs));
        }


        GuiTextures.MODONOMICON_SLOT.render(guiGraphics, recipeX + 61 + 14, recipeY); //render the output slot
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 61 + 3 + 14, recipeY + 3, mouseX, mouseY, recipe.getResultItem(this.parentScreen.getMinecraft().level.registryAccess()));

        GuiTextures.MODONOMICON_ARROW_RIGHT.render(guiGraphics, recipeX + 40 + 16, recipeY + 7); //render the arrow
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 36 + 16, recipeY + 24, mouseX, mouseY, recipe.getToastSymbol());
    }
}
