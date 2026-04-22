// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RecipeSerializerRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(
            BuiltInRegistries.RECIPE_SERIALIZER, Theurgy.MODID);

    public static final Supplier<RecipeSerializer<DivinationRodRecipe>> DIVINATION_ROD = RECIPE_SERIALIZERS.register("divination_rod",
            () -> DivinationRodRecipe.SERIALIZER);

    public static final Supplier<RecipeSerializer<LogisticsNexusRecipe>> LOGISTICS_NEXUS = RECIPE_SERIALIZERS.register("logistics_nexus",
            () -> LogisticsNexusRecipe.SERIALIZER);

    public static final Supplier<RecipeSerializer<CalcinationRecipe>> CALCINATION = RECIPE_SERIALIZERS.register("calcination",
            () -> CalcinationRecipe.SERIALIZER);

    public static final Supplier<RecipeSerializer<LiquefactionRecipe>> LIQUEFACTION = RECIPE_SERIALIZERS.register("liquefaction",
            () -> LiquefactionRecipe.SERIALIZER);

    public static final Supplier<RecipeSerializer<DistillationRecipe>> DISTILLATION = RECIPE_SERIALIZERS.register("distillation",
            () -> DistillationRecipe.SERIALIZER);

    public static final Supplier<RecipeSerializer<IncubationRecipe>> INCUBATION = RECIPE_SERIALIZERS.register("incubation",
            () -> IncubationRecipe.SERIALIZER);

    public static final Supplier<RecipeSerializer<AccumulationRecipe>> ACCUMULATION = RECIPE_SERIALIZERS.register("accumulation",
            () -> AccumulationRecipe.SERIALIZER);

    public static final Supplier<RecipeSerializer<CatalysationRecipe>> CATALYSATION = RECIPE_SERIALIZERS.register("catalysation",
            () -> CatalysationRecipe.SERIALIZER);

    public static final Supplier<RecipeSerializer<ReformationRecipe>> REFORMATION = RECIPE_SERIALIZERS.register("reformation", () -> ReformationRecipe.SERIALIZER);

    public static final Supplier<RecipeSerializer<FermentationRecipe>> FERMENTATION = RECIPE_SERIALIZERS.register("fermentation", () -> FermentationRecipe.SERIALIZER);

    public static final Supplier<RecipeSerializer<DigestionRecipe>> DIGESTION = RECIPE_SERIALIZERS.register("digestion", () -> DigestionRecipe.SERIALIZER);

}
