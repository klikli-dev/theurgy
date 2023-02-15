package com.klikli_dev.theurgy.capability;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class DefaultHeatProvider implements HeatProvider, INBTSerializable<Tag> {
    protected boolean isHot;

    @Override
    public boolean isHot() {
        return this.isHot;
    }

    public void setHot(boolean isHot) {
        this.isHot = isHot;
    }

    @Override
    public Tag serializeNBT() {
        return ByteTag.valueOf(this.isHot ? (byte) 1 : 0);
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (!(nbt instanceof ByteTag byteNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.isHot = byteNbt.getAsByte() != 0;
    }
}
