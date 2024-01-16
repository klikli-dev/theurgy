// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.fluids.FluidStack;

public class TheurgyExtraCodecs {
    public static final Codec<FluidStack> SINGLE_FLUID_CODEC = BuiltInRegistries.FLUID.byNameCodec().xmap(fluid -> new FluidStack(fluid, 1), FluidStack::getFluid);
}
