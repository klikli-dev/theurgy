// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RecipeTypeRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(
            BuiltInRegistries.RECIPE_TYPE, Theurgy.MODID);
    public static final Supplier<RecipeType<CalcinationRecipe>> CALCINATION = register("calcination");
    public static final Supplier<RecipeType<LiquefactionRecipe>> LIQUEFACTION = register("liquefaction");
    public static final Supplier<RecipeType<DistillationRecipe>> DISTILLATION = register("distillation");
    public static final Supplier<RecipeType<DistillationRecipe>> PYROMANTIC_BRAZIER = register("pyromantic_brazier");
    public static final Supplier<RecipeType<IncubationRecipe>> INCUBATION = register("incubation");
    public static final Supplier<RecipeType<AccumulationRecipe>> ACCUMULATION = register("accumulation");
    public static final Supplier<RecipeType<CatalysationRecipe>> CATALYSATION = register("catalysation");
    public static final Supplier<RecipeType<ReformationRecipe>> REFORMATION = register("reformation");
    public static final Supplier<RecipeType<FermentationRecipe>> FERMENTATION = register("fermentation");
    public static final Supplier<RecipeType<DigestionRecipe>> DIGESTION = register("digestion");

    static <T extends Recipe<?>> Supplier<RecipeType<T>> register(final String id) {
        return RECIPE_TYPES.register(id, () -> new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }
}
