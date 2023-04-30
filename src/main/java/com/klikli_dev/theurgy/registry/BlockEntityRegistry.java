/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.block.calcinationoven.CalcinationOvenBlockEntity;
import com.klikli_dev.theurgy.content.block.distiller.DistillerBlockEntity;
import com.klikli_dev.theurgy.content.block.incubator.IncubatorBlockEntity;
import com.klikli_dev.theurgy.content.block.incubator.IncubatorMercuryVesselBlockEntity;
import com.klikli_dev.theurgy.content.block.incubator.IncubatorSaltVesselBlockEntity;
import com.klikli_dev.theurgy.content.block.incubator.IncubatorSulfurVesselBlockEntity;
import com.klikli_dev.theurgy.content.block.liquefactioncauldron.LiquefactionCauldronBlockEntity;
import com.klikli_dev.theurgy.content.block.pyromanticbrazier.PyromanticBrazierBlockEntity;
import com.klikli_dev.theurgy.content.block.salammoniacaccumulator.SalAmmoniacAccumulatorBlockEntity;
import com.klikli_dev.theurgy.content.block.salammoniactank.SalAmmoniacTankBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Theurgy.MODID);

    public static final RegistryObject<BlockEntityType<CalcinationOvenBlockEntity>> CALCINATION_OVEN =
            BLOCKS.register("calcination_oven", () ->
                    BlockEntityType.Builder.of(CalcinationOvenBlockEntity::new, BlockRegistry.CALCINATION_OVEN.get()).build(null));

    public static final RegistryObject<BlockEntityType<PyromanticBrazierBlockEntity>> PYROMANTIC_BRAZIER =
            BLOCKS.register("pyromantic_brazier", () ->
                    BlockEntityType.Builder.of(PyromanticBrazierBlockEntity::new, BlockRegistry.PYROMANTIC_BRAZIER.get()).build(null));

    public static final RegistryObject<BlockEntityType<LiquefactionCauldronBlockEntity>> LIQUEFACTION_CAULDRON =
            BLOCKS.register("liquefaction_cauldron", () ->
                    BlockEntityType.Builder.of(LiquefactionCauldronBlockEntity::new, BlockRegistry.LIQUEFACTION_CAULDRON.get()).build(null));

    public static final RegistryObject<BlockEntityType<DistillerBlockEntity>> DISTILLER =
            BLOCKS.register("distiller", () ->
                    BlockEntityType.Builder.of(DistillerBlockEntity::new, BlockRegistry.DISTILLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<IncubatorBlockEntity>> INCUBATOR =
            BLOCKS.register("incubator", () ->
                    BlockEntityType.Builder.of(IncubatorBlockEntity::new, BlockRegistry.INCUBATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<IncubatorMercuryVesselBlockEntity>> INCUBATOR_MERCURY_VESSEL =
            BLOCKS.register("incubator_mercury_vessel", () ->
                    BlockEntityType.Builder.of(IncubatorMercuryVesselBlockEntity::new, BlockRegistry.INCUBATOR_MERCURY_VESSEL.get()).build(null));

    public static final RegistryObject<BlockEntityType<IncubatorSaltVesselBlockEntity>> INCUBATOR_SALT_VESSEL =
            BLOCKS.register("incubator_salt_vessel", () ->
                    BlockEntityType.Builder.of(IncubatorSaltVesselBlockEntity::new, BlockRegistry.INCUBATOR_SALT_VESSEL.get()).build(null));

    public static final RegistryObject<BlockEntityType<IncubatorSulfurVesselBlockEntity>> INCUBATOR_SULFUR_VESSEL =
            BLOCKS.register("incubator_sulfur_vessel", () ->
                    BlockEntityType.Builder.of(IncubatorSulfurVesselBlockEntity::new, BlockRegistry.INCUBATOR_SULFUR_VESSEL.get()).build(null));

    public static final RegistryObject<BlockEntityType<SalAmmoniacTankBlockEntity>> SAL_AMMONIAC_TANK =
            BLOCKS.register("sal_ammoniac_tank", () ->
                    BlockEntityType.Builder.of(SalAmmoniacTankBlockEntity::new, BlockRegistry.SAL_AMMONIAC_TANK.get()).build(null));

    public static final RegistryObject<BlockEntityType<SalAmmoniacAccumulatorBlockEntity>> SAL_AMMONIAC_ACCUMULATOR =
            BLOCKS.register("sal_ammoniac_accumulator", () ->
                    BlockEntityType.Builder.of(SalAmmoniacAccumulatorBlockEntity::new, BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get()).build(null));
}
