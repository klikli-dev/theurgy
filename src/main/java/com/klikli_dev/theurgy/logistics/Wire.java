package com.klikli_dev.theurgy.logistics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public record Wire(BlockPos from, BlockPos to) {
    public static final Codec<Wire> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("from").forGetter(Wire::from),
            BlockPos.CODEC.fieldOf("to").forGetter(Wire::to)
    ).apply(instance, Wire::new));


    public static Wire load(CompoundTag tag) {
        return new Wire(BlockPos.of(tag.getLong("from")), BlockPos.of(tag.getLong("to")));
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putLong("from", this.from.asLong());
        tag.putLong("to", this.to.asLong());
        return tag;
    }
}
