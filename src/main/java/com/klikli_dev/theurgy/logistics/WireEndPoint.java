// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.logistics;

import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public record WireEndPoint(BlockPos pos, ResourceKey<Level> level) {

    public static final Codec<WireEndPoint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    BlockPos.CODEC.fieldOf("pos").forGetter((r) -> r.pos),
                    ResourceKey.codec(Registries.DIMENSION).fieldOf("level").forGetter((r) -> r.level)
            ).apply(instance, WireEndPoint::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, WireEndPoint> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            WireEndPoint::pos,
            ResourceKey.streamCodec(Registries.DIMENSION),
            WireEndPoint::level,
            WireEndPoint::new
    );


    public static WireEndPoint load(CompoundTag tag) {
        return new WireEndPoint(BlockPos.of(tag.getLong("pos")), ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString("level"))));
    }

    public static @Nullable WireEndPoint load(ItemStack stack) {
        return stack.get(DataComponentRegistry.WIRE_END_POINT.get());
    }

    public static void removeFrom(ItemStack stack) {
        stack.remove(DataComponentRegistry.WIRE_END_POINT.get());
    }

    public void save(ItemStack stack) {
        stack.set(DataComponentRegistry.WIRE_END_POINT.get(), this);
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putLong("pos", this.pos.asLong());
        tag.putString("level", this.level.location().toString());
        return tag;
    }
}
