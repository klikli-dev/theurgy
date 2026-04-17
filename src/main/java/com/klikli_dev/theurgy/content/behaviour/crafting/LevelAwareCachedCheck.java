// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.crafting;

import com.klikli_dev.theurgy.recipe.TheurgyRecipeManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LevelAwareCachedCheck<W extends RecipeInput, R extends Recipe<W>> implements LevelAwareRecipeCheck<W, R> {
    private final RecipeType<R> type;
    private final RecipeManager.CachedCheck<W, R> serverCheck;
    @Nullable
    private ResourceKey<Recipe<?>> lastRecipe;

    public LevelAwareCachedCheck(RecipeType<R> type) {
        this.type = type;
        this.serverCheck = RecipeManager.createCheck(type);
    }

    @Override
    public Optional<RecipeHolder<R>> getRecipeFor(W input, Level level) {
        Optional<RecipeHolder<R>> recipe;
        if (level instanceof ServerLevel serverLevel) {
            recipe = this.serverCheck.getRecipeFor(input, serverLevel);
        } else {
            recipe = TheurgyRecipeManager.get().getRecipeFor(this.type, input, level, this.lastRecipe);
        }

        recipe.ifPresent(value -> this.lastRecipe = value.id());
        return recipe;
    }
}
