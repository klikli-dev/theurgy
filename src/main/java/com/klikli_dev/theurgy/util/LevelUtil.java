/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.server.ServerLifecycleHooks;

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
    }
}
