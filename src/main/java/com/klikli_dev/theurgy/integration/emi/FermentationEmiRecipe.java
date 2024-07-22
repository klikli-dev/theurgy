package com.klikli_dev.theurgy.integration.emi;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FermentationEmiRecipe implements EmiRecipe {

    private final RecipeHolder<FermentationRecipe> recipe;

    public FermentationEmiRecipe(RecipeHolder<FermentationRecipe> recipe) {
        this.recipe = recipe;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiPlugin.FERMENTATION_CATEGORY; // Make sure this category is defined in your EmiPlugin
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.recipe.id();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        var inputs = new ArrayList<EmiIngredient>();

        this.recipe.value().getIngredients().forEach(ingredient -> inputs.add(EmiIngredient.of(ingredient)));
        inputs.add(EmiIngredient.of(Arrays.stream(this.recipe.value().getFluid().getFluids())
                .map(f ->
                        EmiStack.of(f.getFluid(), this.recipe.value().getFluidAmount())
                )
                .toList()));
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(this.recipe.value().getResultItem(RegistryAccess.EMPTY)));
    }

    @Override
    public int getDisplayWidth() {
        return 102;
    }

    @Override
    public int getDisplayHeight() {
        return 43;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 45, 8);

        int index = 0;
        for (var ingredient : this.recipe.value().getIngredients()) {
            int x = 1 + (index % 2) * 18;
            int y = 1 + (index / 2) * 18;
            widgets.addSlot(EmiIngredient.of(ingredient), x, y);
            index++;
        }

        widgets.addSlot(EmiIngredient.of(Arrays.stream(this.recipe.value().getFluid().getFluids())
                .map(f ->
                        EmiStack.of(f.getFluid(), this.recipe.value().getFluidAmount())
                )
                .toList()), 1 + 18, 1 + 18);

        widgets.addSlot(EmiStack.of(this.recipe.value().getResultItem(RegistryAccess.EMPTY)), 81, 9).recipeContext(this);

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