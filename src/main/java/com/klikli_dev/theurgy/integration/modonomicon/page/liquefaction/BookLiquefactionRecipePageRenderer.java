// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.liquefaction;

import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.klikli_dev.modonomicon.fluid.FluidHolder;
import com.klikli_dev.modonomicon.fluid.ForgeFluidHolder;
import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidType;
import java.util.Arrays;

public class BookLiquefactionRecipePageRenderer extends BookRecipePageRenderer<LiquefactionRecipe, BookLiquefactionRecipePage> {
    public BookLiquefactionRecipePageRenderer(BookLiquefactionRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 60;
    }

    @Override
    protected void drawRecipe(GuiGraphics guiGraphics, RecipeHolder<LiquefactionRecipe> recipeHolder, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        recipeY += 10;
        var recipe = recipeHolder.value();

        if (!second) {
            if (!this.page.getTitle1().isEmpty()) {
                this.renderTitle(guiGraphics, this.page.getTitle1(), false, BookContentScreen.PAGE_WIDTH / 2, 0);
            }
        } else {
            if (!this.page.getTitle2().isEmpty()) {
                this.renderTitle(guiGraphics, this.page.getTitle2(), false, BookContentScreen.PAGE_WIDTH / 2,
                        recipeY - (this.page.getTitle2().getString().isEmpty() ? 10 : 0) - 10);
            }
        }

        GuiTextures.MODONOMICON_SLOT.render(guiGraphics, recipeX, recipeY); //render the fluid input slot
        this.parentScreen.renderFluidStacks(guiGraphics, recipeX + 2, recipeY + 2, mouseX, mouseY, Arrays.stream(recipe.getSolvent().getFluids()).map(f -> (FluidHolder) new ForgeFluidHolder(f.getFluid(), recipe.getSolventAmount(), f.getTag())).toList(), FluidType.BUCKET_VOLUME);

        GuiTextures.MODONOMICON_SLOT.render(guiGraphics, recipeX, recipeY + 24); //render the item input slot
        this.parentScreen.renderIngredient(guiGraphics, recipeX + 3, recipeY + 24 + +3, mouseX, mouseY, recipe.getIngredients().get(0));

        GuiTextures.MODONOMICON_SLOT.render(guiGraphics, recipeX + 61, recipeY); //render the output slot
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 61 + 3, recipeY + 3, mouseX, mouseY, recipe.getResultItem(this.parentScreen.getMinecraft().level.registryAccess()));

        GuiTextures.MODONOMICON_ARROW_RIGHT.render(guiGraphics, recipeX + 40, recipeY + 7); //render the arrow
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 36, recipeY + 20, mouseX, mouseY, recipe.getToastSymbol());
    }

}
