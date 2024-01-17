// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityUtil {

    public static void spawnEntityClientSide(Level level, Entity entity) {
        DistHelper.spawnEntityClientSide(level, entity);
    }

    private static class DistHelper {

        public static void spawnEntityClientSide(Level level, Entity entity) {
            if (level instanceof ClientLevel clientLevel) {
                clientLevel.addEntity(entity); //client only spawn of entity
            }
        }
    }
}
