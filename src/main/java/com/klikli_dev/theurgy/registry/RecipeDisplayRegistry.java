// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.display.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RecipeDisplayRegistry {
    public static final DeferredRegister<RecipeDisplay.Type<?>> RECIPE_DISPLAYS = DeferredRegister.create(Registries.RECIPE_DISPLAY, Theurgy.MODID);

    public static final Supplier<RecipeDisplay.Type<AccumulationRecipeDisplay>> ACCUMULATION = RECIPE_DISPLAYS.register("accumulation", () -> new RecipeDisplay.Type<>(AccumulationRecipeDisplay.CODEC, AccumulationRecipeDisplay.STREAM_CODEC));
    public static final Supplier<RecipeDisplay.Type<CalcinationRecipeDisplay>> CALCINATION = RECIPE_DISPLAYS.register("calcination", () -> new RecipeDisplay.Type<>(CalcinationRecipeDisplay.CODEC, CalcinationRecipeDisplay.STREAM_CODEC));
    public static final Supplier<RecipeDisplay.Type<CatalysationRecipeDisplay>> CATALYSATION = RECIPE_DISPLAYS.register("catalysation", () -> new RecipeDisplay.Type<>(CatalysationRecipeDisplay.CODEC, CatalysationRecipeDisplay.STREAM_CODEC));
    public static final Supplier<RecipeDisplay.Type<DigestionRecipeDisplay>> DIGESTION = RECIPE_DISPLAYS.register("digestion", () -> new RecipeDisplay.Type<>(DigestionRecipeDisplay.CODEC, DigestionRecipeDisplay.STREAM_CODEC));
    public static final Supplier<RecipeDisplay.Type<DistillationRecipeDisplay>> DISTILLATION = RECIPE_DISPLAYS.register("distillation", () -> new RecipeDisplay.Type<>(DistillationRecipeDisplay.CODEC, DistillationRecipeDisplay.STREAM_CODEC));
    public static final Supplier<RecipeDisplay.Type<FermentationRecipeDisplay>> FERMENTATION = RECIPE_DISPLAYS.register("fermentation", () -> new RecipeDisplay.Type<>(FermentationRecipeDisplay.CODEC, FermentationRecipeDisplay.STREAM_CODEC));
    public static final Supplier<RecipeDisplay.Type<IncubationRecipeDisplay>> INCUBATION = RECIPE_DISPLAYS.register("incubation", () -> new RecipeDisplay.Type<>(IncubationRecipeDisplay.CODEC, IncubationRecipeDisplay.STREAM_CODEC));
    public static final Supplier<RecipeDisplay.Type<LiquefactionRecipeDisplay>> LIQUEFACTION = RECIPE_DISPLAYS.register("liquefaction", () -> new RecipeDisplay.Type<>(LiquefactionRecipeDisplay.CODEC, LiquefactionRecipeDisplay.STREAM_CODEC));
    public static final Supplier<RecipeDisplay.Type<ReformationRecipeDisplay>> REFORMATION = RECIPE_DISPLAYS.register("reformation", () -> new RecipeDisplay.Type<>(ReformationRecipeDisplay.CODEC, ReformationRecipeDisplay.STREAM_CODEC));

}
