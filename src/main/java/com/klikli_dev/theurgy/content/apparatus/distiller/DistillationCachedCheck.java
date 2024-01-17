// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.distiller;

import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A custom cached check
 */
class DistillationCachedCheck implements RecipeManager.CachedCheck<RecipeWrapper, DistillationRecipe> {

    private final RecipeType<DistillationRecipe> type;
    private final RecipeManager.CachedCheck<RecipeWrapper, DistillationRecipe> internal;
    @Nullable
    private ResourceLocation lastRecipe;

    public DistillationCachedCheck(RecipeType<DistillationRecipe> type) {
        this.type = type;
        this.internal = RecipeManager.createCheck(type);
    }

    private Optional<Pair<ResourceLocation, RecipeHolder<DistillationRecipe>>> getRecipeFor(ItemStack stack, Level level, @Nullable ResourceLocation lastRecipe) {

        var recipeManager = level.getRecipeManager();
        var map = recipeManager.byType(this.type);
        if (lastRecipe != null) {
            var recipe = map.get(lastRecipe);
            //test only the ingredient without the (separate) ingredient count check that the recipe.matches() would.
            if (recipe != null && recipe.value().getIngredient().test(stack)) {
                return Optional.of(Pair.of(lastRecipe, recipe));
            }
        }

        return map.entrySet().stream().filter((entry) -> entry.getValue().value().getIngredient().test(stack)).findFirst().map((entry) -> Pair.of(entry.getKey(), entry.getValue()));
    }

    /**
     * This checks only the ingredient, not the ingredient count
     */
    public Optional<RecipeHolder<DistillationRecipe>> getRecipeFor(ItemStack stack, Level level) {
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
     * This checks full recipe validity: ingredients + ingredient count
     */
    @Override
    public Optional<RecipeHolder<DistillationRecipe>> getRecipeFor(RecipeWrapper container, Level level) {
        var recipe = this.internal.getRecipeFor(container, level);
        if (recipe.isPresent()) {
            this.lastRecipe = recipe.get().id();
        }

        return recipe;
    }
}
