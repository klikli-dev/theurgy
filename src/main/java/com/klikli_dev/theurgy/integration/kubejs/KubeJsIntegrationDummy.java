// SPDX-FileCopyrightText: 2024 klikli_dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.kubejs;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class KubeJsIntegrationDummy implements KubeJsIntegration {
    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public boolean isEmpty(TagKey<Fluid> tag) {
        return true;
    }
}
