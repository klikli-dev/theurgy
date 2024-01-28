package com.klikli_dev.theurgy.logistics;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.saveddata.SavedData;

public class WiresSavedData extends SavedData {
    public static final String ID = "theurgy:wires";
    public static final Codec<WiresSavedData> CODEC = Codec.unit(WiresSavedData::new);
    private static final String NBT_TAG = "theurgy:wires";




    public static WiresSavedData load(CompoundTag pCompoundTag) {
        return CODEC.parse(NbtOps.INSTANCE, pCompoundTag.get(NBT_TAG)).result().orElseThrow();
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.put(NBT_TAG, CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElseThrow());
        return pCompoundTag;
    }
}
