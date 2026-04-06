// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.recipe;

import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TheurgyRecipeManager {
    private static final TheurgyRecipeManager INSTANCE = new TheurgyRecipeManager();
    private static final List<RecipeType<?>> SYNCED_RECIPE_TYPES = List.of(
            RecipeTypeRegistry.CALCINATION.get(),
            RecipeTypeRegistry.LIQUEFACTION.get(),
            RecipeTypeRegistry.DISTILLATION.get(),
            RecipeTypeRegistry.INCUBATION.get(),
            RecipeTypeRegistry.ACCUMULATION.get(),
            RecipeTypeRegistry.CATALYSATION.get(),
            RecipeTypeRegistry.REFORMATION.get(),
            RecipeTypeRegistry.FERMENTATION.get(),
            RecipeTypeRegistry.DIGESTION.get()
    );

    private final Map<RecipeType<?>, List<RecipeHolder<?>>> clientRecipeCache = new ConcurrentHashMap<>();
    private final Map<RecipeType<?>, Map<ResourceKey<Recipe<?>>, RecipeHolder<?>>> clientRecipeByKeyCache = new ConcurrentHashMap<>();
    private volatile long recipeGeneration;

    private TheurgyRecipeManager() {
    }

    public static TheurgyRecipeManager get() {
        return INSTANCE;
    }

    public long getRecipeGeneration() {
        return this.recipeGeneration;
    }

    @SuppressWarnings("unchecked")
    public <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> getRecipesByType(RecipeType<T> type, Level level) {
        if (level == null) {
            return List.of();
        }

        if (level.isClientSide()) {
            return (List<RecipeHolder<T>>) (List<?>) this.clientRecipeCache.getOrDefault(type, List.of());
        }

        return new ArrayList<>(level.getServer().getRecipeManager().recipeMap().byType(type));
    }

    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> getRecipeFor(RecipeType<T> type, C input, Level level) {
        return this.getRecipeFor(type, input, level, null);
    }

    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> getRecipeFor(RecipeType<T> type, C input, Level level, ResourceKey<Recipe<?>> lastRecipe) {
        if (level == null) {
            return Optional.empty();
        }

        if (lastRecipe != null) {
            var cachedRecipe = this.getRecipeByKey(type, lastRecipe, level);
            if (cachedRecipe.isPresent() && cachedRecipe.get().value().matches(input, level)) {
                return cachedRecipe;
            }
        }

        if (level.isClientSide()) {
            return this.getRecipesByType(type, level).stream()
                    .filter(recipe -> recipe.value().matches(input, level))
                    .findFirst();
        }

        return level.getServer().getRecipeManager().getRecipeFor(type, input, level);
    }

    @SuppressWarnings("unchecked")
    public <T extends Recipe<?>> Optional<RecipeHolder<T>> getRecipeByKey(RecipeType<T> type, ResourceKey<Recipe<?>> key, Level level) {
        if (key == null || level == null) {
            return Optional.empty();
        }

        if (level.isClientSide()) {
            var typeMap = this.clientRecipeByKeyCache.get(type);
            if (typeMap != null) {
                return Optional.ofNullable((RecipeHolder<T>) typeMap.get(key));
            }
            return Optional.empty();
        }

        return level.getServer().getRecipeManager().byKey(key)
                .filter(recipe -> recipe.value().getType() == type)
                .map(recipe -> (RecipeHolder<T>) recipe);
    }

    public void onDatapackSync(OnDatapackSyncEvent event) {
        this.recipeGeneration++;
        SYNCED_RECIPE_TYPES.forEach(event::sendRecipes);
    }

    public void onRecipesReceived(RecipesReceivedEvent event) {
        this.clearClientCache();

        for (var type : SYNCED_RECIPE_TYPES) {
            this.storeClientRecipesUnchecked(event.getRecipeMap(), type);
        }
    }

    public void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        this.clearClientCache();
    }

    private void clearClientCache() {
        this.recipeGeneration++;
        this.clientRecipeCache.clear();
        this.clientRecipeByKeyCache.clear();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void storeClientRecipesUnchecked(RecipeMap recipeMap, RecipeType<?> type) {
        this.storeClientRecipes((RecipeMap) recipeMap, (RecipeType) type);
    }

    private <I extends RecipeInput, T extends Recipe<I>> void storeClientRecipes(RecipeMap recipeMap, RecipeType<T> type) {
        this.storeClientRecipes(type, recipeMap.byType(type));
    }

    private <T extends Recipe<?>> void storeClientRecipes(RecipeType<T> type, Iterable<RecipeHolder<T>> recipes) {
        var recipeList = new ArrayList<RecipeHolder<?>>();
        var recipesByKey = new ConcurrentHashMap<ResourceKey<Recipe<?>>, RecipeHolder<?>>();

        for (var recipe : recipes) {
            recipeList.add(recipe);
            recipesByKey.put(recipe.id(), recipe);
        }

        this.clientRecipeCache.put(type, List.copyOf(recipeList));
        this.clientRecipeByKeyCache.put(type, recipesByKey);
    }
}
