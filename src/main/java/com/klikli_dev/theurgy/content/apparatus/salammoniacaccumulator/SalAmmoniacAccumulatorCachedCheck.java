// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator;

import com.klikli_dev.theurgy.content.recipe.AccumulationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import com.klikli_dev.theurgy.content.storage.FluidStorageHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A custom cached check
 */
class SalAmmoniacAccumulatorCachedCheck implements RecipeManager.CachedCheck<ItemHandlerWithFluidRecipeInput, AccumulationRecipe> {

    private final RecipeType<AccumulationRecipe> type;
    @Nullable
    private ResourceKey<Recipe<?>> lastRecipe;

    public SalAmmoniacAccumulatorCachedCheck(RecipeType<AccumulationRecipe> type) {
        this.type = type;
    }

    private FluidStack normalizeFluid(FluidStack stack) {
        if (stack.isEmpty() || stack.isComponentsPatchEmpty()) {
            return stack;
        }

        return new FluidStack(stack.getFluid(), stack.getAmount());
    }

    private boolean matchesEvaporant(AccumulationRecipe recipe, FluidStack normalizedStack) {
        return recipe.hasEvaporant() && recipe.evaporant().ingredient().test(normalizedStack);
    }

    private boolean matches(ItemStack item, FluidStack normalizedFluid, AccumulationRecipe recipe) {
        boolean evaporantMatches = !recipe.hasEvaporant() || recipe.evaporant().test(normalizedFluid);
        boolean soluteMatches =
                item.isEmpty() && !recipe.hasSolute() ||
                        recipe.hasSolute() && recipe.solute().test(item);

        return soluteMatches && evaporantMatches;
    }

    private Optional<RecipeHolder<AccumulationRecipe>> getRecipeFor(ItemStack stack, ServerLevel level, @Nullable ResourceKey<Recipe<?>> lastRecipe) {
        var recipeManager = level.getServer().getRecipeManager();
        if (lastRecipe != null) {
            var recipeOptional = recipeManager.byKey(lastRecipe);
            if (recipeOptional.isPresent()) {
                var recipe = recipeOptional.get();
                if (recipe.value().getType() == this.type) {
                    @SuppressWarnings("unchecked")
                    var typedRecipe = (RecipeHolder<AccumulationRecipe>) recipe;
                    if (typedRecipe.value().hasSolute() && typedRecipe.value().solute().test(stack)) {
                        return Optional.of(typedRecipe);
                    }
                }
            }
        }

        return recipeManager.recipeMap().byType(this.type).stream().filter((entry) -> entry.value().hasSolute() && entry.value().solute().test(stack)).findFirst();
    }

    private Optional<RecipeHolder<AccumulationRecipe>> getRecipeFor(FluidStack stack, ServerLevel level, @Nullable ResourceKey<Recipe<?>> lastRecipe) {
        var recipeManager = level.getServer().getRecipeManager();
        var normalizedStack = this.normalizeFluid(stack);
        if (lastRecipe != null) {
            var recipeOptional = recipeManager.byKey(lastRecipe);
            if (recipeOptional.isPresent()) {
                var recipe = recipeOptional.get();
                if (recipe.value().getType() == this.type) {
                    @SuppressWarnings("unchecked")
                    var typedRecipe = (RecipeHolder<AccumulationRecipe>) recipe;
                    //test only the fluid without the (separate) solute item ingredient check that the recipe.matches() would.
                    if (this.matchesEvaporant(typedRecipe.value(), normalizedStack)) {
                        return Optional.of(typedRecipe);
                    }
                }
            }
        }

        return recipeManager.recipeMap().byType(this.type).stream().filter((entry) -> this.matchesEvaporant(entry.value(), normalizedStack)).findFirst();
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
        var recipeManager = level.getServer().getRecipeManager();
        var item = container.getItem(0);
        var fluid = this.normalizeFluid(FluidStorageHelper.getFluidInTank(container.getTank(), 0));
        if (this.lastRecipe != null) {
            var recipeOptional = recipeManager.byKey(this.lastRecipe);
            if (recipeOptional.isPresent()) {
                var recipe = recipeOptional.get();
                if (recipe.value().getType() == this.type) {
                    @SuppressWarnings("unchecked")
                    var typedRecipe = (RecipeHolder<AccumulationRecipe>) recipe;
                    if (this.matches(item, fluid, typedRecipe.value())) {
                        return Optional.of(typedRecipe);
                    }
                }
            }
        }

        var recipe = recipeManager.recipeMap().byType(this.type).stream().filter((entry) -> this.matches(item, fluid, entry.value())).findFirst();
        recipe.ifPresent(value -> this.lastRecipe = value.id());
        return recipe;
    }
}
