// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron;

import com.klikli_dev.theurgy.content.behaviour.crafting.LevelAwareRecipeCheck;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
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
class LiquefactionCachedCheck implements LevelAwareRecipeCheck<ItemHandlerWithFluidRecipeInput, LiquefactionRecipe> {

    private final RecipeType<LiquefactionRecipe> type;
    private final RecipeManager.CachedCheck<ItemHandlerWithFluidRecipeInput, LiquefactionRecipe> internal;
    @Nullable
    private ResourceKey<Recipe<?>> lastRecipe;

    public LiquefactionCachedCheck(RecipeType<LiquefactionRecipe> type) {
        this.type = type;
        this.internal = RecipeManager.createCheck(type);
    }

    private Optional<RecipeHolder<LiquefactionRecipe>> getRecipeFor(ItemStack stack, Level level, @Nullable ResourceKey<Recipe<?>> lastRecipe) {
        if (lastRecipe != null) {
            var recipeOptional = TheurgyRecipeManager.get().getRecipeByKey(this.type, lastRecipe, level);
            if (recipeOptional.isPresent()) {
                var recipe = recipeOptional.get();
                if (recipe.value().getIngredient().test(stack)) {
                    return Optional.of(recipe);
                }
            }
        }

        return TheurgyRecipeManager.get().getRecipesByType(this.type, level).stream().filter((entry) -> entry.value().getIngredient().test(stack)).findFirst();
    }

    /**
     * This only checks ingredients, not fluids
     */
    public Optional<RecipeHolder<LiquefactionRecipe>> getRecipeFor(ItemStack stack, Level level) {
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
    public Optional<RecipeHolder<LiquefactionRecipe>> getRecipeFor(ItemHandlerWithFluidRecipeInput container, Level level) {
        Optional<RecipeHolder<LiquefactionRecipe>> recipe;
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
