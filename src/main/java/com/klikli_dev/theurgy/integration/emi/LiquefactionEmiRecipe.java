package com.klikli_dev.theurgy.integration.emi;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
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

public class LiquefactionEmiRecipe implements EmiRecipe {

    private final RecipeHolder<LiquefactionRecipe> recipe;

    public LiquefactionEmiRecipe(RecipeHolder<LiquefactionRecipe> recipe) {
        this.recipe = recipe;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiPlugin.LIQUEFACTION_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.recipe.id();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        var inputs = new ArrayList<EmiIngredient>(Arrays.stream(this.recipe.value().getSolvent().getFluids())
                .map(f -> EmiStack.of(f.getFluid(), this.recipe.value().getSolventAmount())).toList());
        inputs.add(EmiIngredient.of(this.recipe.value().getIngredients().getFirst()));
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
//        widgets.addTexture(EmiPlugin.LIQUEFACTION_ICON, 12, 20);

        widgets.addSlot(EmiIngredient.of(Arrays.stream(this.recipe.value().getSolvent().getFluids())
                .map(f -> EmiStack.of(f.getFluid(), this.recipe.value().getSolventAmount())).toList()), 1, 1);

        widgets.addSlot(EmiIngredient.of(this.recipe.value().getIngredients().getFirst()), 19, 1);

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