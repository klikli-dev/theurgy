package com.klikli_dev.theurgy.logistics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public record WireConnection(BlockPos from, BlockPos to) {
    public static final Codec<WireConnection> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("from").forGetter(WireConnection::from),
            BlockPos.CODEC.fieldOf("to").forGetter(WireConnection::to)
    ).apply(instance, WireConnection::new));


    public static WireConnection load(CompoundTag tag) {
        return new WireConnection(BlockPos.of(tag.getLong("from")), BlockPos.of(tag.getLong("to")));
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putLong("from", this.from.asLong());
        tag.putLong("to", this.to.asLong());
        return tag;
    }
}
