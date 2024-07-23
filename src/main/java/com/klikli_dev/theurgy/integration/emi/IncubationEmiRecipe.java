// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.emi;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class IncubationEmiRecipe implements EmiRecipe {

    private final RecipeHolder<IncubationRecipe> recipe;

    public IncubationEmiRecipe(RecipeHolder<IncubationRecipe> recipe) {
        this.recipe = recipe;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiPlugin.INCUBATION_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.recipe.id();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return Arrays.asList(
                EmiIngredient.of(this.recipe.value().getMercury()),
                EmiIngredient.of(Arrays.stream(this.recipe.value().getSulfur().getItems()).map(EmiStack::of).toList()),
                EmiIngredient.of(this.recipe.value().getSalt())
        );
    }

    @Override
    public List<EmiStack> getOutputs() {
        return Arrays.stream(this.recipe.value().getResult().getStacks()).map(EmiStack::of).toList();
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(
                EmiStack.of(ItemRegistry.INCUBATOR_MERCURY_VESSEL),
                EmiStack.of(ItemRegistry.INCUBATOR_SULFUR_VESSEL),
                EmiStack.of(ItemRegistry.INCUBATOR_SALT_VESSEL)
        );
    }

    @Override
    public int getDisplayWidth() {
        return 82;
    }

    @Override
    public int getDisplayHeight() {
        return 60;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 24, 22);
        widgets.addTexture(EmiTexture.EMPTY_FLAME, 28, 44);

        widgets.addSlot(EmiIngredient.of(this.recipe.value().getMercury()), 1, 1);
        widgets.addSlot(EmiIngredient.of(Arrays.stream(this.recipe.value().getSulfur().getItems()).map(EmiStack::of).toList()), 1, 21);
        widgets.addSlot(EmiIngredient.of(this.recipe.value().getSalt()), 1, 42);

        widgets.addSlot(EmiIngredient.of(Arrays.stream(this.recipe.value().getResult().getStacks())
                .map(
                        i -> EmiStack.of(i.getItem(), i.getCount())
                )
                .toList()), 61, 22).recipeContext(this);

        int cookTime = this.recipe.value().getTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable(TheurgyConstants.I18n.Gui.SMELTING_TIME_SECONDS, cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            int stringWidth = font.width(timeString);
            widgets.addText(timeString, this.getDisplayWidth() - stringWidth, 47, 0xFF808080, false);
        }
    }
}