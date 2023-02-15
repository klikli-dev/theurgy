/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.fluid.SolventFluidType;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidTypeRegistry {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Theurgy.MODID);

    public static final RegistryObject<FluidType> SAL_AMMONIAC = FLUID_TYPES.register("sal_ammoniac", () -> new SolventFluidType(FluidType.Properties.create(), Theurgy.loc("block/sal_ammoniac/still"), Theurgy.loc("block/sal_ammoniac/flow"), Theurgy.loc("block/sal_ammoniac/overlay"), 0xFFFF00FF));
}
