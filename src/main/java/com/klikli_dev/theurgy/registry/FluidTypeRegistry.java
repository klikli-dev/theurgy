// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.fluid.SolventFluidType;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class FluidTypeRegistry {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Theurgy.MODID);

    public static final DeferredHolder<FluidType, SolventFluidType> SAL_AMMONIAC = FLUID_TYPES.register("sal_ammoniac",
            () -> new SolventFluidType(
                    FluidType.Properties.create(),
                    Theurgy.loc("block/sal_ammoniac/still"),
                    Theurgy.loc("block/sal_ammoniac/flow"),
                    Theurgy.loc("block/sal_ammoniac/overlay"),
                    0xFFFF00FF)
    );
}
