// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlock;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterBlock;
import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatBlock;
import com.klikli_dev.theurgy.content.apparatus.distiller.DistillerBlock;
import com.klikli_dev.theurgy.content.apparatus.fermentationvat.FermentationVatBlock;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorBlock;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorMercuryVesselBlock;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSaltVesselBlock;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSulfurVesselBlock;
import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlock;
import com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector.extractor.LogisticsFluidExtractorBlock;
import com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector.inserter.LogisticsFluidInserterBlock;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.extractor.LogisticsItemExtractorBlock;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.inserter.LogisticsItemInserterBlock;
import com.klikli_dev.theurgy.content.apparatus.logisticsconnectionnode.LogisticsConnectionNodeBlock;
import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlock;
import com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier.PyromanticBrazierBlock;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationResultPedestalBlock;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationSourcePedestalBlock;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationTargetPedestalBlock;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterBlock;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.SalAmmoniacAccumulatorBlock;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.SalAmmoniacTankBlock;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Theurgy.MODID);

    public static final DeferredBlock<CalcinationOvenBlock> CALCINATION_OVEN =
            BLOCKS.register("calcination_oven", () -> new CalcinationOvenBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f)));

    public static final DeferredBlock<PyromanticBrazierBlock> PYROMANTIC_BRAZIER =
            BLOCKS.register("pyromantic_brazier", () -> new PyromanticBrazierBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f)
                    .lightLevel((state) -> state.getValue(BlockStateProperties.LIT) ? 14 : 0)));

    public static final DeferredBlock<LiquefactionCauldronBlock> LIQUEFACTION_CAULDRON =
            BLOCKS.register("liquefaction_cauldron", () -> new LiquefactionCauldronBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f)));

    public static final DeferredBlock<DistillerBlock> DISTILLER =
            BLOCKS.register("distiller", () -> new DistillerBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f)));

    public static final DeferredBlock<IncubatorBlock> INCUBATOR =
            BLOCKS.register("incubator", () -> new IncubatorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .strength(1.0f)));

    public static final DeferredBlock<IncubatorMercuryVesselBlock> INCUBATOR_MERCURY_VESSEL =
            BLOCKS.register("incubator_mercury_vessel", () -> new IncubatorMercuryVesselBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .strength(1.0f)));

    public static final DeferredBlock<IncubatorSaltVesselBlock> INCUBATOR_SALT_VESSEL =
            BLOCKS.register("incubator_salt_vessel", () -> new IncubatorSaltVesselBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .strength(1.0f)));

    public static final DeferredBlock<IncubatorSulfurVesselBlock> INCUBATOR_SULFUR_VESSEL =
            BLOCKS.register("incubator_sulfur_vessel", () -> new IncubatorSulfurVesselBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .strength(1.0f)));

    public static final DeferredBlock<SalAmmoniacAccumulatorBlock> SAL_AMMONIAC_ACCUMULATOR =
            BLOCKS.register("sal_ammoniac_accumulator", () -> new SalAmmoniacAccumulatorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f)));

    public static final DeferredBlock<SalAmmoniacTankBlock> SAL_AMMONIAC_TANK =
            BLOCKS.register("sal_ammoniac_tank", () -> new SalAmmoniacTankBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f)));

    public static final DeferredBlock<MercuryCatalystBlock> MERCURY_CATALYST =
            BLOCKS.register("mercury_catalyst", () -> new MercuryCatalystBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .strength(1.0f)));

    public static final DeferredBlock<CaloricFluxEmitterBlock> CALORIC_FLUX_EMITTER =
            BLOCKS.register("caloric_flux_emitter", () -> new CaloricFluxEmitterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .strength(1.0f))
            );

    public static final DeferredBlock<SulfuricFluxEmitterBlock> SULFURIC_FLUX_EMITTER =
            BLOCKS.register("sulfuric_flux_emitter", () -> new SulfuricFluxEmitterBlock(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .noOcclusion()
                            .sound(SoundType.METAL)
                            .strength(1.0f)

                    )
            );

    public static final DeferredBlock<ReformationSourcePedestalBlock> REFORMATION_SOURCE_PEDESTAL =
            BLOCKS.register("reformation_source_pedestal", () -> new ReformationSourcePedestalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.STONE)
                    .strength(1.0f))
            );

    public static final DeferredBlock<ReformationTargetPedestalBlock> REFORMATION_TARGET_PEDESTAL =
            BLOCKS.register("reformation_target_pedestal", () -> new ReformationTargetPedestalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.STONE)
                    .strength(1.0f))
            );

    public static final DeferredBlock<ReformationResultPedestalBlock> REFORMATION_RESULT_PEDESTAL =
            BLOCKS.register("reformation_result_pedestal", () -> new ReformationResultPedestalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.STONE)
                    .strength(1.0f))
            );

    public static final DeferredBlock<FermentationVatBlock> FERMENTATION_VAT =
            BLOCKS.register("fermentation_vat", () -> new FermentationVatBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(1.0f))
            );

    public static final DeferredBlock<DigestionVatBlock> DIGESTION_VAT =
            BLOCKS.register("digestion_vat", () -> new DigestionVatBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .mapColor(MapColor.CLAY)
                    .sound(SoundType.DECORATED_POT)
                    .strength(1.0f))
            );

    public static final DeferredBlock<LogisticsConnectionNodeBlock> LOGISTICS_CONNECTION_NODE =
            BLOCKS.register("logistics_connector_node", () -> new LogisticsConnectionNodeBlock(BlockBehaviour.Properties.of()
                            .strength(0.1f)
                            .noOcclusion()
                            .forceSolidOff()
                    )
            );

    public static final DeferredBlock<LogisticsItemInserterBlock> LOGISTICS_ITEM_INSERTER =
            BLOCKS.register("logistics_item_inserter", () -> new LogisticsItemInserterBlock(BlockBehaviour.Properties.of()
                            .strength(0.1f)
                            .noOcclusion()
                            .forceSolidOff()
                    )
            );

    public static final DeferredBlock<LogisticsItemExtractorBlock> LOGISTICS_ITEM_EXTRACTOR =
            BLOCKS.register("logistics_item_extractor", () -> new LogisticsItemExtractorBlock(BlockBehaviour.Properties.of()
                            .strength(0.1f)
                            .noOcclusion()
                            .forceSolidOff()
                    )
            );

    public static final DeferredBlock<LogisticsFluidInserterBlock> LOGISTICS_FLUID_INSERTER =
            BLOCKS.register("logistics_fluid_inserter", () -> new LogisticsFluidInserterBlock(BlockBehaviour.Properties.of()
                            .strength(0.1f)
                            .noOcclusion()
                            .forceSolidOff()
                    )
            );

    public static final DeferredBlock<LogisticsFluidExtractorBlock> LOGISTICS_FLUID_EXTRACTOR =
            BLOCKS.register("logistics_fluid_extractor", () -> new LogisticsFluidExtractorBlock(BlockBehaviour.Properties.of()
                            .strength(0.1f)
                            .noOcclusion()
                            .forceSolidOff()
                    )
            );

    public static final DeferredBlock<Block> SAL_AMMONIAC_ORE =
            BLOCKS.register("sal_ammoniac_ore", () -> new DropExperienceBlock(
                    UniformInt.of(2, 5),
                    BlockBehaviour.Properties.of()
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 3.0F))
            );

    public static final DeferredBlock<Block> DEEPSLATE_SAL_AMMONIAC_ORE =
            BLOCKS.register("deepslate_sal_ammoniac_ore", () -> new DropExperienceBlock(
                    UniformInt.of(2, 5),
                    BlockBehaviour.Properties.ofLegacyCopy(SAL_AMMONIAC_ORE.get())
                            .mapColor(MapColor.DEEPSLATE)
                            .strength(4.5f, 3.0f)
                            .sound(SoundType.DEEPSLATE))
            );
}
