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
import com.klikli_dev.theurgy.content.apparatus.mercuryfluxemitter.MercuryFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticscapabilityproxy.LogisticsCapabilityProxyBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector.extractor.LogisticsFluidExtractorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector.inserter.LogisticsFluidInserterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.extractor.LogisticsItemExtractorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.inserter.LogisticsItemInserterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsmercuryfluxconnector.LogisticsMercuryFluxConnectorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.MercuryCapacitorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier.PyromanticBrazierBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.*;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.SalAmmoniacAccumulatorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.SalAmmoniacTankBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsnexus.LogisticsNexusBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Theurgy.MODID);

    public static final Supplier<BlockEntityType<CalcinationOvenBlockEntity>> CALCINATION_OVEN =
            BLOCKS.register("calcination_oven", () ->
                    new BlockEntityType<>(CalcinationOvenBlockEntity::new, Set.of(BlockRegistry.CALCINATION_OVEN.get())));

    public static final Supplier<BlockEntityType<PyromanticBrazierBlockEntity>> PYROMANTIC_BRAZIER =
            BLOCKS.register("pyromantic_brazier", () ->
                    new BlockEntityType<>(PyromanticBrazierBlockEntity::new, Set.of(BlockRegistry.PYROMANTIC_BRAZIER.get())));

    public static final Supplier<BlockEntityType<LiquefactionCauldronBlockEntity>> LIQUEFACTION_CAULDRON =
            BLOCKS.register("liquefaction_cauldron", () ->
                    new BlockEntityType<>(LiquefactionCauldronBlockEntity::new, Set.of(BlockRegistry.LIQUEFACTION_CAULDRON.get())));

    public static final Supplier<BlockEntityType<DistillerBlockEntity>> DISTILLER =
            BLOCKS.register("distiller", () ->
                    new BlockEntityType<>(DistillerBlockEntity::new, Set.of(BlockRegistry.DISTILLER.get())));

    public static final Supplier<BlockEntityType<IncubatorBlockEntity>> INCUBATOR =
            BLOCKS.register("incubator", () ->
                    new BlockEntityType<>(IncubatorBlockEntity::new, Set.of(BlockRegistry.INCUBATOR.get())));

    public static final Supplier<BlockEntityType<IncubatorMercuryVesselBlockEntity>> INCUBATOR_MERCURY_VESSEL =
            BLOCKS.register("incubator_mercury_vessel", () ->
                    new BlockEntityType<>(IncubatorMercuryVesselBlockEntity::new, Set.of(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get())));

    public static final Supplier<BlockEntityType<IncubatorSaltVesselBlockEntity>> INCUBATOR_SALT_VESSEL =
            BLOCKS.register("incubator_salt_vessel", () ->
                    new BlockEntityType<>(IncubatorSaltVesselBlockEntity::new, Set.of(BlockRegistry.INCUBATOR_SALT_VESSEL.get())));

    public static final Supplier<BlockEntityType<IncubatorSulfurVesselBlockEntity>> INCUBATOR_SULFUR_VESSEL =
            BLOCKS.register("incubator_sulfur_vessel", () ->
                    new BlockEntityType<>(IncubatorSulfurVesselBlockEntity::new, Set.of(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get())));

    public static final Supplier<BlockEntityType<SalAmmoniacTankBlockEntity>> SAL_AMMONIAC_TANK =
            BLOCKS.register("sal_ammoniac_tank", () ->
                    new BlockEntityType<>(SalAmmoniacTankBlockEntity::new, Set.of(BlockRegistry.SAL_AMMONIAC_TANK.get())));

    public static final Supplier<BlockEntityType<SalAmmoniacAccumulatorBlockEntity>> SAL_AMMONIAC_ACCUMULATOR =
            BLOCKS.register("sal_ammoniac_accumulator", () ->
                    new BlockEntityType<>(SalAmmoniacAccumulatorBlockEntity::new, Set.of(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get())));

    public static final Supplier<BlockEntityType<LogisticsNexusBlockEntity>> LOGISTICS_NEXUS =
            BLOCKS.register("logistics_nexus", () ->
                    new BlockEntityType<>(LogisticsNexusBlockEntity::new, Set.of(BlockRegistry.LOGISTICS_NEXUS.get())));

    public static final Supplier<BlockEntityType<MercuryCatalystBlockEntity>> MERCURY_CATALYST =
            BLOCKS.register("mercury_catalyst", () ->
                    new BlockEntityType<>(MercuryCatalystBlockEntity::new, Set.of(BlockRegistry.MERCURY_CATALYST.get())));

    public static final Supplier<BlockEntityType<MercuryCapacitorBlockEntity>> MERCURY_CAPACITOR =
            BLOCKS.register("mercury_capacitor", () ->
                    new BlockEntityType<>(MercuryCapacitorBlockEntity::new, Set.of(BlockRegistry.MERCURY_CAPACITOR.get())));

    public static final Supplier<BlockEntityType<CaloricFluxEmitterBlockEntity>> CALORIC_FLUX_EMITTER =
            BLOCKS.register("caloric_flux_emitter", () ->
                    new BlockEntityType<>(CaloricFluxEmitterBlockEntity::new, Set.of(BlockRegistry.CALORIC_FLUX_EMITTER.get())));

    public static final Supplier<BlockEntityType<ReformationSourcePedestalBlockEntity>> REFORMATION_SOURCE_PEDESTAL =
            BLOCKS.register("reformation_source_pedestal", () ->
                    new BlockEntityType<>(ReformationSourcePedestalBlockEntity::new, Set.of(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get())));

    public static final Supplier<BlockEntityType<ReformationTargetPedestalBlockEntity>> REFORMATION_TARGET_PEDESTAL =
            BLOCKS.register("reformation_target_pedestal", () ->
                    new BlockEntityType<>(ReformationTargetPedestalBlockEntity::new, Set.of(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get())));

    public static final Supplier<BlockEntityType<ReformationResultPedestalBlockEntity>> REFORMATION_RESULT_PEDESTAL =
            BLOCKS.register("reformation_result_pedestal", () ->
                    new BlockEntityType<>(ReformationResultPedestalBlockEntity::new, Set.of(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get())));

    public static final Supplier<BlockEntityType<SulfuricFluxEmitterBlockEntity>> SULFURIC_FLUX_EMITTER =
            BLOCKS.register("sulfuric_flux_emitter", () ->
                    new BlockEntityType<>(SulfuricFluxEmitterBlockEntity::new, Set.of(BlockRegistry.SULFURIC_FLUX_EMITTER.get())));

    public static final Supplier<BlockEntityType<MercuryFluxEmitterBlockEntity>> MERCURY_FLUX_EMITTER =
            BLOCKS.register("mercury_flux_emitter", () ->
                    new BlockEntityType<>(MercuryFluxEmitterBlockEntity::new, Set.of(BlockRegistry.MERCURY_FLUX_EMITTER.get())));

    public static final Supplier<BlockEntityType<FermentationVatBlockEntity>> FERMENTATION_VAT =
            BLOCKS.register("fermentation_vat", () ->
                    new BlockEntityType<>(FermentationVatBlockEntity::new, Set.of(BlockRegistry.FERMENTATION_VAT.get())));

    public static final Supplier<BlockEntityType<DigestionVatBlockEntity>> DIGESTION_VAT =
            BLOCKS.register("digestion_vat", () ->
                    new BlockEntityType<>(DigestionVatBlockEntity::new, Set.of(BlockRegistry.DIGESTION_VAT.get())));

    public static final Supplier<BlockEntityType<LogisticsItemInserterBlockEntity>> LOGISTICS_ITEM_INSERTER =
            BLOCKS.register(BlockRegistry.LOGISTICS_ITEM_INSERTER.getId().getPath(), () ->
                    new BlockEntityType<>(LogisticsItemInserterBlockEntity::new, Set.of(BlockRegistry.LOGISTICS_ITEM_INSERTER.get())));

    public static final Supplier<BlockEntityType<LogisticsItemExtractorBlockEntity>> LOGISTICS_ITEM_EXTRACTOR =
            BLOCKS.register(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.getId().getPath(), () ->
                    new BlockEntityType<>(LogisticsItemExtractorBlockEntity::new, Set.of(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get())));

    public static final Supplier<BlockEntityType<LogisticsFluidInserterBlockEntity>> LOGISTICS_FLUID_INSERTER =
            BLOCKS.register(BlockRegistry.LOGISTICS_FLUID_INSERTER.getId().getPath(), () ->
                    new BlockEntityType<>(LogisticsFluidInserterBlockEntity::new, Set.of(BlockRegistry.LOGISTICS_FLUID_INSERTER.get())));

    public static final Supplier<BlockEntityType<LogisticsFluidExtractorBlockEntity>> LOGISTICS_FLUID_EXTRACTOR =
            BLOCKS.register(BlockRegistry.LOGISTICS_FLUID_EXTRACTOR.getId().getPath(), () ->
                    new BlockEntityType<>(LogisticsFluidExtractorBlockEntity::new, Set.of(BlockRegistry.LOGISTICS_FLUID_EXTRACTOR.get())));

    public static final Supplier<BlockEntityType<LogisticsCapabilityProxyBlockEntity>> LOGISTICS_CAPABILITY_PROXY =
            BLOCKS.register(BlockRegistry.LOGISTICS_CAPABILITY_PROXY.getId().getPath(), () ->
                    new BlockEntityType<>(LogisticsCapabilityProxyBlockEntity::new, Set.of(BlockRegistry.LOGISTICS_CAPABILITY_PROXY.get())));

    public static final Supplier<BlockEntityType<LogisticsMercuryFluxConnectorBlockEntity>> LOGISTICS_MERCURY_FLUX_CONNECTOR =
            BLOCKS.register("logistics_mercury_flux_connector", () ->
                    new BlockEntityType<>(LogisticsMercuryFluxConnectorBlockEntity::new, Set.of(BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get())));

}
