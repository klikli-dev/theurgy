package com.klikli_dev.theurgy.integration.emi;

import com.klikli_dev.theurgy.content.recipe.AccumulationRecipe;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccumulationEmiRecipe implements EmiRecipe {

    private final RecipeHolder<AccumulationRecipe> recipe;

    public AccumulationEmiRecipe(RecipeHolder<AccumulationRecipe> recipe) {
        this.recipe = recipe;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiPlugin.ACCUMULATION_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.recipe.id();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        var inputs = new ArrayList<EmiIngredient>();
        if (this.recipe.value().hasSolute())
            inputs.add(EmiIngredient.of(this.recipe.value().getSolute()));

        inputs.add(EmiIngredient.of(Arrays.stream(this.recipe.value().getEvaporant().getFluids())
                .map(f -> EmiStack.of(f.getFluid(), f.getAmount())).toList()));

        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(
                EmiStack.of(this.recipe.value().getResult().getFluid(), this.recipe.value().getResult().getAmount())
        );
    }

    @Override
    public int getDisplayWidth() {
        return 82;
    }

    @Override
    public int getDisplayHeight() {
        return 40;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 24, 2);

        widgets.addSlot(EmiIngredient.of(Arrays.stream(this.recipe.value().getEvaporant().getFluids())
                .map(f -> EmiStack.of(f.getFluid(), f.getAmount())).toList()), 1, 1);

        if (this.recipe.value().hasSolute()) {
            widgets.addSlot(EmiIngredient.of(this.recipe.value().getSolute()), 1, 21);
        }

        widgets.addSlot(EmiStack.of(this.recipe.value().getResult().getFluid(), this.recipe.value().getResult().getAmount()), 56, 1).recipeContext(this);

        int cookTime = this.recipe.value().getTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            int stringWidth = font.width(timeString);
            widgets.addText(timeString, this.getDisplayWidth() - stringWidth, 29, 0xFF808080, false);
        }
    }
}
