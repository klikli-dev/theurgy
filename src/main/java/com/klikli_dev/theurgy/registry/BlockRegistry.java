// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatBlock;
import com.klikli_dev.theurgy.content.apparatus.fermentationvat.FermentationVatBlock;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlock;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterBlock;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterSelectionBehaviour;
import com.klikli_dev.theurgy.content.apparatus.distiller.DistillerBlock;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorBlock;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorMercuryVesselBlock;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSaltVesselBlock;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSulfurVesselBlock;
import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlock;
import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlock;
import com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier.PyromanticBrazierBlock;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.*;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.SalAmmoniacAccumulatorBlock;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.SalAmmoniacTankBlock;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Theurgy.MODID);

    public static final RegistryObject<CalcinationOvenBlock> CALCINATION_OVEN =
            BLOCKS.register("calcination_oven", () -> new CalcinationOvenBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f)));

    public static final RegistryObject<PyromanticBrazierBlock> PYROMANTIC_BRAZIER =
            BLOCKS.register("pyromantic_brazier", () -> new PyromanticBrazierBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f)
                    .lightLevel((state) -> state.getValue(BlockStateProperties.LIT) ? 14 : 0)));

    public static final RegistryObject<LiquefactionCauldronBlock> LIQUEFACTION_CAULDRON =
            BLOCKS.register("liquefaction_cauldron", () -> new LiquefactionCauldronBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f)));

    public static final RegistryObject<DistillerBlock> DISTILLER =
            BLOCKS.register("distiller", () -> new DistillerBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f)));

    public static final RegistryObject<IncubatorBlock> INCUBATOR =
            BLOCKS.register("incubator", () -> new IncubatorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .strength(1.0f)));

    public static final RegistryObject<IncubatorMercuryVesselBlock> INCUBATOR_MERCURY_VESSEL =
            BLOCKS.register("incubator_mercury_vessel", () -> new IncubatorMercuryVesselBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .strength(1.0f)));

    public static final RegistryObject<IncubatorSaltVesselBlock> INCUBATOR_SALT_VESSEL =
            BLOCKS.register("incubator_salt_vessel", () -> new IncubatorSaltVesselBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .strength(1.0f)));

    public static final RegistryObject<IncubatorSulfurVesselBlock> INCUBATOR_SULFUR_VESSEL =
            BLOCKS.register("incubator_sulfur_vessel", () -> new IncubatorSulfurVesselBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .strength(1.0f)));

    public static final RegistryObject<SalAmmoniacAccumulatorBlock> SAL_AMMONIAC_ACCUMULATOR =
            BLOCKS.register("sal_ammoniac_accumulator", () -> new SalAmmoniacAccumulatorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f)));

    public static final RegistryObject<SalAmmoniacTankBlock> SAL_AMMONIAC_TANK =
            BLOCKS.register("sal_ammoniac_tank", () -> new SalAmmoniacTankBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.COPPER)
                    .strength(1.0f)));

    public static final RegistryObject<MercuryCatalystBlock> MERCURY_CATALYST =
            BLOCKS.register("mercury_catalyst", () -> new MercuryCatalystBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .strength(1.0f)));

    public static final RegistryObject<CaloricFluxEmitterBlock> CALORIC_FLUX_EMITTER =
            BLOCKS.register("caloric_flux_emitter", () -> new CaloricFluxEmitterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .strength(1.0f),
                    new CaloricFluxEmitterSelectionBehaviour())
            );

    public static final RegistryObject<SulfuricFluxEmitterBlock> SULFURIC_FLUX_EMITTER =
            BLOCKS.register("sulfuric_flux_emitter", () -> new SulfuricFluxEmitterBlock(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .noOcclusion()
                            .sound(SoundType.METAL)
                            .strength(1.0f),
                            new SulfuricFluxEmitterSelectionBehaviour()
                    )
            );

    public static final RegistryObject<ReformationSourcePedestalBlock> REFORMATION_SOURCE_PEDESTAL =
            BLOCKS.register("reformation_source_pedestal", () -> new ReformationSourcePedestalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.STONE)
                    .strength(1.0f))
            );

    public static final RegistryObject<ReformationTargetPedestalBlock> REFORMATION_TARGET_PEDESTAL =
            BLOCKS.register("reformation_target_pedestal", () -> new ReformationTargetPedestalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.STONE)
                    .strength(1.0f))
            );

    public static final RegistryObject<ReformationResultPedestalBlock> REFORMATION_RESULT_PEDESTAL =
            BLOCKS.register("reformation_result_pedestal", () -> new ReformationResultPedestalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .sound(SoundType.STONE)
                    .strength(1.0f))
            );

    public static final RegistryObject<FermentationVatBlock> FERMENTATION_VAT =
            BLOCKS.register("fermentation_vat", () -> new FermentationVatBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(1.0f))
            );

    public static final RegistryObject<DigestionVatBlock> DIGESTION_VAT =
            BLOCKS.register("digestion_vat", () -> new DigestionVatBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .mapColor(MapColor.CLAY)
                    .sound(SoundType.DECORATED_POT)
                    .strength(1.0f))
            );

    public static final RegistryObject<Block> SAL_AMMONIAC_ORE = BLOCKS.register("sal_ammoniac_ore", () ->
            new DropExperienceBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(2, 5)));

    public static final RegistryObject<Block> DEEPSLATE_SAL_AMMONIAC_ORE = BLOCKS.register("deepslate_sal_ammoniac_ore", () ->
            new DropExperienceBlock(BlockBehaviour.Properties.copy(SAL_AMMONIAC_ORE.get()).mapColor(MapColor.DEEPSLATE).strength(4.5f, 3.0f).sound(SoundType.DEEPSLATE), UniformInt.of(2, 5)));


}
