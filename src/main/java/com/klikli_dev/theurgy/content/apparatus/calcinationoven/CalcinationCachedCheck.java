// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.calcinationoven;

import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerRecipeInput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A custom cached check
 */
class CalcinationCachedCheck implements RecipeManager.CachedCheck<ItemHandlerRecipeInput, CalcinationRecipe> {

    private final RecipeType<CalcinationRecipe> type;
    private final RecipeManager.CachedCheck<ItemHandlerRecipeInput, CalcinationRecipe> internal;
    @Nullable
    private ResourceLocation lastRecipe;

    public CalcinationCachedCheck(RecipeType<CalcinationRecipe> type) {
        this.type = type;
        this.internal = RecipeManager.createCheck(type);
    }

    private Optional<RecipeHolder<CalcinationRecipe>> getRecipeFor(ItemStack stack, Level level, @Nullable ResourceLocation lastRecipe) {
        var recipeManager = level.getRecipeManager();
        if (lastRecipe != null) {

            var recipe = recipeManager.byKeyTyped(this.type, lastRecipe);
            //test only the ingredient within the sized ingredient to allow to find recipes even for too small stack sizes
            if (recipe != null && recipe.value().sizedIngredient().ingredient().test(stack)) {
                return Optional.of(recipe);
            }
        }

        return recipeManager.byType(this.type).stream().filter((entry) -> entry.value().sizedIngredient().ingredient().test(stack)).findFirst();
    }

    /**
     * This only checks itemstacks independent of count (ignoring the size of sized ingredients)
     */
    public Optional<RecipeHolder<CalcinationRecipe>> getRecipeFor(ItemStack stack, Level level) {
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
     * This checks full recipe validity (including sized ingredients)
     */
    @Override
    public @NotNull Optional<RecipeHolder<CalcinationRecipe>> getRecipeFor(@NotNull ItemHandlerRecipeInput container, @NotNull Level level) {
        var recipe = this.internal.getRecipeFor(container, level);
        if (recipe.isPresent()) {
            this.lastRecipe = recipe.get().id();
        }

        return recipe;
    }
}
