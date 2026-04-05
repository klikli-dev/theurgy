// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron;

import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A custom cached check
 */
class LiquefactionCachedCheck implements RecipeManager.CachedCheck<ItemHandlerWithFluidRecipeInput, LiquefactionRecipe> {

    private final RecipeType<LiquefactionRecipe> type;
    private final RecipeManager.CachedCheck<ItemHandlerWithFluidRecipeInput, LiquefactionRecipe> internal;
    @Nullable
    private ResourceKey<Recipe<?>> lastRecipe;

    public LiquefactionCachedCheck(RecipeType<LiquefactionRecipe> type) {
        this.type = type;
        this.internal = RecipeManager.createCheck(type);
    }

    private Optional<RecipeHolder<LiquefactionRecipe>> getRecipeFor(ItemStack stack, ServerLevel level, @Nullable ResourceKey<Recipe<?>> lastRecipe) {
        var recipeManager = level.getServer().getRecipeManager();
        if (lastRecipe != null) {
            var recipeOptional = recipeManager.byKey(lastRecipe);
            if (recipeOptional.isPresent()) {
                var recipe = recipeOptional.get();
                if (recipe.value().getType() == this.type) {
                    @SuppressWarnings("unchecked")
                    var typedRecipe = (RecipeHolder<LiquefactionRecipe>) recipe;
                    if (typedRecipe.value().getIngredient().test(stack)) {
                        return Optional.of(typedRecipe);
                    }
                }
            }
        }

        return recipeManager.recipeMap().byType(this.type).stream().filter((entry) -> entry.value().getIngredient().test(stack)).findFirst();
    }

    /**
     * This only checks ingredients, not fluids
     */
    public Optional<RecipeHolder<LiquefactionRecipe>> getRecipeFor(ItemStack stack, ServerLevel level) {
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
    public Optional<RecipeHolder<LiquefactionRecipe>> getRecipeFor(ItemHandlerWithFluidRecipeInput container, ServerLevel level) {
        var recipe = this.internal.getRecipeFor(container, level);
        if (recipe.isPresent()) {
            this.lastRecipe = recipe.get().id();
        }

        return recipe;
    }
}
