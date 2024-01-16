// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.content.recipe.DigestionRecipe;
import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
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
public class DigestionCachedCheck implements RecipeManager.CachedCheck<RecipeWrapperWithFluid, DigestionRecipe> {

    private final RecipeType<DigestionRecipe> type;
    private final RecipeManager.CachedCheck<RecipeWrapperWithFluid, DigestionRecipe> internal;
    @Nullable
    private ResourceLocation lastRecipe;

    public DigestionCachedCheck(RecipeType<DigestionRecipe> type) {
        this.type = type;
        this.internal = RecipeManager.createCheck(type);
    }

    private Optional<Pair<ResourceLocation, RecipeHolder<DigestionRecipe>>> getRecipeFor(ItemStack stack, Level level, @Nullable ResourceLocation lastRecipe) {

        var recipeManager = level.getRecipeManager();
        var map = recipeManager.byType(this.type);
        if (lastRecipe != null) {
            var recipe = map.get(lastRecipe);
            //test only the ingredient without the (separate) fluid ingredient check that the recipe.matches() would.
            if (recipe != null && recipe.value().getIngredients().stream().anyMatch(i -> i.test(stack))) {
                return Optional.of(Pair.of(lastRecipe, recipe));
            }
        }

        return map.entrySet().stream().filter((entry) -> entry.getValue().value().getIngredients().stream().anyMatch(i -> i.test(stack))).findFirst().map((entry) -> Pair.of(entry.getKey(), entry.getValue()));
    }

    private Optional<Pair<ResourceLocation, RecipeHolder<DigestionRecipe>>> getRecipeFor(FluidStack stack, Level level, @Nullable ResourceLocation lastRecipe) {

        var recipeManager = level.getRecipeManager();
        var map = recipeManager.byType(this.type);
        if (lastRecipe != null) {
            var recipe = map.get(lastRecipe);
            //test only the fluid without the (separate) item ingredients check that the recipe.matches() would.
            if (recipe != null && recipe.value().getFluid().test(stack)) {
                return Optional.of(Pair.of(lastRecipe, recipe));
            }
        }

        return map.entrySet().stream().filter((entry) -> entry.getValue().value().getFluid().test(stack)).findFirst().map((entry) -> Pair.of(entry.getKey(), entry.getValue()));
    }

    /**
     * This only checks ingredients, not fluids
     */
    public Optional<RecipeHolder<DigestionRecipe>> getRecipeFor(ItemStack stack, Level level) {
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
    public Optional<RecipeHolder<DigestionRecipe>> getRecipeFor(FluidStack stack, Level level) {
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
    public Optional<RecipeHolder<DigestionRecipe>> getRecipeFor(RecipeWrapperWithFluid container, Level level) {
        var recipe = this.internal.getRecipeFor(container, level);
        if (recipe.isPresent()) {
            this.lastRecipe = recipe.get().id();
        }

        return recipe;
    }
}
