// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.distiller.DistillerBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.fermentationvat.FermentationVatBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorMercuryVesselBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSaltVesselBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSulfurVesselBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector.extractor.LogisticsFluidExtractorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector.inserter.LogisticsFluidInserterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.extractor.LogisticsItemExtractorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.inserter.LogisticsItemInserterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier.PyromanticBrazierBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationResultPedestalBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationSourcePedestalBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationTargetPedestalBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.SalAmmoniacAccumulatorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.SalAmmoniacTankBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Theurgy.MODID);

    public static final Supplier<BlockEntityType<CalcinationOvenBlockEntity>> CALCINATION_OVEN =
            BLOCKS.register("calcination_oven", () ->
                    BlockEntityType.Builder.of(CalcinationOvenBlockEntity::new, BlockRegistry.CALCINATION_OVEN.get()).build(null));

    public static final Supplier<BlockEntityType<PyromanticBrazierBlockEntity>> PYROMANTIC_BRAZIER =
            BLOCKS.register("pyromantic_brazier", () ->
                    BlockEntityType.Builder.of(PyromanticBrazierBlockEntity::new, BlockRegistry.PYROMANTIC_BRAZIER.get()).build(null));

    public static final Supplier<BlockEntityType<LiquefactionCauldronBlockEntity>> LIQUEFACTION_CAULDRON =
            BLOCKS.register("liquefaction_cauldron", () ->
                    BlockEntityType.Builder.of(LiquefactionCauldronBlockEntity::new, BlockRegistry.LIQUEFACTION_CAULDRON.get()).build(null));

    public static final Supplier<BlockEntityType<DistillerBlockEntity>> DISTILLER =
            BLOCKS.register("distiller", () ->
                    BlockEntityType.Builder.of(DistillerBlockEntity::new, BlockRegistry.DISTILLER.get()).build(null));

    public static final Supplier<BlockEntityType<IncubatorBlockEntity>> INCUBATOR =
            BLOCKS.register("incubator", () ->
                    BlockEntityType.Builder.of(IncubatorBlockEntity::new, BlockRegistry.INCUBATOR.get()).build(null));

    public static final Supplier<BlockEntityType<IncubatorMercuryVesselBlockEntity>> INCUBATOR_MERCURY_VESSEL =
            BLOCKS.register("incubator_mercury_vessel", () ->
                    BlockEntityType.Builder.of(IncubatorMercuryVesselBlockEntity::new, BlockRegistry.INCUBATOR_MERCURY_VESSEL.get()).build(null));

    public static final Supplier<BlockEntityType<IncubatorSaltVesselBlockEntity>> INCUBATOR_SALT_VESSEL =
            BLOCKS.register("incubator_salt_vessel", () ->
                    BlockEntityType.Builder.of(IncubatorSaltVesselBlockEntity::new, BlockRegistry.INCUBATOR_SALT_VESSEL.get()).build(null));

    public static final Supplier<BlockEntityType<IncubatorSulfurVesselBlockEntity>> INCUBATOR_SULFUR_VESSEL =
            BLOCKS.register("incubator_sulfur_vessel", () ->
                    BlockEntityType.Builder.of(IncubatorSulfurVesselBlockEntity::new, BlockRegistry.INCUBATOR_SULFUR_VESSEL.get()).build(null));

    public static final Supplier<BlockEntityType<SalAmmoniacTankBlockEntity>> SAL_AMMONIAC_TANK =
            BLOCKS.register("sal_ammoniac_tank", () ->
                    BlockEntityType.Builder.of(SalAmmoniacTankBlockEntity::new, BlockRegistry.SAL_AMMONIAC_TANK.get()).build(null));

    public static final Supplier<BlockEntityType<SalAmmoniacAccumulatorBlockEntity>> SAL_AMMONIAC_ACCUMULATOR =
            BLOCKS.register("sal_ammoniac_accumulator", () ->
                    BlockEntityType.Builder.of(SalAmmoniacAccumulatorBlockEntity::new, BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get()).build(null));

    public static final Supplier<BlockEntityType<MercuryCatalystBlockEntity>> MERCURY_CATALYST =
            BLOCKS.register("mercury_catalyst", () ->
                    BlockEntityType.Builder.of(MercuryCatalystBlockEntity::new, BlockRegistry.MERCURY_CATALYST.get()).build(null));

    public static final Supplier<BlockEntityType<CaloricFluxEmitterBlockEntity>> CALORIC_FLUX_EMITTER =
            BLOCKS.register("caloric_flux_emitter", () ->
                    BlockEntityType.Builder.of(CaloricFluxEmitterBlockEntity::new, BlockRegistry.CALORIC_FLUX_EMITTER.get()).build(null));

    public static final Supplier<BlockEntityType<ReformationSourcePedestalBlockEntity>> REFORMATION_SOURCE_PEDESTAL =
            BLOCKS.register("reformation_source_pedestal", () ->
                    BlockEntityType.Builder.of(ReformationSourcePedestalBlockEntity::new, BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get()).build(null));

    public static final Supplier<BlockEntityType<ReformationTargetPedestalBlockEntity>> REFORMATION_TARGET_PEDESTAL =
            BLOCKS.register("reformation_target_pedestal", () ->
                    BlockEntityType.Builder.of(ReformationTargetPedestalBlockEntity::new, BlockRegistry.REFORMATION_TARGET_PEDESTAL.get()).build(null));

    public static final Supplier<BlockEntityType<ReformationResultPedestalBlockEntity>> REFORMATION_RESULT_PEDESTAL =
            BLOCKS.register("reformation_result_pedestal", () ->
                    BlockEntityType.Builder.of(ReformationResultPedestalBlockEntity::new, BlockRegistry.REFORMATION_RESULT_PEDESTAL.get()).build(null));

    public static final Supplier<BlockEntityType<SulfuricFluxEmitterBlockEntity>> SULFURIC_FLUX_EMITTER =
            BLOCKS.register("sulfuric_flux_emitter", () ->
                    BlockEntityType.Builder.of(SulfuricFluxEmitterBlockEntity::new, BlockRegistry.SULFURIC_FLUX_EMITTER.get()).build(null));

    public static final Supplier<BlockEntityType<FermentationVatBlockEntity>> FERMENTATION_VAT =
            BLOCKS.register("fermentation_vat", () ->
                    BlockEntityType.Builder.of(FermentationVatBlockEntity::new, BlockRegistry.FERMENTATION_VAT.get()).build(null));

    public static final Supplier<BlockEntityType<DigestionVatBlockEntity>> DIGESTION_VAT =
            BLOCKS.register("digestion_vat", () ->
                    BlockEntityType.Builder.of(DigestionVatBlockEntity::new, BlockRegistry.DIGESTION_VAT.get()).build(null));

    public static final Supplier<BlockEntityType<LogisticsItemInserterBlockEntity>> LOGISTICS_ITEM_INSERTER =
            BLOCKS.register(BlockRegistry.LOGISTICS_ITEM_INSERTER.getId().getPath(), () ->
                    BlockEntityType.Builder.of(LogisticsItemInserterBlockEntity::new, BlockRegistry.LOGISTICS_ITEM_INSERTER.get()).build(null));

    public static final Supplier<BlockEntityType<LogisticsItemExtractorBlockEntity>> LOGISTICS_ITEM_EXTRACTOR =
            BLOCKS.register(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.getId().getPath(), () ->
                    BlockEntityType.Builder.of(LogisticsItemExtractorBlockEntity::new, BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get()).build(null));

    public static final Supplier<BlockEntityType<LogisticsFluidInserterBlockEntity>> LOGISTICS_FLUID_INSERTER =
            BLOCKS.register(BlockRegistry.LOGISTICS_FLUID_INSERTER.getId().getPath(), () ->
                    BlockEntityType.Builder.of(LogisticsFluidInserterBlockEntity::new, BlockRegistry.LOGISTICS_FLUID_INSERTER.get()).build(null));

    public static final Supplier<BlockEntityType<LogisticsFluidExtractorBlockEntity>> LOGISTICS_FLUID_EXTRACTOR =
            BLOCKS.register(BlockRegistry.LOGISTICS_FLUID_EXTRACTOR.getId().getPath(), () ->
                    BlockEntityType.Builder.of(LogisticsFluidExtractorBlockEntity::new, BlockRegistry.LOGISTICS_FLUID_EXTRACTOR.get()).build(null));

}
