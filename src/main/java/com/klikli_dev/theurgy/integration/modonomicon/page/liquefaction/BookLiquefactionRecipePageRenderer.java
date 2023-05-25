/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.modonomicon.page.liquefaction;

import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.fluids.FluidType;

import java.util.Arrays;
import java.util.List;

public class BookLiquefactionRecipePageRenderer extends BookRecipePageRenderer<LiquefactionRecipe, BookLiquefactionRecipePage> {
    public BookLiquefactionRecipePageRenderer(BookLiquefactionRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 60;
    }

    @Override
    protected void drawRecipe(PoseStack poseStack, LiquefactionRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
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

        GuiTextures.MODONOMICON_SLOT.render(poseStack, recipeX, recipeY); //render the fluid input slot
        this.parentScreen.renderFluidStacks(poseStack, recipeX + 2, recipeY + 2, mouseX, mouseY, List.of(recipe.getSolvent().getFluids()), FluidType.BUCKET_VOLUME);

        GuiTextures.MODONOMICON_SLOT.render(poseStack, recipeX, recipeY + 24); //render the item input slot
        this.parentScreen.renderIngredient(poseStack, recipeX + 3, recipeY + 24 + +3, mouseX, mouseY, recipe.getIngredients().get(0));

        GuiTextures.MODONOMICON_SLOT.render(poseStack, recipeX + 61, recipeY); //render the output slot
        this.parentScreen.renderItemStack(poseStack, recipeX + 61 + 3, recipeY + 3, mouseX, mouseY, recipe.getResultItem(this.parentScreen.getMinecraft().level.registryAccess()));

        GuiTextures.MODONOMICON_ARROW_RIGHT.render(poseStack, recipeX + 40, recipeY + 7); //render the arrow
        this.parentScreen.renderItemStack(poseStack, recipeX + 36, recipeY + 20, mouseX, mouseY, recipe.getToastSymbol());
    }

}
