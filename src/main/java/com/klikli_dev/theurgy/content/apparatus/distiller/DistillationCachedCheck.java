// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.distiller;

import com.klikli_dev.theurgy.content.behaviour.crafting.LevelAwareRecipeCheck;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerRecipeInput;
import com.klikli_dev.theurgy.recipe.TheurgyRecipeManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A custom cached check
 */
class DistillationCachedCheck implements LevelAwareRecipeCheck<ItemHandlerRecipeInput, DistillationRecipe> {

    private final RecipeType<DistillationRecipe> type;
    private final RecipeManager.CachedCheck<ItemHandlerRecipeInput, DistillationRecipe> internal;
    @Nullable
    private ResourceKey<Recipe<?>> lastRecipe;

    public DistillationCachedCheck(RecipeType<DistillationRecipe> type) {
        this.type = type;
        this.internal = RecipeManager.createCheck(type);
    }

    private Optional<RecipeHolder<DistillationRecipe>> getRecipeFor(ItemStack stack, Level level, @Nullable ResourceKey<Recipe<?>> lastRecipe) {
        if (lastRecipe != null) {

            var recipe = TheurgyRecipeManager.get().getRecipeByKey(this.type, lastRecipe, level).orElse(null);
            //test only the ingredient without the (separate) ingredient count check that the recipe.matches() would.
            //that means we call ingredient().test() instead of .test() (which would also match the count)
            if (recipe != null && recipe.value().getIngredient().ingredient().test(stack)) {
                return Optional.of(recipe);
            }
        }

        return TheurgyRecipeManager.get().getRecipesByType(this.type, level).stream().filter((entry) -> entry.value().getIngredient().ingredient().test(stack)).findFirst();
    }

    /**
     * This checks only the ingredient, not the ingredient count
     */
    public Optional<RecipeHolder<DistillationRecipe>> getRecipeFor(ItemStack stack, Level level) {
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
     * This checks full recipe validity: ingredients + ingredient count
     */
    @Override
    public Optional<RecipeHolder<DistillationRecipe>> getRecipeFor(ItemHandlerRecipeInput container, Level level) {
        Optional<RecipeHolder<DistillationRecipe>> recipe;
        if (level instanceof ServerLevel serverLevel) {
            recipe = this.internal.getRecipeFor(container, serverLevel);
        } else {
            recipe = TheurgyRecipeManager.get().getRecipeFor(this.type, container, level, this.lastRecipe);
        }

        if (recipe.isPresent()) {
            this.lastRecipe = recipe.get().id();
        }

        return recipe;
    }
}
