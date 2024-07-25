// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlockItem;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterBlockItem;
import com.klikli_dev.theurgy.content.apparatus.distiller.DistillerBlockItem;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorMercuryVesselBlockItem;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSaltVesselBlockItem;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSulfurVesselBlockItem;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlockItem;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterBlockItem;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.SalAmmoniacAccumulatorBlockItem;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.SalAmmoniacTankBlockItem;
import com.klikli_dev.theurgy.content.item.divinationrod.DivinationRodItem;
import com.klikli_dev.theurgy.content.item.filter.AttributeFilterItem;
import com.klikli_dev.theurgy.content.item.filter.FilterItem;
import com.klikli_dev.theurgy.content.item.filter.ListFilterItem;
import com.klikli_dev.theurgy.content.item.mercurialwand.MercurialWandItem;
import com.klikli_dev.theurgy.content.item.mercurialwand.mode.MercurialWandItemMode;
import com.klikli_dev.theurgy.content.item.wire.WireItem;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Theurgy.MODID);

    //Helper items for rendering
    public static final DeferredItem<Item> EMPTY_JAR_ICON =
            ITEMS.register("empty_jar_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EMPTY_JAR_IRON_BAND_ICON =
            ITEMS.register("empty_jar_iron_band_icon", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EMPTY_JAR_LABELED_ICON =
            ITEMS.register("empty_jar_labeled_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EMPTY_CERAMIC_JAR_ICON =
            ITEMS.register("empty_ceramic_jar_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EMPTY_CERAMIC_JAR_LABELED_ICON =
            ITEMS.register("empty_ceramic_jar_labeled_icon", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> JAR_LABEL_ICON =
            ITEMS.register("jar_label_icon", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> JAR_LABEL_FRAME_ABUNDANT_ICON =
            ITEMS.register("jar_label_frame_abundant_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> JAR_LABEL_FRAME_COMMON_ICON =
            ITEMS.register("jar_label_frame_common_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> JAR_LABEL_FRAME_RARE_ICON =
            ITEMS.register("jar_label_frame_rare_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> JAR_LABEL_FRAME_PRECIOUS_ICON =
            ITEMS.register("jar_label_frame_precious_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> THE_HERMETICA_ICON =
            ITEMS.register("the_hermetica_icon", () -> new Item(new Item.Properties()));


    public static final DeferredItem<Item> GEMS_ABUNDANT_ICON = ITEMS.register("gems_abundant_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GEMS_COMMON_ICON = ITEMS.register("gems_common_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GEMS_RARE_ICON = ITEMS.register("gems_rare_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GEMS_PRECIOUS_ICON = ITEMS.register("gems_precious_icon", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> METALS_ABUNDANT_ICON = ITEMS.register("metals_abundant_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> METALS_COMMON_ICON = ITEMS.register("metals_common_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> METALS_RARE_ICON = ITEMS.register("metals_rare_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> METALS_PRECIOUS_ICON = ITEMS.register("metals_precious_icon", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> OTHER_MINERALS_ABUNDANT_ICON = ITEMS.register("other_minerals_abundant_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> OTHER_MINERALS_COMMON_ICON = ITEMS.register("other_minerals_common_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> OTHER_MINERALS_RARE_ICON = ITEMS.register("other_minerals_rare_icon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> OTHER_MINERALS_PRECIOUS_ICON = ITEMS.register("other_minerals_precious_icon", () -> new Item(new Item.Properties()));
    //Divination rods
    public static final DeferredItem<DivinationRodItem> DIVINATION_ROD_T1 =
            ITEMS.register("divination_rod_t1", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).durability(1),
                    Tiers.STONE, BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 8, true));

    public static final DeferredItem<DivinationRodItem> DIVINATION_ROD_T2 =
            ITEMS.register("divination_rod_t2", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).durability(1),
                    Tiers.IRON, BlockTagRegistry.DIVINATION_ROD_T2_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T2_DISALLOWED_BLOCKS,
                    96, 40, 16, true));
    public static final DeferredItem<DivinationRodItem> DIVINATION_ROD_T3 =
            ITEMS.register("divination_rod_t3", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).durability(1),
                    Tiers.DIAMOND, BlockTagRegistry.DIVINATION_ROD_T3_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T3_DISALLOWED_BLOCKS,
                    96, 40, 32, true));
    public static final DeferredItem<DivinationRodItem> DIVINATION_ROD_T4 =

            ITEMS.register("divination_rod_t4", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).durability(1),
                    Tiers.NETHERITE, BlockTagRegistry.DIVINATION_ROD_T4_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T4_DISALLOWED_BLOCKS,
                    96, 40, 128, true));

    public static final DeferredItem<DivinationRodItem> SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT =
            ITEMS.register("sulfur_attuned_divination_rod_abundant", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).durability(1),
                    Tiers.STONE, BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 16, false));

    public static final DeferredItem<DivinationRodItem> SULFUR_ATTUNED_DIVINATION_ROD_COMMON =
            ITEMS.register("sulfur_attuned_divination_rod_common", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).durability(1),
                    Tiers.STONE, BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 16, false));

    public static final DeferredItem<DivinationRodItem> SULFUR_ATTUNED_DIVINATION_ROD_RARE =
            ITEMS.register("sulfur_attuned_divination_rod_rare", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).durability(1),
                    Tiers.STONE, BlockTagRegistry.DIVINATION_ROD_T2_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T2_DISALLOWED_BLOCKS,
                    96, 40, 32, false));

    public static final DeferredItem<DivinationRodItem> SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS =
            ITEMS.register("sulfur_attuned_divination_rod_precious", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).durability(1),
                    Tiers.STONE, BlockTagRegistry.DIVINATION_ROD_T3_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T3_DISALLOWED_BLOCKS,
                    96, 40, 32, false));

    public static final DeferredItem<DivinationRodItem> AMETHYST_DIVINATION_ROD =
            ITEMS.register("amethyst_divination_rod", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).durability(1),
                    Tiers.STONE, BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 16, false));

    //Other Tools
    public static final DeferredItem<Item> COPPER_WIRE =
            ITEMS.register("copper_wire", () -> new WireItem(new Item.Properties(), 32));

    public static final DeferredItem<MercurialWandItem> MERCURIAL_WAND =
            ITEMS.register("mercurial_wand", () -> new MercurialWandItem(new Item.Properties()
                    .component(DataComponentRegistry.MERCURIAL_WAND_ITEM_MODE.get(), MercurialWandItemMode.Type.CYCLE_DIRECTION.mode())
                    .component(DataComponentRegistry.SELECTED_FREQUENCY.get(), 0)
            ));

    public static final DeferredItem<FilterItem> LIST_FILTER = ITEMS.register("list_filter", () -> new ListFilterItem(new Item.Properties()));
    public static final DeferredItem<FilterItem> ATTRIBUTE_FILTER = ITEMS.register("attribute_filter", () -> new AttributeFilterItem(new Item.Properties()));

    //Buckets
    public static final DeferredItem<Item> SAL_AMMONIAC_BUCKET =
            ITEMS.register("sal_ammoniac_bucket", () -> new BucketItem(
                    FluidRegistry.SAL_AMMONIAC.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1))
            );

    //Ingredients
    public static final DeferredItem<Item> MERCURY_SHARD =
            ITEMS.register("mercury_shard", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MERCURY_CRYSTAL =
            ITEMS.register("mercury_crystal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SAL_AMMONIAC_CRYSTAL =
            ITEMS.register("sal_ammoniac_crystal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PURIFIED_GOLD =
            ITEMS.register("purified_gold", () -> new Item(new Item.Properties()));

    //Blocks
    public static final DeferredItem<Item> SAL_AMMONIAC_ORE =
            ITEMS.register("sal_ammoniac_ore", () -> new BlockItem(BlockRegistry.SAL_AMMONIAC_ORE.get(), new Item.Properties()));
    public static final DeferredItem<Item> DEEPSLATE_SAL_AMMONIAC_ORE =
            ITEMS.register("deepslate_sal_ammoniac_ore", () -> new BlockItem(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> CALCINATION_OVEN =
            ITEMS.register("calcination_oven", () -> new CalcinationOvenBlockItem(BlockRegistry.CALCINATION_OVEN.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> PYROMANTIC_BRAZIER =
            ITEMS.register("pyromantic_brazier", () -> new BlockItem(BlockRegistry.PYROMANTIC_BRAZIER.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> LIQUEFACTION_CAULDRON =
            ITEMS.register("liquefaction_cauldron", () -> new BlockItem(BlockRegistry.LIQUEFACTION_CAULDRON.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> DISTILLER =
            ITEMS.register("distiller", () -> new DistillerBlockItem(BlockRegistry.DISTILLER.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> INCUBATOR =
            ITEMS.register("incubator", () -> new BlockItem(BlockRegistry.INCUBATOR.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> INCUBATOR_MERCURY_VESSEL =
            ITEMS.register("incubator_mercury_vessel", () -> new IncubatorMercuryVesselBlockItem(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> INCUBATOR_SALT_VESSEL =
            ITEMS.register("incubator_salt_vessel", () -> new IncubatorSaltVesselBlockItem(BlockRegistry.INCUBATOR_SALT_VESSEL.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> INCUBATOR_SULFUR_VESSEL =
            ITEMS.register("incubator_sulfur_vessel", () -> new IncubatorSulfurVesselBlockItem(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SAL_AMMONIAC_ACCUMULATOR =
            ITEMS.register("sal_ammoniac_accumulator", () -> new SalAmmoniacAccumulatorBlockItem(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SAL_AMMONIAC_TANK =
            ITEMS.register("sal_ammoniac_tank", () -> new SalAmmoniacTankBlockItem(BlockRegistry.SAL_AMMONIAC_TANK.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> MERCURY_CATALYST =
            ITEMS.register("mercury_catalyst", () -> new BlockItem(BlockRegistry.MERCURY_CATALYST.get(), new Item.Properties()));
    public static final DeferredItem<CaloricFluxEmitterBlockItem> CALORIC_FLUX_EMITTER =
            ITEMS.register("caloric_flux_emitter", () -> new CaloricFluxEmitterBlockItem(BlockRegistry.CALORIC_FLUX_EMITTER.get(), new Item.Properties()));
    public static final DeferredItem<SulfuricFluxEmitterBlockItem> SULFURIC_FLUX_EMITTER =
            ITEMS.register("sulfuric_flux_emitter", () -> new SulfuricFluxEmitterBlockItem(BlockRegistry.SULFURIC_FLUX_EMITTER.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> REFORMATION_SOURCE_PEDESTAL =
            ITEMS.register("reformation_source_pedestal", () -> new BlockItem(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> REFORMATION_TARGET_PEDESTAL =
            ITEMS.register("reformation_target_pedestal", () -> new BlockItem(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> REFORMATION_RESULT_PEDESTAL =
            ITEMS.register("reformation_result_pedestal", () -> new BlockItem(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> FERMENTATION_VAT =
            ITEMS.register("fermentation_vat", () -> new BlockItem(BlockRegistry.FERMENTATION_VAT.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> DIGESTION_VAT =
            ITEMS.register("digestion_vat", () -> new BlockItem(BlockRegistry.DIGESTION_VAT.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> LOGISTICS_CONNECTION_NODE =
            ITEMS.register(BlockRegistry.LOGISTICS_CONNECTION_NODE.getId().getPath(), () -> new BlockItem(BlockRegistry.LOGISTICS_CONNECTION_NODE.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> LOGISTICS_ITEM_INSERTER =
            ITEMS.register(BlockRegistry.LOGISTICS_ITEM_INSERTER.getId().getPath(), () -> new LogisticsItemConnectorBlockItem(BlockRegistry.LOGISTICS_ITEM_INSERTER.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> LOGISTICS_ITEM_EXTRACTOR =
            ITEMS.register(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.getId().getPath(), () -> new LogisticsItemConnectorBlockItem(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> LOGISTICS_FLUID_INSERTER =
            ITEMS.register(BlockRegistry.LOGISTICS_FLUID_INSERTER.getId().getPath(), () -> new LogisticsItemConnectorBlockItem(BlockRegistry.LOGISTICS_FLUID_INSERTER.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> LOGISTICS_FLUID_EXTRACTOR =
            ITEMS.register(BlockRegistry.LOGISTICS_FLUID_EXTRACTOR.getId().getPath(), () -> new LogisticsItemConnectorBlockItem(BlockRegistry.LOGISTICS_FLUID_EXTRACTOR.get(), new Item.Properties()));
}
