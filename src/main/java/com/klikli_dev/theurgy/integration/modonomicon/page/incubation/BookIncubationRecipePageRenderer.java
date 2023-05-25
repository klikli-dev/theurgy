/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.modonomicon.page.incubation;

import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookIncubationRecipePageRenderer extends BookRecipePageRenderer<IncubationRecipe, BookIncubationRecipePage> {

    protected Map<IncubationRecipe, ItemStack[]> renderableSulfurIngredients = new HashMap<>();

    public BookIncubationRecipePageRenderer(BookIncubationRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 65;
    }

    @Override
    public void onBeginDisplayPage(BookContentScreen parentScreen, int left, int top) {
        super.onBeginDisplayPage(parentScreen, left, top);

        //sulfurs in the recipe are in most cases specified only via the item id (as one sulfur = one item), but for rendering we need the nbt, so we get it from the corresponding recipe.
        var recipeManager = Minecraft.getInstance().level.getRecipeManager();
        var liquefactionRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get()).stream().filter(r -> r.getResultItem(Minecraft.getInstance().level.registryAccess()) != null).collect(Collectors.toList());

        if (this.page.getRecipe1() != null)
            this.renderableSulfurIngredients.put(this.page.getRecipe1(), this.getRenderableSulfurIngredients(liquefactionRecipes, this.page.getRecipe1()));
        if (this.page.getRecipe2() != null)
            this.renderableSulfurIngredients.put(this.page.getRecipe2(), this.getRenderableSulfurIngredients(liquefactionRecipes, this.page.getRecipe2()));
    }

    protected ItemStack[] getRenderableSulfurIngredients(List<LiquefactionRecipe> liquefactionRecipes, IncubationRecipe recipe) {
        return Arrays.stream(recipe.getSulfur().getItems()).map(sulfur -> {
            if (sulfur.hasTag())
                return sulfur;

            var sulfurItem = sulfur.getItem();
            var sulfurWithNbt = liquefactionRecipes.stream()
                    .filter(r -> r.getResultItem(Minecraft.getInstance().level.registryAccess()).getItem() == sulfurItem).map(r -> r.getResultItem(Minecraft.getInstance().level.registryAccess())).findFirst();

            if (sulfurWithNbt.isPresent()) {
                sulfur = sulfur.copy();
                sulfur.setTag(sulfurWithNbt.get().getTag());
            }

            return sulfur;
        }).toArray(ItemStack[]::new);
    }

    @Override
    protected void drawRecipe(PoseStack poseStack, IncubationRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
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


        GuiTextures.MODONOMICON_SLOT.render(poseStack, recipeX, recipeY); //render the mercury input slot
        this.parentScreen.renderIngredient(poseStack, recipeX + 3, recipeY + 3, mouseX, mouseY, recipe.getMercury());

        GuiTextures.MODONOMICON_SLOT.render(poseStack, recipeX + 24, recipeY); //render the salt input slot
        this.parentScreen.renderIngredient(poseStack, recipeX + 24 + 3, recipeY + 3, mouseX, mouseY, recipe.getSalt());

        GuiTextures.MODONOMICON_SLOT.render(poseStack, recipeX, recipeY + 24); //render the sulfur input slot
        var sulfurs = this.renderableSulfurIngredients.get(recipe);
        if (sulfurs != null && sulfurs.length > 0) {
            this.parentScreen.renderItemStacks(poseStack, recipeX + 3, recipeY + 24 + 3, mouseX, mouseY, List.of(sulfurs));
        }



        GuiTextures.MODONOMICON_SLOT.render(poseStack, recipeX + 61 + 14, recipeY); //render the output slot
        this.parentScreen.renderItemStack(poseStack, recipeX + 61 + 3 + 14, recipeY + 3, mouseX, mouseY, recipe.getResultItem(this.parentScreen.getMinecraft().level.registryAccess()));

        GuiTextures.MODONOMICON_ARROW_RIGHT.render(poseStack, recipeX + 40 + 16, recipeY + 7); //render the arrow
        this.parentScreen.renderItemStack(poseStack, recipeX + 36 + 16, recipeY + 24, mouseX, mouseY, recipe.getToastSymbol());
    }
}
