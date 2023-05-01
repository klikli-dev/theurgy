/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.modonomicon.page.distillation;

import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class BookDistillationRecipePageRenderer extends BookRecipePageRenderer<DistillationRecipe, BookDistillationRecipePage> {
    public BookDistillationRecipePageRenderer(BookDistillationRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 60;
    }

    @Override
    protected void drawRecipe(PoseStack poseStack, DistillationRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        recipeY += 10;

        if (!second) {
            if (!this.page.getTitle1().isEmpty()) {
                this.renderTitle(this.page.getTitle1(), false, poseStack, BookContentScreen.PAGE_WIDTH / 2, 0);
            }
        } else {
            if (!this.page.getTitle2().isEmpty()) {
                this.renderTitle(this.page.getTitle2(), false, poseStack, BookContentScreen.PAGE_WIDTH / 2,
                        recipeY - (this.page.getTitle2().getString().isEmpty() ? 10 : 0) - 10);
            }
        }

        GuiTextures.MODONOMICON_SLOT.render(poseStack, recipeX, recipeY); //render the input slot
        this.renderIngredient(poseStack, recipeX + 3, recipeY + 3, mouseX, mouseY, recipe.getIngredients().get(0), recipe.getIngredientCount());

        GuiTextures.MODONOMICON_SLOT.render(poseStack, recipeX + 61, recipeY); //render the output slot
        this.parentScreen.renderItemStack(poseStack, recipeX + 61 + 3, recipeY + 3, mouseX, mouseY, recipe.getResultItem(this.parentScreen.getMinecraft().level.registryAccess()));

        GuiTextures.MODONOMICON_ARROW_RIGHT.render(poseStack, recipeX + 40, recipeY + 7); //render the arrow
        this.parentScreen.renderItemStack(poseStack, recipeX + 36, recipeY + 20, mouseX, mouseY, recipe.getToastSymbol());
    }

    public void renderIngredient(PoseStack poseStack, int x, int y, int mouseX, int mouseY, Ingredient ingr, int count) {
        ItemStack[] stacks = ingr.getItems();
        if (stacks.length > 0) {
            var stack = stacks[(this.parentScreen.ticksInBook / 20) % stacks.length].copyWithCount(count);
            this.parentScreen.renderItemStack(poseStack, x, y, mouseX, mouseY, stack);
        }
    }
}
