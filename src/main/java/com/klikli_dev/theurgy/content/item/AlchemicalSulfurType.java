// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item;

import com.klikli_dev.theurgy.Theurgy;

public enum AlchemicalSulfurType {
    MISC(),
    EARTHEN_MATTERS(),
    GEMS(),
    METALS(),
    OTHER_MINERALS(),
    LOGS(),
    CROPS(),
    HERBS(),
    ANIMALS(),
    MOBS(),
    NITER();

    public final String descriptionId;

    AlchemicalSulfurType() {
        this.descriptionId = Theurgy.MODID + "theurgy.sulfur_type." + this.name().toLowerCase();
    }

    public String descriptionId() {
        return this.descriptionId;
    }
}
