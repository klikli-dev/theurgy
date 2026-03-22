// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import net.minecraft.core.HolderLookup;

public interface NBTSerializable<T> {
    T serializeNBT(HolderLookup.Provider provider);

    void deserializeNBT(HolderLookup.Provider provider, T nbt);
}

