// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FluidRegistry {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Theurgy.MODID);

    private static BaseFlowingFluid.Properties salAmmoniacProperties() {
        return new BaseFlowingFluid.Properties(FluidTypeRegistry.SAL_AMMONIAC, SAL_AMMONIAC, SAL_AMMONIAC_FLOWING).bucket(ItemRegistry.SAL_AMMONIAC_BUCKET);
    }

    public static final DeferredHolder<Fluid, Fluid> SAL_AMMONIAC = FLUIDS.register("sal_ammoniac", () -> new BaseFlowingFluid.Source(salAmmoniacProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SAL_AMMONIAC_FLOWING = FLUIDS.register("sal_ammoniac_flowing", () -> new BaseFlowingFluid.Flowing(salAmmoniacProperties()));
}
