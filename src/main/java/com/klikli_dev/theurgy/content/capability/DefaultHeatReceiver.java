// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.capability;

import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class DefaultHeatReceiver implements HeatReceiver, INBTSerializable<Tag> {
    protected long isHotUntil;

    @Override
    public Tag serializeNBT() {
        return LongTag.valueOf(this.isHotUntil);
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (!(nbt instanceof LongTag longTag))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.isHotUntil = longTag.getAsLong();
    }

    @Override
    public void setHotUntil(long gameTime) {
        this.isHotUntil = gameTime;
    }

    @Override
    public long getIsHotUntil() {
        return this.isHotUntil;
    }
}
