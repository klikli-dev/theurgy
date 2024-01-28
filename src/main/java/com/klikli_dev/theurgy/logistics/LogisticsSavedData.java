package com.klikli_dev.theurgy.logistics;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.saveddata.SavedData;

@SuppressWarnings("UnstableApiUsage")
public class LogisticsSavedData extends SavedData {
    public static final String ID = "theurgy:logistics";
    public static final Codec<LogisticsSavedData> CODEC = Codec.unit(LogisticsSavedData::new);
    private static final String NBT_TAG = "theurgy:logistics";

    private final MutableGraph<BlockPos> graph;

    public LogisticsSavedData() {
        this.graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    }

    public static LogisticsSavedData load(CompoundTag pCompoundTag) {
        return CODEC.parse(NbtOps.INSTANCE, pCompoundTag.get(NBT_TAG)).result().orElseThrow();
    }

    public MutableGraph<BlockPos> graph() {
        return this.graph;
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.put(NBT_TAG, CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElseThrow());
        return pCompoundTag;
    }
}
