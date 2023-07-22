/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidRegistry {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Theurgy.MODID);

    private static ForgeFlowingFluid.Properties salAmmoniacProperties() {
        return new ForgeFlowingFluid.Properties(FluidTypeRegistry.SAL_AMMONIAC, SAL_AMMONIAC, SAL_AMMONIAC_FLOWING).bucket(ItemRegistry.SAL_AMMONIAC_BUCKET::get);
    }

    public static final RegistryObject<Fluid> SAL_AMMONIAC = FLUIDS.register("sal_ammoniac", () -> new ForgeFlowingFluid.Source(salAmmoniacProperties()));
    public static final RegistryObject<FlowingFluid> SAL_AMMONIAC_FLOWING = FLUIDS.register("sal_ammoniac_flowing", () -> new ForgeFlowingFluid.Flowing(salAmmoniacProperties()));
}
