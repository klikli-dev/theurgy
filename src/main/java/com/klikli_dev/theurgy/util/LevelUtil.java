// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Optional;


public class LevelUtil {

    /**
     * Attempts to get a level if there is no context.
     * This is generally needed if items want to access recipes but are not provided a world context.
     * Very hacky, do not use unless absolutely necessary.
     */
    public static Level getLevelWithoutContext() {
        Level serverLevel = getOverworldServerLevel();
        if (serverLevel != null) {
            return serverLevel;
        }
        return Minecraft.getInstance().level;
    }

    private static Level getOverworldServerLevel() {
        var server = ServerLifecycleHooks.getCurrentServer();
        return server == null ? null : server.getLevel(Level.OVERWORLD);
    }
}
