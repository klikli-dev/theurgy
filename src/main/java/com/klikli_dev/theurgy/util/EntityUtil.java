// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import com.klikli_dev.theurgy.TheurgyConstants;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityUtil {

    public static void spawnEntityClientSide(Level level, Entity entity) {
        DistHelper.spawnEntityClientSide(level, entity);
    }

    private static class DistHelper {

        public static void spawnEntityClientSide(Level level, Entity entity) {
            if (level instanceof ClientLevel clientLevel) {
                clientLevel.putNonPlayerEntity(entity.getId(), entity); //client only spawn of entity
            }
        }
    }
}
