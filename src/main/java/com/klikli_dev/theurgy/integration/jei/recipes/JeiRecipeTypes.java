package com.klikli_dev.theurgy.integration.jei.recipes;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.recipe.CalcinationRecipe;
import mezz.jei.api.recipe.RecipeType;

public class JeiRecipeTypes {
    public static final RecipeType<CalcinationRecipe> CALCINATION =
            RecipeType.create(Theurgy.MODID, "calcination", CalcinationRecipe.class);
}
