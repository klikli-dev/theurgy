// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.capability;

import com.klikli_dev.theurgy.util.NBTSerializable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class DefaultHeatProvider implements HeatProvider, NBTSerializable<Tag>, ValueIOSerializable {
    protected boolean isHot;

    @Override
    public boolean isHot() {
        return this.isHot;
    }

    public void setHot(boolean isHot) {
        this.isHot = isHot;
    }

    @Override
    public Tag serializeNBT(HolderLookup.Provider pRegistries) {
        return ByteTag.valueOf(this.isHot ? (byte) 1 : 0);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider pRegistries, Tag nbt) {
        if (!(nbt instanceof ByteTag(byte value)))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.isHot = value != 0;
    }

    @Override
    public void serialize(ValueOutput output) {
        output.putBoolean("value", this.isHot);
    }

    @Override
    public void deserialize(ValueInput input) {
        this.isHot = input.getBooleanOr("value", false);
    }
}
