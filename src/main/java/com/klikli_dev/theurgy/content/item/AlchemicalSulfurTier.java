package com.klikli_dev.theurgy.content.item;

import com.klikli_dev.theurgy.Theurgy;

public enum AlchemicalSulfurTier {
    ABUNDANT(0xcfcfcf),
    COMMON(0x41b335),
    RARE(0x413fa3),
    PRECIOUS(0x6e2f74);

    public final int color;
    public final String descriptionId;

    AlchemicalSulfurTier(int color) {
        this.color = color;
        this.descriptionId = Theurgy.MODID + "theurgy.sulfur_tier." + this.name().toLowerCase();
    }

    public int color() {
        return this.color;
    }

    public String descriptionId() {
        return this.descriptionId;
    }
}
