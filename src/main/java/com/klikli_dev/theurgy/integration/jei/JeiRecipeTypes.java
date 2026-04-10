// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.*;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class JeiRecipeTypes {
    public static final IRecipeType<RecipeHolder<CalcinationRecipe>> CALCINATION = create(Theurgy.MODID, "calcination", CalcinationRecipe.class);
    public static final IRecipeType<RecipeHolder<LiquefactionRecipe>> LIQUEFACTION = create(Theurgy.MODID, "liquefaction", LiquefactionRecipe.class);
    public static final IRecipeType<RecipeHolder<DistillationRecipe>> DISTILLATION = create(Theurgy.MODID, "distillation", DistillationRecipe.class);
    public static final IRecipeType<RecipeHolder<IncubationRecipe>> INCUBATION = create(Theurgy.MODID, "incubation", IncubationRecipe.class);
    public static final IRecipeType<RecipeHolder<AccumulationRecipe>> ACCUMULATION = create(Theurgy.MODID, "accumulation", AccumulationRecipe.class);
    public static final IRecipeType<RecipeHolder<ReformationRecipe>> REFORMATION = create(Theurgy.MODID, "reformation", ReformationRecipe.class);
    public static final IRecipeType<RecipeHolder<FermentationRecipe>> FERMENTATION = create(Theurgy.MODID, "fermentation", FermentationRecipe.class);
    public static final IRecipeType<RecipeHolder<DigestionRecipe>> DIGESTION = create(Theurgy.MODID, "digestion", DigestionRecipe.class);

    public static <R extends Recipe<?>> IRecipeType<RecipeHolder<R>> create(String modid, String name, Class<? extends R> recipeClass) {
        Identifier uid = Identifier.fromNamespaceAndPath(modid, name);
        @SuppressWarnings({"unchecked", "RedundantCast"})
        Class<? extends RecipeHolder<R>> holderClass = (Class<? extends RecipeHolder<R>>) (Object) RecipeHolder.class;
        return IRecipeType.create(uid.getNamespace(), uid.getPath(), holderClass);
    }
}
