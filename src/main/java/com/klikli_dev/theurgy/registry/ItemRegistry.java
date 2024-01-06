// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlockItem;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterBlockItem;
import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatBlockItem;
import com.klikli_dev.theurgy.content.apparatus.distiller.DistillerBlockItem;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorMercuryVesselBlockItem;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSaltVesselBlockItem;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSulfurVesselBlockItem;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.SalAmmoniacAccumulatorBlockItem;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterBlockItem;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.SalAmmoniacTankBlockItem;
import com.klikli_dev.theurgy.content.item.DivinationRodItem;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Theurgy.MODID);
    //Helper items for rendering
    public static final RegistryObject<Item> EMPTY_JAR =
            ITEMS.register("empty_jar", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EMPTY_JAR_LABELED =
            ITEMS.register("empty_jar_labeled", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> JAR_LABEL =
            ITEMS.register("jar_label", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> THE_HERMETICA_ICON =
            ITEMS.register("the_hermetica_icon", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GEMS_ABUNDANT_ICON = ITEMS.register("gems_abundant_icon", () -> new Item(new Item.Properties()));
    //Divination rods
    public static final RegistryObject<DivinationRodItem> DIVINATION_ROD_T1 =
            ITEMS.register("divination_rod_t1", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).defaultDurability(1),
                    Tiers.STONE, BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 8, true));

    public static final RegistryObject<DivinationRodItem> DIVINATION_ROD_T2 =
            ITEMS.register("divination_rod_t2", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).defaultDurability(1),
                    Tiers.IRON, BlockTagRegistry.DIVINATION_ROD_T2_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T2_DISALLOWED_BLOCKS,
                    96, 40, 16, true));
    public static final RegistryObject<DivinationRodItem> DIVINATION_ROD_T3 =
            ITEMS.register("divination_rod_t3", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).defaultDurability(1),
                    Tiers.DIAMOND, BlockTagRegistry.DIVINATION_ROD_T3_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T3_DISALLOWED_BLOCKS,
                    96, 40, 32, true));
    public static final RegistryObject<DivinationRodItem> DIVINATION_ROD_T4 =

            ITEMS.register("divination_rod_t4", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).defaultDurability(1),
                    Tiers.NETHERITE, BlockTagRegistry.DIVINATION_ROD_T4_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T4_DISALLOWED_BLOCKS,
                    96, 40, 128, true));

    public static final RegistryObject<DivinationRodItem> SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT =
            ITEMS.register("sulfur_attuned_divination_rod_abundant", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).defaultDurability(1),
                    Tiers.STONE, BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 16, false));

    public static final RegistryObject<DivinationRodItem> SULFUR_ATTUNED_DIVINATION_ROD_COMMON =
            ITEMS.register("sulfur_attuned_divination_rod_common", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).defaultDurability(1),
                    Tiers.STONE, BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 16, false));

    public static final RegistryObject<DivinationRodItem> SULFUR_ATTUNED_DIVINATION_ROD_RARE =
            ITEMS.register("sulfur_attuned_divination_rod_rare", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).defaultDurability(1),
                    Tiers.STONE, BlockTagRegistry.DIVINATION_ROD_T2_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T2_DISALLOWED_BLOCKS,
                    96, 40, 32, false));

    public static final RegistryObject<DivinationRodItem> SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS =
            ITEMS.register("sulfur_attuned_divination_rod_precious", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).defaultDurability(1),
                    Tiers.STONE, BlockTagRegistry.DIVINATION_ROD_T3_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T3_DISALLOWED_BLOCKS,
                    96, 40, 32, false));

    public static final RegistryObject<DivinationRodItem> AMETHYST_DIVINATION_ROD =
            ITEMS.register("amethyst_divination_rod", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).defaultDurability(1),
                    Tiers.STONE, BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 16, false));

    //Buckets
    public static final RegistryObject<Item> SAL_AMMONIAC_BUCKET = ITEMS.register("sal_ammoniac_bucket", () -> new BucketItem(FluidRegistry.SAL_AMMONIAC, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> MERCURY_SHARD =
            ITEMS.register("mercury_shard", () -> new Item(new Item.Properties()));

    //Ingredients
    public static final RegistryObject<Item> MERCURY_CRYSTAL =
            ITEMS.register("mercury_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SAL_AMMONIAC_CRYSTAL =
            ITEMS.register("sal_ammoniac_crystal", () -> new Item(new Item.Properties()));
    //Blocks
    public static final RegistryObject<Item> SAL_AMMONIAC_ORE =
            ITEMS.register("sal_ammoniac_ore", () -> new BlockItem(BlockRegistry.SAL_AMMONIAC_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> DEEPSLATE_SAL_AMMONIAC_ORE =
            ITEMS.register("deepslate_sal_ammoniac_ore", () -> new BlockItem(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CALCINATION_OVEN =
            ITEMS.register("calcination_oven", () -> new CalcinationOvenBlockItem(BlockRegistry.CALCINATION_OVEN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> PYROMANTIC_BRAZIER =
            ITEMS.register("pyromantic_brazier", () -> new BlockItem(BlockRegistry.PYROMANTIC_BRAZIER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> LIQUEFACTION_CAULDRON =
            ITEMS.register("liquefaction_cauldron", () -> new BlockItem(BlockRegistry.LIQUEFACTION_CAULDRON.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> DISTILLER =
            ITEMS.register("distiller", () -> new DistillerBlockItem(BlockRegistry.DISTILLER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INCUBATOR =
            ITEMS.register("incubator", () -> new BlockItem(BlockRegistry.INCUBATOR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INCUBATOR_MERCURY_VESSEL =
            ITEMS.register("incubator_mercury_vessel", () -> new IncubatorMercuryVesselBlockItem(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INCUBATOR_SALT_VESSEL =
            ITEMS.register("incubator_salt_vessel", () -> new IncubatorSaltVesselBlockItem(BlockRegistry.INCUBATOR_SALT_VESSEL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INCUBATOR_SULFUR_VESSEL =
            ITEMS.register("incubator_sulfur_vessel", () -> new IncubatorSulfurVesselBlockItem(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SAL_AMMONIAC_ACCUMULATOR =
            ITEMS.register("sal_ammoniac_accumulator", () -> new SalAmmoniacAccumulatorBlockItem(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SAL_AMMONIAC_TANK =
            ITEMS.register("sal_ammoniac_tank", () -> new SalAmmoniacTankBlockItem(BlockRegistry.SAL_AMMONIAC_TANK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MERCURY_CATALYST =
            ITEMS.register("mercury_catalyst", () -> new BlockItem(BlockRegistry.MERCURY_CATALYST.get(), new Item.Properties()));
    public static final RegistryObject<CaloricFluxEmitterBlockItem> CALORIC_FLUX_EMITTER =
            ITEMS.register("caloric_flux_emitter", () -> new CaloricFluxEmitterBlockItem(BlockRegistry.CALORIC_FLUX_EMITTER.get(), new Item.Properties()));
    public static final RegistryObject<SulfuricFluxEmitterBlockItem> SULFURIC_FLUX_EMITTER =
            ITEMS.register("sulfuric_flux_emitter", () -> new SulfuricFluxEmitterBlockItem(BlockRegistry.SULFURIC_FLUX_EMITTER.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> REFORMATION_SOURCE_PEDESTAL =
            ITEMS.register("reformation_source_pedestal", () -> new BlockItem(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> REFORMATION_TARGET_PEDESTAL =
            ITEMS.register("reformation_target_pedestal", () -> new BlockItem(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> REFORMATION_RESULT_PEDESTAL =
            ITEMS.register("reformation_result_pedestal", () -> new BlockItem(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> FERMENTATION_VAT =
            ITEMS.register("fermentation_vat", () -> new BlockItem(BlockRegistry.FERMENTATION_VAT.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> DIGESTION_VAT =
            ITEMS.register("digestion_vat", () -> new DigestionVatBlockItem(BlockRegistry.DIGESTION_VAT.get(), new Item.Properties()));
}
