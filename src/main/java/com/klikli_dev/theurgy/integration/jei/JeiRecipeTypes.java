// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.*;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class JeiRecipeTypes {
    public static final RecipeType<RecipeHolder<CalcinationRecipe>> CALCINATION = create(Theurgy.MODID, "calcination", CalcinationRecipe.class);
    public static final RecipeType<RecipeHolder<LiquefactionRecipe>> LIQUEFACTION = create(Theurgy.MODID, "liquefaction", LiquefactionRecipe.class);
    public static final RecipeType<RecipeHolder<DistillationRecipe>> DISTILLATION = create(Theurgy.MODID, "distillation", DistillationRecipe.class);
    public static final RecipeType<RecipeHolder<IncubationRecipe>> INCUBATION = create(Theurgy.MODID, "incubation", IncubationRecipe.class);
    public static final RecipeType<RecipeHolder<AccumulationRecipe>> ACCUMULATION = create(Theurgy.MODID, "accumulation", AccumulationRecipe.class);
    public static final RecipeType<RecipeHolder<ReformationRecipe>> REFORMATION = create(Theurgy.MODID, "reformation", ReformationRecipe.class);
    public static final RecipeType<RecipeHolder<FermentationRecipe>> FERMENTATION = create(Theurgy.MODID, "fermentation", FermentationRecipe.class);
    public static final RecipeType<RecipeHolder<DigestionRecipe>> DIGESTION = create(Theurgy.MODID, "digestion", DigestionRecipe.class);

    public static <R extends Recipe<?>> RecipeType<RecipeHolder<R>> create(String modid, String name, Class<? extends R> recipeClass) {
        ResourceLocation uid = ResourceLocation.fromNamespaceAndPath(modid,  name);
        @SuppressWarnings({"unchecked", "RedundantCast"})
        Class<? extends RecipeHolder<R>> holderClass = (Class<? extends RecipeHolder<R>>) (Object) RecipeHolder.class;
        return new RecipeType<>(uid, holderClass);
    }
}
