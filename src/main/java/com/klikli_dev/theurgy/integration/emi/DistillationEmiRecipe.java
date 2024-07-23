// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.emi;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class DistillationEmiRecipe implements EmiRecipe {

    private final RecipeHolder<DistillationRecipe> recipe;

    public DistillationEmiRecipe(RecipeHolder<DistillationRecipe> recipe) {
        this.recipe = recipe;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiPlugin.DISTILLATION_CATEGORY; // Ensure this category is defined in your EmiPlugin
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.recipe.id();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiIngredient.of(Arrays.stream(this.recipe.value().getIngredient().getItems())
                .map(i -> EmiStack.of(i.getItem(), this.recipe.value().getIngredientCount())).toList()));
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(this.recipe.value().getResultItem(RegistryAccess.EMPTY)));
    }

    @Override
    public int getDisplayWidth() {
        return 82;
    }

    @Override
    public int getDisplayHeight() {
        return 43;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 24, 8);

        widgets.addSlot(EmiIngredient.of(Arrays.stream(this.recipe.value().getIngredient().getItems())
                .map(i -> EmiStack.of(i.getItem(), this.recipe.value().getIngredientCount())).toList()), 1, 1);

        widgets.addSlot(EmiStack.of(this.recipe.value().getResultItem(RegistryAccess.EMPTY)), 61, 9).recipeContext(this);

        int cookTime = this.recipe.value().getTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable(TheurgyConstants.I18n.Gui.SMELTING_TIME_SECONDS, cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            int stringWidth = font.width(timeString);
            widgets.addText(timeString, this.getDisplayWidth() - stringWidth, 34, 0xFF808080, false);
        }
    }
}