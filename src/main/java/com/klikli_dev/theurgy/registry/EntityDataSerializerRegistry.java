// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class EntityDataSerializerRegistry {
    public static DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Theurgy.MODID);

    public static final Supplier<EntityDataSerializer<Vec3>> VEC3_FLOAT = ENTITY_DATA_SERIALIZERS.register("vec3_float", () -> EntityDataSerializer.simple((buf, vec) -> {
        buf.writeFloat((float) vec.x);
        buf.writeFloat((float) vec.y);
        buf.writeFloat((float) vec.z);
    }, (buf) -> new Vec3(buf.readFloat(), buf.readFloat(), buf.readFloat())));

}
