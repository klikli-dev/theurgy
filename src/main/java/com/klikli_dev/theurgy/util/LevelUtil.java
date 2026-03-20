// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.server.ServerLifecycleHooks;


public class LevelUtil {

    /**
     * Attempts to get a level if there is no context.
     * This is generally needed if items want to access recipes but are not provided a world context.
     * Very hacky, do not use unless absolutely necessary.
     */
    public static Level getLevelWithoutContext() {

        if (FMLLoader.getDist() == Dist.CLIENT) {
            return DistHelper.getClientLevel();
        }
        return getOverworldServerLevel();
    }

    private static Level getOverworldServerLevel() {
        var server = ServerLifecycleHooks.getCurrentServer();
        return server == null ? null : server.getLevel(Level.OVERWORLD);
    }

    private static class DistHelper {
        private static Level getClientLevel() {
            return Minecraft.getInstance().level;
        }
        
        private static net.minecraft.world.item.crafting.RecipeManager getRecipeManager() {
            //In 1.21.3 ClientPacketListener does not expose getRecipeManager() publically (or removed).
            //Level has recipeAccess() which returns RecipeAccess, implemented by RecipeManager.
            return (net.minecraft.world.item.crafting.RecipeManager) Minecraft.getInstance().level.recipeAccess();
        }
    }
    
    public static net.minecraft.world.item.crafting.RecipeManager getRecipeManager(Level level) {
         if (level.isClientSide()) {
             return DistHelper.getRecipeManager();
         } else {
             return ((net.minecraft.server.level.ServerLevel)level).getServer().getRecipeManager();
         }
    }

    public static <C extends net.minecraft.world.item.crafting.RecipeInput, T extends net.minecraft.world.item.crafting.Recipe<C>> java.util.List<net.minecraft.world.item.crafting.RecipeHolder<T>> getRecipesByType(net.minecraft.world.item.crafting.RecipeManager manager, net.minecraft.world.item.crafting.RecipeType<T> type) {
        return manager.getRecipes().stream()
                .filter(recipe -> recipe.value().getType() == type)
                .map(recipe -> (net.minecraft.world.item.crafting.RecipeHolder<T>) recipe)
                .toList();
    }
}
