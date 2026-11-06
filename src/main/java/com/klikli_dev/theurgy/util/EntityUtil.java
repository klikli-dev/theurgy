// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.concurrent.atomic.AtomicInteger;

public class EntityUtil {

    public static void spawnEntityClientSide(Level level, Entity entity, boolean onlyInTickingChunks) {
        DistHelper.spawnEntityClientSide(level, entity, onlyInTickingChunks);
    }

    private static class DistHelper {

        //Server entity ids are positive, so negative ids can never collide with an entity sent by the server.
        private static final AtomicInteger NEXT_CLIENT_ONLY_ID = new AtomicInteger(Integer.MIN_VALUE);

        public static void spawnEntityClientSide(Level level, Entity entity, boolean onlyInTickingChunks) {
            if (level instanceof ClientLevel clientLevel) {
                var sectionPos = SectionPos.asLong(entity.blockPosition());
                var section = clientLevel.entityStorage.sectionStorage.getOrCreateSection(sectionPos);
                if(onlyInTickingChunks && !section.getStatus().isTicking()){
                    return;
                }
                entity.setId(NEXT_CLIENT_ONLY_ID.getAndIncrement());
                clientLevel.addEntity(entity); //client only spawn of entity
            }
        }
    }
}
