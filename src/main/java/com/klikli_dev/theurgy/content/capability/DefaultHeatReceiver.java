// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.capability;

import com.klikli_dev.theurgy.util.NBTSerializable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class DefaultHeatReceiver implements HeatReceiver, NBTSerializable<Tag>, ValueIOSerializable {
    protected long isHotUntil;

    @Override
    public Tag serializeNBT(HolderLookup.Provider provider) {
        return LongTag.valueOf(this.isHotUntil);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag nbt) {
        if (!(nbt instanceof LongTag longTag))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.isHotUntil = longTag.value();
    }

    @Override
    public void serialize(ValueOutput output) {
        output.putLong("value", this.isHotUntil);
    }

    @Override
    public void deserialize(ValueInput input) {
        this.isHotUntil = input.getLongOr("value", 0L);
    }

    @Override
    public void setHotUntil(long gameTime) {
        this.isHotUntil = gameTime;
    }

    @Override
    public long getIsHotUntil() {
        return this.isHotUntil;
    }

    @Override
    public boolean readyToReceive() {
        return true;
    }
}
