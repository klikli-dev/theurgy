package com.klikli_dev.theurgy.content.logistics;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Graph;
import net.minecraft.world.level.saveddata.SavedData;

public class LogisticsSavedData extends SavedData {
    public static final String ID = "theurgy:logistics";
    public static final Codec<LogisticsSavedData> CODEC = Codec.unit(LogisticsSavedData::new);
    private static final String NBT_TAG = "theurgy:logistics";

    public static LogisticsSavedData load(CompoundTag pCompoundTag) {
        return CODEC.parse(NbtOps.INSTANCE, pCompoundTag.get(NBT_TAG)).result().orElseThrow();
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.put(NBT_TAG, CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElseThrow());
        return pCompoundTag;
    }
}
