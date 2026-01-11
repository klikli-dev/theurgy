// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator;

import com.klikli_dev.theurgy.content.recipe.AccumulationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import net.minecraft.resources.ResourceKey;
import com.klikli_dev.theurgy.util.LevelUtil;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A custom cached check
 */
class SalAmmoniacAccumulatorCachedCheck implements RecipeManager.CachedCheck<ItemHandlerWithFluidRecipeInput, AccumulationRecipe> {

    private final RecipeType<AccumulationRecipe> type;
    private final RecipeManager.CachedCheck<ItemHandlerWithFluidRecipeInput, AccumulationRecipe> internal;
    @Nullable
    private ResourceKey<Recipe<?>> lastRecipe;

    public SalAmmoniacAccumulatorCachedCheck(RecipeType<AccumulationRecipe> type) {
        this.type = type;
        this.internal = RecipeManager.createCheck(type);
    }

    private Optional<RecipeHolder<AccumulationRecipe>> getRecipeFor(ItemStack stack, ServerLevel level, @Nullable ResourceKey<Recipe<?>> lastRecipe) {
        var recipeManager = level.getServer().getRecipeManager();
        if (lastRecipe != null) {
            var recipeOptional = recipeManager.byKey(lastRecipe);
             if(recipeOptional.isPresent()){
                var recipe = recipeOptional.get();
                if(recipe.value().getType() == this.type) {
                     @SuppressWarnings("unchecked")
                     var typedRecipe = (RecipeHolder<AccumulationRecipe>) (Object) recipe;
                     if (typedRecipe.value().hasSolute() && typedRecipe.value().getSolute().test(stack)) {
                         return Optional.of(typedRecipe);
                     }
                }
             }
        }

        return LevelUtil.getRecipesByType(recipeManager, this.type).stream().filter((entry) -> entry.value().hasSolute() && entry.value().getSolute().test(stack)).findFirst();
    }

    private Optional<RecipeHolder<AccumulationRecipe>> getRecipeFor(FluidStack stack, ServerLevel level, @Nullable ResourceKey<Recipe<?>> lastRecipe) {
        var recipeManager = level.getServer().getRecipeManager();
         if (lastRecipe != null) {
            var recipeOptional = recipeManager.byKey(lastRecipe);
             if(recipeOptional.isPresent()){
                var recipe = recipeOptional.get();
                if(recipe.value().getType() == this.type) {
                     @SuppressWarnings("unchecked")
                     var typedRecipe = (RecipeHolder<AccumulationRecipe>) (Object) recipe;
                     //test only the fluid without the (separate) solute item ingredient check that the recipe.matches() would.
                    if (typedRecipe.value().hasEvaporant()  && typedRecipe.value().getEvaporant().ingredient().test(stack)) {
                        return Optional.of(typedRecipe);
                    }
                }
             }
        }

        return LevelUtil.getRecipesByType(recipeManager, this.type).stream().filter((entry) -> entry.value().hasEvaporant() && entry.value().getEvaporant().ingredient().test(stack)).findFirst();
    }

    /**
     * This only checks ingredients, not fluids
     */
    public Optional<RecipeHolder<AccumulationRecipe>> getRecipeFor(ItemStack stack, ServerLevel level) {
        var optional = this.getRecipeFor(stack, level, this.lastRecipe);
        if (optional.isPresent()) {
            var recipeHolder = optional.get();
            this.lastRecipe = recipeHolder.id();
            return optional;
        } else {
            return Optional.empty();
        }
    }

    /**
     * This only checks fluids, not ingredients
     */
    public Optional<RecipeHolder<AccumulationRecipe>> getRecipeFor(FluidStack stack, ServerLevel level) {
        var optional = this.getRecipeFor(stack, level, this.lastRecipe);
        if (optional.isPresent()) {
            var recipeHolder = optional.get();
            this.lastRecipe = recipeHolder.id();
            return optional;
        } else {
            return Optional.empty();
        }
    }

    /**
     * This checks full recipe validity: ingredients + fluids
     */
    @Override
    public Optional<RecipeHolder<AccumulationRecipe>> getRecipeFor(ItemHandlerWithFluidRecipeInput container, ServerLevel level) {
        var recipe = this.internal.getRecipeFor(container, level);
        if (recipe.isPresent()) {
            this.lastRecipe = recipe.get().id();
        }

        return recipe;
    }
}