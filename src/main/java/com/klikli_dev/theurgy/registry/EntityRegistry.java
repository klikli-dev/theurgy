// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.entity.FollowProjectile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE,
            Theurgy.MODID);
    public static final Supplier<EntityType<FollowProjectile>> FOLLOW_PROJECTILE = register(
            "follow_projectile",
            EntityType.Builder.<FollowProjectile>of(FollowProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .noSave()
                    .fireImmune()
                    .setTrackingRange(10)
                    .setShouldReceiveVelocityUpdates(true)
    );

    static <T extends Entity> Supplier<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(Theurgy.MODID + ":" + name));
    }


}
