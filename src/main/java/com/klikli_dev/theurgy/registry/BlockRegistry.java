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
            BLOCKS.registerBlock("calcination_oven", CalcinationOvenBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f));

    public static final DeferredBlock<PyromanticBrazierBlock> PYROMANTIC_BRAZIER =
            BLOCKS.registerBlock("pyromantic_brazier", PyromanticBrazierBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f)
                    .lightLevel((state) -> state.getValue(BlockStateProperties.LIT) ? 14 : 0));

    public static final DeferredBlock<LiquefactionCauldronBlock> LIQUEFACTION_CAULDRON =
            BLOCKS.registerBlock("liquefaction_cauldron", LiquefactionCauldronBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f));

    public static final DeferredBlock<DistillerBlock> DISTILLER =
            BLOCKS.registerBlock("distiller", DistillerBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f));

    public static final DeferredBlock<IncubatorBlock> INCUBATOR =
            BLOCKS.registerBlock("incubator", IncubatorBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .strength(1.0f));

    public static final DeferredBlock<IncubatorMercuryVesselBlock> INCUBATOR_MERCURY_VESSEL =
            BLOCKS.registerBlock("incubator_mercury_vessel", IncubatorMercuryVesselBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .strength(1.0f));

    public static final DeferredBlock<IncubatorSaltVesselBlock> INCUBATOR_SALT_VESSEL =
            BLOCKS.registerBlock("incubator_salt_vessel", IncubatorSaltVesselBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .strength(1.0f));

    public static final DeferredBlock<IncubatorSulfurVesselBlock> INCUBATOR_SULFUR_VESSEL =
            BLOCKS.registerBlock("incubator_sulfur_vessel", IncubatorSulfurVesselBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .strength(1.0f));

    public static final DeferredBlock<SalAmmoniacAccumulatorBlock> SAL_AMMONIAC_ACCUMULATOR =
            BLOCKS.registerBlock("sal_ammoniac_accumulator", SalAmmoniacAccumulatorBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f));

    public static final DeferredBlock<SalAmmoniacTankBlock> SAL_AMMONIAC_TANK =
            BLOCKS.registerBlock("sal_ammoniac_tank", SalAmmoniacTankBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f));

    public static final DeferredBlock<MercuryCatalystBlock> MERCURY_CATALYST =
            BLOCKS.registerBlock("mercury_catalyst", MercuryCatalystBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .strength(1.0f));

    public static final DeferredBlock<CaloricFluxEmitterBlock> CALORIC_FLUX_EMITTER =
            BLOCKS.registerBlock("caloric_flux_emitter", CaloricFluxEmitterBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .strength(1.0f));

    public static final DeferredBlock<SulfuricFluxEmitterBlock> SULFURIC_FLUX_EMITTER =
            BLOCKS.registerBlock("sulfuric_flux_emitter", SulfuricFluxEmitterBlock::new, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .noOcclusion()
                            .sound(SoundType.METAL)
                            .strength(1.0f));

    public static final DeferredBlock<ReformationSourcePedestalBlock> REFORMATION_SOURCE_PEDESTAL =
            BLOCKS.registerBlock("reformation_source_pedestal", ReformationSourcePedestalBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.STONE)
                    .strength(1.0f));

    public static final DeferredBlock<ReformationTargetPedestalBlock> REFORMATION_TARGET_PEDESTAL =
            BLOCKS.registerBlock("reformation_target_pedestal", ReformationTargetPedestalBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.STONE)
                    .strength(1.0f));

    public static final DeferredBlock<ReformationResultPedestalBlock> REFORMATION_RESULT_PEDESTAL =
            BLOCKS.registerBlock("reformation_result_pedestal", ReformationResultPedestalBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.STONE)
                    .strength(1.0f));

    public static final DeferredBlock<FermentationVatBlock> FERMENTATION_VAT =
            BLOCKS.registerBlock("fermentation_vat", FermentationVatBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(1.0f));

    public static final DeferredBlock<DigestionVatBlock> DIGESTION_VAT =
            BLOCKS.registerBlock("digestion_vat", DigestionVatBlock::new, BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .mapColor(MapColor.CLAY)
                    .sound(SoundType.DECORATED_POT)
                    .strength(1.0f));

    public static final DeferredBlock<LogisticsConnectionNodeBlock> LOGISTICS_CONNECTION_NODE =
            BLOCKS.registerBlock("logistics_connector_node", LogisticsConnectionNodeBlock::new, BlockBehaviour.Properties.of()
                            .strength(0.1f)
                            .noOcclusion()
                            .forceSolidOff());

    public static final DeferredBlock<LogisticsItemInserterBlock> LOGISTICS_ITEM_INSERTER =
            BLOCKS.registerBlock("logistics_item_inserter", LogisticsItemInserterBlock::new, BlockBehaviour.Properties.of()
                            .strength(0.1f)
                            .noOcclusion()
                            .forceSolidOff());

    public static final DeferredBlock<LogisticsItemExtractorBlock> LOGISTICS_ITEM_EXTRACTOR =
            BLOCKS.registerBlock("logistics_item_extractor", LogisticsItemExtractorBlock::new, BlockBehaviour.Properties.of()
                            .strength(0.1f)
                            .noOcclusion()
                            .forceSolidOff());

    public static final DeferredBlock<LogisticsFluidInserterBlock> LOGISTICS_FLUID_INSERTER =
            BLOCKS.registerBlock("logistics_fluid_inserter", LogisticsFluidInserterBlock::new, BlockBehaviour.Properties.of()
                            .strength(0.1f)
                            .noOcclusion()
                            .forceSolidOff());

    public static final DeferredBlock<LogisticsFluidExtractorBlock> LOGISTICS_FLUID_EXTRACTOR =
            BLOCKS.registerBlock("logistics_fluid_extractor", LogisticsFluidExtractorBlock::new, BlockBehaviour.Properties.of()
                            .strength(0.1f)
                            .noOcclusion()
                            .forceSolidOff());

    public static final DeferredBlock<Block> SAL_AMMONIAC_ORE =
            BLOCKS.registerBlock("sal_ammoniac_ore", p -> new DropExperienceBlock(
                    UniformInt.of(2, 5), p),
                    BlockBehaviour.Properties.of()
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 3.0F));

    public static final DeferredBlock<Block> DEEPSLATE_SAL_AMMONIAC_ORE =
            BLOCKS.registerBlock("deepslate_sal_ammoniac_ore", (p) -> new DropExperienceBlock(
                    UniformInt.of(2, 5), p),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE)
                            .strength(4.5f, 3.0f)
                            .sound(SoundType.DEEPSLATE));
}
