// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.fermentationvat;

import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
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
public class FermentationCachedCheck implements RecipeManager.CachedCheck<RecipeWrapperWithFluid, FermentationRecipe> {

    private final RecipeType<FermentationRecipe> type;
    private final RecipeManager.CachedCheck<RecipeWrapperWithFluid, FermentationRecipe> internal;
    @Nullable
    private ResourceLocation lastRecipe;

    public FermentationCachedCheck(RecipeType<FermentationRecipe> type) {
        this.type = type;
        this.internal = RecipeManager.createCheck(type);
    }

    private Optional<Pair<ResourceLocation, FermentationRecipe>> getRecipeFor(ItemStack stack, Level level, @Nullable ResourceLocation lastRecipe) {

        var recipeManager = level.getRecipeManager();
        var map = recipeManager.byType(this.type);
        if (lastRecipe != null) {
            var recipe = map.get(lastRecipe);
            //test only the ingredient without the (separate) fluid ingredient check that the recipe.matches() would.
            if (recipe != null && recipe.getIngredients().stream().anyMatch(i -> i.test(stack))) {
                return Optional.of(Pair.of(lastRecipe, recipe));
            }
        }

        return map.entrySet().stream().filter((entry) -> entry.getValue().getIngredients().stream().anyMatch(i -> i.test(stack))).findFirst().map((entry) -> Pair.of(entry.getKey(), entry.getValue()));
    }

    private Optional<Pair<ResourceLocation, FermentationRecipe>> getRecipeFor(FluidStack stack, Level level, @Nullable ResourceLocation lastRecipe) {

        var recipeManager = level.getRecipeManager();
        var map = recipeManager.byType(this.type);
        if (lastRecipe != null) {
            var recipe = map.get(lastRecipe);
            //test only the fluid without the (separate) item ingredients check that the recipe.matches() would.
            if (recipe != null && recipe.getFluid().test(stack)) {
                return Optional.of(Pair.of(lastRecipe, recipe));
            }
        }

        return map.entrySet().stream().filter((entry) -> entry.getValue().getFluid().test(stack)).findFirst().map((entry) -> Pair.of(entry.getKey(), entry.getValue()));
    }

    /**
     * This only checks ingredients, not fluids
     */
    public Optional<FermentationRecipe> getRecipeFor(ItemStack stack, Level level) {
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
    public Optional<FermentationRecipe> getRecipeFor(FluidStack stack, Level level) {
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
    public Optional<FermentationRecipe> getRecipeFor(RecipeWrapperWithFluid container, Level level) {
        var recipe = this.internal.getRecipeFor(container, level);
        if (recipe.isPresent()) {
            this.lastRecipe = recipe.get().getId();
        }

        return recipe;
    }
}
