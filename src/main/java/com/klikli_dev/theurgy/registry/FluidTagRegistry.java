/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class FluidTagRegistry {
    public static final TagKey<Fluid> SOLVENT = tag( "solvent");

    public static TagKey<Fluid> tag(String id) {
        return tag(Theurgy.loc(id));
    }

    public static TagKey<Fluid> tag(ResourceLocation id) {
        return TagKey.create(Registries.FLUID, id);
    }
}
