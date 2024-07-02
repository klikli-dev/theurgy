/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.kubejs;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public interface KubeJsIntegration {

    KubeJsIntegration instance = new KubeJsIntegrationDummy();

    static KubeJsIntegration get() {
        return instance;
    }

    boolean isLoaded();

    boolean isEmpty(TagKey<Fluid> tag);
}
