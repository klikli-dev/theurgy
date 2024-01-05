// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeTypeRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(
            ForgeRegistries.RECIPE_TYPES, Theurgy.MODID);
    public static final RegistryObject<RecipeType<CalcinationRecipe>> CALCINATION = register("calcination");
    public static final RegistryObject<RecipeType<LiquefactionRecipe>> LIQUEFACTION = register("liquefaction");
    public static final RegistryObject<RecipeType<DistillationRecipe>> DISTILLATION = register("distillation");
    public static final RegistryObject<RecipeType<DistillationRecipe>> PYROMANTIC_BRAZIER = register("pyromantic_brazier");
    public static final RegistryObject<RecipeType<IncubationRecipe>> INCUBATION = register("incubation");
    public static final RegistryObject<RecipeType<AccumulationRecipe>> ACCUMULATION = register("accumulation");
    public static final RegistryObject<RecipeType<CatalysationRecipe>> CATALYSATION = register("catalysation");
    public static final RegistryObject<RecipeType<ReformationRecipe>> REFORMATION = register("reformation");
    public static final RegistryObject<RecipeType<FermentationRecipe>> FERMENTATION = register("fermentation");
    public static final RegistryObject<RecipeType<DigestionRecipe>> DIGESTION = register("digestion");

    static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(final String id) {
        return RECIPE_TYPES.register(id, () -> new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }
}
