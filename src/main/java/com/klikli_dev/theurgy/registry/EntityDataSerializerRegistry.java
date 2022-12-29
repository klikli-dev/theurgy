/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityDataSerializerRegistry {
    public static DeferredRegister<DataSerializerEntry> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.DATA_SERIALIZERS, Theurgy.MODID);

    public static final RegistryObject<DataSerializerEntry> VEC3_FLOAT = ENTITY_DATA_SERIALIZERS.register("vec3_float",
            () -> new DataSerializerEntry(new EntityDataSerializer<Vec3>() {
                @Override
                public void write(FriendlyByteBuf buf, Vec3 vec) {
                    buf.writeFloat((float) vec.x);
                    buf.writeFloat((float) vec.y);
                    buf.writeFloat((float) vec.z);
                }

                @Override
                public Vec3 read(FriendlyByteBuf buf) {
                    return new Vec3(buf.readFloat(), buf.readFloat(), buf.readFloat());
                }

                @Override
                public Vec3 copy(Vec3 vec) {
                    return vec;
                }
            }));
}