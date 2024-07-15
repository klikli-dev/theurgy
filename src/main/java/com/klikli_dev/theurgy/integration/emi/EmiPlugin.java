package com.klikli_dev.theurgy.integration.emi;

import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

@EmiEntrypoint
public class EmiPlugin implements dev.emi.emi.api.EmiPlugin {

    public static final EmiStack ACCUMULATION_ICON = EmiStack.of(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get());
    public static final EmiRecipeCategory ACCUMULATION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.ACCUMULATION.getId(), ACCUMULATION_ICON);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(ACCUMULATION_CATEGORY);
        registry.addWorkstation(ACCUMULATION_CATEGORY, ACCUMULATION_ICON);


        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.ACCUMULATION.get())) {
            registry.addRecipe(new AccumulationEmiRecipe(recipe));
        }

    }
}
