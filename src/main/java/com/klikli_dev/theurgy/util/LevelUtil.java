// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;


public class LevelUtil {

    /**
     * Attempts to get a level if there is no context.
     * This is generally needed if items want to access recipes but are not provided a world context.
     * Very hacky, do not use unless absolutely necessary.
     * <p>
     * Note: the client-only fallback lives in {@link ClientLevelUtil} on purpose -- referencing
     * client-only classes (like ClientLevel) directly in this method would make the dedicated
     * server throw a NoClassDefFoundError as soon as this method is verified/linked, even though
     * that branch would never be taken on the server.
     */
    public static Level getLevelWithoutContext() {
        Level serverLevel = getOverworldServerLevel();
        if (serverLevel != null) {
            return serverLevel;
        }
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            return ClientLevelUtil.getClientLevel();
        }
        return null;
    }

    private static Level getOverworldServerLevel() {
        var server = ServerLifecycleHooks.getCurrentServer();
        return server == null ? null : server.getLevel(Level.OVERWORLD);
    }
}
