// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.logistics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record Wire(BlockPos from, BlockPos to) {
    public static final Codec<Wire> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("from").forGetter(Wire::from),
            BlockPos.CODEC.fieldOf("to").forGetter(Wire::to)
    ).apply(instance, Wire::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Wire> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            (w) -> w.from,
            BlockPos.STREAM_CODEC,
            (w) -> w.to,
            Wire::new
    );

    public Wire(BlockPos from, BlockPos to) {
        this.from = from.immutable();
        this.to = to.immutable();
    }

    public static Wire load(CompoundTag tag) {
        return new Wire(BlockPos.of(tag.getLong("from")), BlockPos.of(tag.getLong("to")));
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putLong("from", this.from.asLong());
        tag.putLong("to", this.to.asLong());
        return tag;
    }
}
