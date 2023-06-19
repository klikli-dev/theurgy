/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.salammoniacaccumulator;

import com.klikli_dev.theurgy.content.recipe.AccumulationRecipe;
import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A custom cached check
 */
class SalAmmoniacAccumulatorCachedCheck implements RecipeManager.CachedCheck<RecipeWrapperWithFluid, AccumulationRecipe> {

    private final RecipeType<AccumulationRecipe> type;
    private final RecipeManager.CachedCheck<RecipeWrapperWithFluid, AccumulationRecipe> internal;
    @Nullable
    private ResourceLocation lastRecipe;

    public SalAmmoniacAccumulatorCachedCheck(RecipeType<AccumulationRecipe> type) {
        this.type = type;
        this.internal = RecipeManager.createCheck(type);
    }

    private Optional<Pair<ResourceLocation, AccumulationRecipe>> getRecipeFor(ItemStack stack, Level level, @Nullable ResourceLocation lastRecipe) {

        var recipeManager = level.getRecipeManager();
        var map = recipeManager.byType(this.type);
        if (lastRecipe != null) {
            var recipe = map.get(lastRecipe);
            //test only the ingredient without the (separate) evaporate fluid ingredient check that the recipe.matches() would.
            if (recipe != null && recipe.hasSolute() && recipe.getSolute().test(stack)) {
                return Optional.of(Pair.of(lastRecipe, recipe));
            }
        }

        return map.entrySet().stream().filter((entry) -> entry.getValue().hasSolute() && entry.getValue().getSolute().test(stack)).findFirst().map((entry) -> Pair.of(entry.getKey(), entry.getValue()));
    }

    private Optional<Pair<ResourceLocation, AccumulationRecipe>> getRecipeFor(FluidStack stack, Level level, @Nullable ResourceLocation lastRecipe) {

        var recipeManager = level.getRecipeManager();
        var map = recipeManager.byType(this.type);
        if (lastRecipe != null) {
            var recipe = map.get(lastRecipe);
            //test only the fluid without the (separate) solute item ingredient check that the recipe.matches() would.
            if (recipe != null && recipe.getEvaporant().test(stack)) {
                return Optional.of(Pair.of(lastRecipe, recipe));
            }
        }

        return map.entrySet().stream().filter((entry) -> entry.getValue().getEvaporant().test(stack)).findFirst().map((entry) -> Pair.of(entry.getKey(), entry.getValue()));
    }

    /**
     * This only checks ingredients, not fluids
     */
    public Optional<AccumulationRecipe> getRecipeFor(ItemStack stack, Level level) {
        var optional = this.getRecipeFor(stack, level, this.lastRecipe);
        if (optional.isPresent()) {
            var pair = optional.get();
            this.lastRecipe = pair.getFirst();
            return Optional.of(pair.getSecond());
        } else {
            return Optional.empty();
        }
    }

    /**
     * This only checks fluids, not ingredients
     */
    public Optional<AccumulationRecipe> getRecipeFor(FluidStack stack, Level level) {
        var optional = this.getRecipeFor(stack, level, this.lastRecipe);
        if (optional.isPresent()) {
            var pair = optional.get();
            this.lastRecipe = pair.getFirst();
            return Optional.of(pair.getSecond());
        } else {
            return Optional.empty();
        }
    }

    /**
     * This checks full recipe validity: ingredients + fluids
     */
    @Override
    public Optional<AccumulationRecipe> getRecipeFor(RecipeWrapperWithFluid container, Level level) {
        var recipe = this.internal.getRecipeFor(container, level);
        if (recipe.isPresent()) {
            this.lastRecipe = recipe.get().getId();
        }

        return recipe;
    }
}
