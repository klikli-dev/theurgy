// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.crafting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import java.util.Optional;

public interface LevelAwareRecipeCheck<W extends RecipeInput, R extends Recipe<W>> extends RecipeManager.CachedCheck<W, R> {
    Optional<RecipeHolder<R>> getRecipeFor(W input, Level level);

    @Override
    default Optional<RecipeHolder<R>> getRecipeFor(W input, ServerLevel level) {
        return this.getRecipeFor(input, (Level) level);
    }
}
