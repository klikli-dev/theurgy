// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item;

import com.klikli_dev.theurgy.Theurgy;

public enum AlchemicalSulfurType {
    MISC(),
    GEMS(),
    METALS(),
    OTHER_MINERALS(),
    NITER();

    public final String descriptionId;

    AlchemicalSulfurType() {
        this.descriptionId = Theurgy.MODID + "theurgy.sulfur_type." + this.name().toLowerCase();
    }

    public String descriptionId() {
        return this.descriptionId;
    }
}
