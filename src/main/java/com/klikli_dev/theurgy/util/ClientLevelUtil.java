// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

/**
 * Isolates access to client-only classes (e.g. {@link net.minecraft.client.multiplayer.ClientLevel}).
 * Keeping this in its own class prevents the dedicated server from having to resolve those classes
 * during bytecode verification of {@link LevelUtil}, which would throw a NoClassDefFoundError even
 * if the client-only branch is never actually executed on the server.
 */
class ClientLevelUtil {

    private ClientLevelUtil() {
    }

    public static Level getClientLevel() {
        return Minecraft.getInstance().level;
    }
}
