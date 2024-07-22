package com.klikli_dev.theurgy.integration.emi;

import com.klikli_dev.theurgy.content.recipe.ReformationRecipe;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReformationEmiRecipe implements EmiRecipe {

    private final RecipeHolder<ReformationRecipe> recipe;

    public ReformationEmiRecipe(RecipeHolder<ReformationRecipe> recipe) {
        this.recipe = recipe;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiPlugin.REFORMATION_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.recipe.id();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        var inputs = new ArrayList<EmiIngredient>();
        inputs.add(EmiIngredient.of(this.recipe.value().getTarget()));
        this.recipe.value().getSources().forEach(source -> inputs.add(EmiIngredient.of(source)));
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(this.recipe.value().getResultItem(RegistryAccess.EMPTY)));
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(
                EmiStack.of(ItemRegistry.REFORMATION_RESULT_PEDESTAL),
                EmiStack.of(ItemRegistry.REFORMATION_TARGET_PEDESTAL),
                EmiStack.of(ItemRegistry.REFORMATION_SOURCE_PEDESTAL)
        );
    }

    @Override
    public int getDisplayWidth() {
        return 180;
    }

    @Override
    public int getDisplayHeight() {
        return 100;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 130, 19);

        widgets.addSlot(EmiStack.of(ItemRegistry.SULFURIC_FLUX_EMITTER.get()), 1, 15).catalyst(true).drawBack(false);

        //TODO arrow
        //TODO second arrow

        widgets.addSlot(EmiIngredient.of(this.recipe.value().getTarget()), 45, 19).catalyst(true);

        widgets.addSlot(EmiStack.of(ItemRegistry.REFORMATION_TARGET_PEDESTAL.get()), 45, 35).catalyst(true).drawBack(false);

        int sourceSlotX = 90;
        int startY = 55;
        int sourceSlotY = startY; // Start from the bottom
        for (int i = 0; i < this.recipe.value().getSources().size(); i++) {
            widgets.addSlot(EmiIngredient.of(this.recipe.value().getSources().get(i)), sourceSlotX, sourceSlotY);

            sourceSlotY -= 18; // Move upwards
            if (i % 4 == 3) {
                sourceSlotY = startY; // Reset to the bottom
                sourceSlotX += 18; // Move to the right
            }

            //TODO: draw empty slots
        }

        //TODO: Draw mercury flux

        widgets.addSlot(EmiStack.of(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get()), 90 + 9, startY + 18).catalyst(true).drawBack(false);

        widgets.addSlot(EmiStack.of(this.recipe.value().getResultItem(RegistryAccess.EMPTY)), 160, 19).recipeContext(this);

        widgets.addSlot(EmiStack.of(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get()), 160, 42).catalyst(true).drawBack(false);
    }
}