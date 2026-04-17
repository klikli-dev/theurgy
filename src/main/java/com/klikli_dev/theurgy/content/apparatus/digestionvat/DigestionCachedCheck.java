// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.content.behaviour.crafting.LevelAwareRecipeCheck;
import com.klikli_dev.theurgy.content.recipe.DigestionRecipe;
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
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * A custom cached check
 */
public class DigestionCachedCheck implements LevelAwareRecipeCheck<ItemHandlerWithFluidRecipeInput, DigestionRecipe> {


    private final RecipeType<DigestionRecipe> type;
    private final RecipeManager.CachedCheck<ItemHandlerWithFluidRecipeInput, DigestionRecipe> internal;
    @Nullable
    private ResourceKey<Recipe<?>> lastRecipeForFluidStack;
    @Nullable
    private ResourceKey<Recipe<?>> lastRecipeForItemStack;
    @Nullable
    private ResourceKey<Recipe<?>> lastRecipeForItemStackCollection;
    @Nullable
    private ResourceKey<Recipe<?>> lastRecipeForItemHandler;

    private boolean noRecipeForLastItemStackCollectionInput;
    private boolean noRecipeForLastItemStackInput;
    private boolean noRecipeForLastFluidStackInput;
    /**
     * This flag works differently from the others - we don't compare the entire item handler, instead we reset this flag if the item handler content changes.
     */
    private boolean noRecipeForLastItemHandlerInput;

    private Collection<ItemStack> lastItemStackCollectionInput;
    private ItemStack lastItemStackInput;
    private FluidStack lastFluidStackInput;
    private long lastRecipeGeneration = -1;

    public DigestionCachedCheck(RecipeType<DigestionRecipe> type) {
        this.type = type;
        this.internal = RecipeManager.createCheck(type);
    }

    /**
     * Call this when the content of the item handler handed to {@link #getRecipeFor(ItemHandlerWithFluidRecipeInput, Level)} changes.
     */
    public void resetNoRecipeForLastItemHandlerInput() {
        this.noRecipeForLastItemHandlerInput = false;
    }

    private void refreshForRecipeReload() {
        var generation = TheurgyRecipeManager.get().getRecipeGeneration();
        if (this.lastRecipeGeneration == generation) {
            return;
        }

        this.lastRecipeGeneration = generation;
        this.noRecipeForLastItemStackCollectionInput = false;
        this.noRecipeForLastItemStackInput = false;
        this.noRecipeForLastFluidStackInput = false;
        this.noRecipeForLastItemHandlerInput = false;
        this.lastRecipeForFluidStack = null;
        this.lastRecipeForItemStack = null;
        this.lastRecipeForItemStackCollection = null;
        this.lastRecipeForItemHandler = null;
    }

    private boolean matchesRecipe(RecipeHolder<DigestionRecipe> recipe, Collection<ItemStack> input) {
        var ingredients = recipe.value().getIngredients();
        return input.stream().allMatch(stack ->
                ingredients.stream().anyMatch(ingredient -> ingredient.test(stack))
        );
    }

    private boolean isSameInput(Collection<ItemStack> input) {
        return Objects.equals(this.lastItemStackCollectionInput, input);
    }

    private boolean isSameInput(ItemStack input) {
        return ItemStack.matches(this.lastItemStackInput, input);
    }

    private boolean isSameInput(FluidStack input) {
        return FluidStack.matches(this.lastFluidStackInput, input);
    }

    private Optional<RecipeHolder<DigestionRecipe>> getRecipeFor(Collection<ItemStack> input, Level level, @Nullable ResourceKey<Recipe<?>> lastRecipe) {
        if (lastRecipe != null) {
            var recipeOptional = TheurgyRecipeManager.get().getRecipeByKey(this.type, lastRecipe, level);
            if (recipeOptional.isPresent()) {
                var recipe = recipeOptional.get();
                if (this.matchesRecipe(recipe, input)) {
                    return Optional.of(recipe);
                }
            }
        }

        return TheurgyRecipeManager.get().getRecipesByType(this.type, level).stream().filter((entry) -> this.matchesRecipe(entry, input)).findFirst();
    }

    private Optional<RecipeHolder<DigestionRecipe>> getRecipeFor(ItemStack stack, Level level, @Nullable ResourceKey<Recipe<?>> lastRecipe) {
        if (lastRecipe != null) {
            var recipeOptional = TheurgyRecipeManager.get().getRecipeByKey(this.type, lastRecipe, level);
            if (recipeOptional.isPresent()) {
                var recipe = recipeOptional.get();
                if (recipe.value().getIngredients().stream().anyMatch(i -> i.test(stack))) {
                    return Optional.of(recipe);
                }
            }
        }

        return TheurgyRecipeManager.get().getRecipesByType(this.type, level).stream().filter((entry) -> entry.value().getIngredients().stream().anyMatch(i -> i.test(stack))).findFirst();
    }

    private Optional<RecipeHolder<DigestionRecipe>> getRecipeFor(FluidStack stack, Level level, @Nullable ResourceKey<Recipe<?>> lastRecipe) {
        if (lastRecipe != null) {
            var recipeOptional = TheurgyRecipeManager.get().getRecipeByKey(this.type, lastRecipe, level);
            if (recipeOptional.isPresent()) {
                var recipe = recipeOptional.get();
                if (recipe.value().getFluid().ingredient().test(stack)) {
                    return Optional.of(recipe);
                }
            }
        }

        return TheurgyRecipeManager.get().getRecipesByType(this.type, level).stream().filter((entry) -> entry.value().getFluid().ingredient().test(stack)).findFirst();
    }


    /**
     * This only checks ingredients, including ingredients already present, not fluids
     */
    public Optional<RecipeHolder<DigestionRecipe>> getRecipeFor(Collection<ItemStack> input, Level level) {
        this.refreshForRecipeReload();
        if (this.noRecipeForLastItemStackCollectionInput && this.isSameInput(input)) {
            return Optional.empty();
        }

        var optional = this.getRecipeFor(input, level, this.lastRecipeForItemStackCollection);
        if (optional.isPresent()) {
            var recipeHolder = optional.get();
            this.lastRecipeForItemStackCollection = recipeHolder.id();
            this.noRecipeForLastItemStackCollectionInput = false;
            this.lastItemStackCollectionInput = input;
            return optional;
        } else {
            this.noRecipeForLastItemStackCollectionInput = true;
            this.lastItemStackCollectionInput = input;
            return Optional.empty();
        }
    }

    /**
     * This only checks ingredients, not fluids
     */
    public Optional<RecipeHolder<DigestionRecipe>> getRecipeFor(ItemStack stack, Level level) {
        this.refreshForRecipeReload();
        if (this.noRecipeForLastItemStackInput && this.isSameInput(stack)) {
            return Optional.empty();
        }

        var optional = this.getRecipeFor(stack, level, this.lastRecipeForItemStack);
        if (optional.isPresent()) {
            var recipeHolder = optional.get();
            this.lastRecipeForItemStack = recipeHolder.id();
            this.noRecipeForLastItemStackInput = false;
            this.lastItemStackInput = stack;
            return optional;
        } else {
            this.noRecipeForLastItemStackInput = true;
            this.lastItemStackInput = stack;
            return Optional.empty();
        }
    }

    /**
     * This only checks fluids, not ingredients
     */
    public Optional<RecipeHolder<DigestionRecipe>> getRecipeFor(FluidStack stack, Level level) {
        this.refreshForRecipeReload();
        if (this.noRecipeForLastFluidStackInput && this.isSameInput(stack)) {
            return Optional.empty();
        }

        var optional = this.getRecipeFor(stack, level, this.lastRecipeForFluidStack);
        if (optional.isPresent()) {
            var recipeHolder = optional.get();
            this.lastRecipeForFluidStack = recipeHolder.id();
            this.noRecipeForLastFluidStackInput = false;
            this.lastFluidStackInput = stack;
            return optional;
        } else {
            this.noRecipeForLastFluidStackInput = true;
            this.lastFluidStackInput = stack;
            return Optional.empty();
        }
    }

    /**
     * This checks full recipe validity: ingredients + fluids
     */
    @Override
    public @NotNull Optional<RecipeHolder<DigestionRecipe>> getRecipeFor(@NotNull ItemHandlerWithFluidRecipeInput container, @NotNull Level level) {
        this.refreshForRecipeReload();
        if (this.noRecipeForLastItemHandlerInput) {
            return Optional.empty();
        }

        Optional<RecipeHolder<DigestionRecipe>> optional;
        if (level instanceof ServerLevel serverLevel) {
            optional = this.internal.getRecipeFor(container, serverLevel);
        } else {
            optional = TheurgyRecipeManager.get().getRecipeFor(this.type, container, level, this.lastRecipeForItemHandler);
        }

        if (optional.isPresent()) {
            this.lastRecipeForItemHandler = optional.get().id();
            this.noRecipeForLastItemHandlerInput = false;
            return optional;
        } else {
            this.noRecipeForLastItemHandlerInput = true;
            return Optional.empty();
        }
    }
}
