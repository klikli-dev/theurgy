// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.*;
import mezz.jei.api.recipe.RecipeType;

public class JeiRecipeTypes {
    public static final RecipeType<CalcinationRecipe> CALCINATION = RecipeType.create(Theurgy.MODID, "calcination", CalcinationRecipe.class);
    public static final RecipeType<LiquefactionRecipe> LIQUEFACTION = RecipeType.create(Theurgy.MODID, "liquefaction", LiquefactionRecipe.class);
    public static final RecipeType<DistillationRecipe> DISTILLATION = RecipeType.create(Theurgy.MODID, "distillation", DistillationRecipe.class);
    public static final RecipeType<IncubationRecipe> INCUBATION = RecipeType.create(Theurgy.MODID, "incubation", IncubationRecipe.class);
    public static final RecipeType<AccumulationRecipe> ACCUMULATION = RecipeType.create(Theurgy.MODID, "accumulation", AccumulationRecipe.class);
    public static final RecipeType<ReformationRecipe> REFORMATION = RecipeType.create(Theurgy.MODID, "reformation", ReformationRecipe.class);
}
