// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.derivative;

import com.klikli_dev.theurgy.Theurgy;

public enum AlchemicalDerivativeTier {
    ABUNDANT(0xcfcfcf),
    COMMON(0x41b335),
    RARE(0x413fa3),
    PRECIOUS(0xd20dd4);

    public final int color;
    public final String descriptionId;

    AlchemicalDerivativeTier(int color) {
        this.color = color;
        this.descriptionId = Theurgy.MODID + "theurgy.alchemical_tier." + this.name().toLowerCase();
    }

    public int color() {
        return this.color;
    }

    public String descriptionId() {
        return this.descriptionId;
    }
}
