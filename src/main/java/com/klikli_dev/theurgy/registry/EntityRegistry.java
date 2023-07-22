/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.entity.FollowProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
            Theurgy.MODID);

    static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(Theurgy.MODID + ":" + name));
    }

    public static final RegistryObject<EntityType<FollowProjectile>> FOLLOW_PROJECTILE = register(
            "follow_projectile",
            EntityType.Builder.<FollowProjectile>of(FollowProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).noSave().fireImmune()
                    .setTrackingRange(10)
                    .setShouldReceiveVelocityUpdates(true).setCustomClientFactory(FollowProjectile::new));


}
