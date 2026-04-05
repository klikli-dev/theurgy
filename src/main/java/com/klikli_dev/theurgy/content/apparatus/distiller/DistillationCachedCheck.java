// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.distiller;

import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerRecipeInput;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A custom cached check
 */
class DistillationCachedCheck implements RecipeManager.CachedCheck<ItemHandlerRecipeInput, DistillationRecipe> {

    private final RecipeType<DistillationRecipe> type;
    private final RecipeManager.CachedCheck<ItemHandlerRecipeInput, DistillationRecipe> internal;
    @Nullable
    private net.minecraft.resources.ResourceKey<net.minecraft.world.item.crafting.Recipe<?>> lastRecipe;

    public DistillationCachedCheck(RecipeType<DistillationRecipe> type) {
        this.type = type;
        this.internal = RecipeManager.createCheck(type);
    }

    private Optional<RecipeHolder<DistillationRecipe>> getRecipeFor(ItemStack stack, ServerLevel level, @Nullable net.minecraft.resources.ResourceKey<net.minecraft.world.item.crafting.Recipe<?>> lastRecipe) {
        var recipeManager = level.getServer().getRecipeManager();
        if (lastRecipe != null) {

            var recipe = recipeManager.byKey(lastRecipe).orElse(null);
            //test only the ingredient without the (separate) ingredient count check that the recipe.matches() would.
            //that means we call ingredient().test() instead of .test() (which would also match the count)
            if (recipe != null && recipe.value().getType() == this.type && ((DistillationRecipe) recipe.value()).getIngredient().ingredient().test(stack)) {
                return Optional.of((RecipeHolder<DistillationRecipe>) recipe);
            }
        }

        return recipeManager.recipeMap().byType(this.type).stream().filter((entry) -> entry.value().getIngredient().test(stack)).findFirst();
    }

    /**
     * This checks only the ingredient, not the ingredient count
     */
    public Optional<RecipeHolder<DistillationRecipe>> getRecipeFor(ItemStack stack, ServerLevel level) {
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
    public Optional<RecipeHolder<DistillationRecipe>> getRecipeFor(ItemHandlerRecipeInput container, ServerLevel level) {
        var recipe = this.internal.getRecipeFor(container, level);
        if (recipe.isPresent()) {
            this.lastRecipe = recipe.get().id();
        }

        return recipe;
    }
}
