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

    public static final EmiStack CALCINATION_ICON = EmiStack.of(ItemRegistry.CALCINATION_OVEN.get());
    public static final EmiRecipeCategory CALCINATION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.CALCINATION.getId(), CALCINATION_ICON);

    public static final EmiStack DIGESTION_ICON = EmiStack.of(ItemRegistry.DIGESTION_VAT.get());
    public static final EmiRecipeCategory DIGESTION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.DIGESTION.getId(), DIGESTION_ICON);

    public static final EmiStack DISTILLATION_ICON = EmiStack.of(ItemRegistry.DISTILLER.get());
    public static final EmiRecipeCategory DISTILLATION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.DISTILLATION.getId(), DISTILLATION_ICON);

    public static final EmiStack FERMENTATION_ICON = EmiStack.of(ItemRegistry.FERMENTATION_VAT.get());
    public static final EmiRecipeCategory FERMENTATION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.FERMENTATION.getId(), FERMENTATION_ICON);

    public static final EmiStack INCUBATION_ICON = EmiStack.of(ItemRegistry.INCUBATOR.get());
    public static final EmiRecipeCategory INCUBATION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.INCUBATION.getId(), INCUBATION_ICON);

    public static final EmiStack LIQUEFACTION_ICON = EmiStack.of(ItemRegistry.LIQUEFACTION_CAULDRON.get());
    public static final EmiRecipeCategory LIQUEFACTION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.LIQUEFACTION.getId(), LIQUEFACTION_ICON);

    public static final EmiStack REFORMATION_ICON = EmiStack.of(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get());
    public static final EmiRecipeCategory REFORMATION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.REFORMATION.getId(), REFORMATION_ICON);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(ACCUMULATION_CATEGORY);
        registry.addWorkstation(ACCUMULATION_CATEGORY, ACCUMULATION_ICON);
        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.ACCUMULATION.get())) {
            registry.addRecipe(new AccumulationEmiRecipe(recipe));
        }

        registry.addCategory(CALCINATION_CATEGORY);
        registry.addWorkstation(CALCINATION_CATEGORY, CALCINATION_ICON);
        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.CALCINATION.get())) {
            registry.addRecipe(new CalcinationEmiRecipe(recipe));
        }

        registry.addCategory(DIGESTION_CATEGORY);
        registry.addWorkstation(DIGESTION_CATEGORY, DIGESTION_ICON);
        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.DIGESTION.get())) {
            registry.addRecipe(new DigestionEmiRecipe(recipe));
        }

        registry.addCategory(DISTILLATION_CATEGORY);
        registry.addWorkstation(DISTILLATION_CATEGORY, DISTILLATION_ICON);
        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.DISTILLATION.get())) {
            registry.addRecipe(new DistillationEmiRecipe(recipe));
        }
    }
}
