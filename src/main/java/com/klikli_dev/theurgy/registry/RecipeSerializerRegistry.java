// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.*;
import com.klikli_dev.theurgy.content.recipe.ingredient.FluidIngredient;
import com.klikli_dev.theurgy.content.recipe.ingredient.FluidTagEmptyCondition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.crafting.CraftingHelper;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Supplier;

public class RecipeSerializerRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(
            BuiltInRegistries.RECIPE_SERIALIZER, Theurgy.MODID);

    public static final Supplier<RecipeSerializer<DivinationRodRecipe>> DIVINATION_ROD = RECIPE_SERIALIZERS.register("divination_rod",
            DivinationRodRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<CalcinationRecipe>> CALCINATION = RECIPE_SERIALIZERS.register("calcination",
            CalcinationRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<LiquefactionRecipe>> LIQUEFACTION = RECIPE_SERIALIZERS.register("liquefaction",
            LiquefactionRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<DistillationRecipe>> DISTILLATION = RECIPE_SERIALIZERS.register("distillation",
            DistillationRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<IncubationRecipe>> INCUBATION = RECIPE_SERIALIZERS.register("incubation",
            IncubationRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<AccumulationRecipe>> ACCUMULATION = RECIPE_SERIALIZERS.register("accumulation",
            AccumulationRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<CatalysationRecipe>> CATALYSATION = RECIPE_SERIALIZERS.register("catalysation",
            CatalysationRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<?>> REFORMATION = RECIPE_SERIALIZERS.register("reformation", ReformationRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<?>> FERMENTATION = RECIPE_SERIALIZERS.register("fermentation", FermentationRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<?>> DIGESTION = RECIPE_SERIALIZERS.register("digestion", DigestionRecipe.Serializer::new);

}
