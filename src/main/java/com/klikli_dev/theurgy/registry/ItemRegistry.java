// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlockItem;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterBlockItem;
import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlockItem;
import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.MercuryCapacitorBlockItem;
import com.klikli_dev.theurgy.content.apparatus.distiller.DistillerBlockItem;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorMercuryVesselBlockItem;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSaltVesselBlockItem;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSulfurVesselBlockItem;
import com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector.LogisticsFluidConnectorBlockItem;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlockItem;
import com.klikli_dev.theurgy.content.apparatus.logisticsnexus.LogisticsNexusBlockItem;
import com.klikli_dev.theurgy.content.apparatus.mercuryfluxemitter.MercuryFluxEmitterBlockItem;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterBlockItem;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.SalAmmoniacAccumulatorBlockItem;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.SalAmmoniacTankBlockItem;
import com.klikli_dev.theurgy.content.item.book.TheHermeticaItem;
import com.klikli_dev.theurgy.content.item.divinationrod.DivinationRodItem;
import com.klikli_dev.theurgy.content.item.filter.FilterUiStyles;
import com.klikli_dev.theurgy.content.item.mercurialwand.MercurialWandItem;
import com.klikli_dev.theurgy.content.item.mercurialwand.mode.MercurialWandItemMode;
import com.klikli_dev.theurgy.content.item.wire.WireItem;
import com.klikli_dev.codedefinedgui.filter.attribute.AttributeFilterItem;
import com.klikli_dev.codedefinedgui.filter.list.ListFilterItem;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Theurgy.MODID);

    //Helper items for rendering
    public static final DeferredItem<Item> EMPTY_JAR_ICON =
            ITEMS.registerItem("empty_jar_icon", Item::new);
    public static final DeferredItem<Item> EMPTY_JAR_IRON_BAND_ICON =
            ITEMS.registerItem("empty_jar_iron_band_icon", Item::new);

    public static final DeferredItem<Item> EMPTY_JAR_LABELED_ICON =
            ITEMS.registerItem("empty_jar_labeled_icon", Item::new);
    public static final DeferredItem<Item> EMPTY_CERAMIC_JAR_ICON =
            ITEMS.registerItem("empty_ceramic_jar_icon", Item::new);
    public static final DeferredItem<Item> EMPTY_CERAMIC_JAR_LABELED_ICON =
            ITEMS.registerItem("empty_ceramic_jar_labeled_icon", Item::new);

    public static final DeferredItem<Item> JAR_LABEL_ICON =
            ITEMS.registerItem("jar_label_icon", Item::new);

    public static final DeferredItem<Item> JAR_LABEL_FRAME_ABUNDANT_ICON =
            ITEMS.registerItem("jar_label_frame_abundant_icon", Item::new);
    public static final DeferredItem<Item> JAR_LABEL_FRAME_COMMON_ICON =
            ITEMS.registerItem("jar_label_frame_common_icon", Item::new);
    public static final DeferredItem<Item> JAR_LABEL_FRAME_RARE_ICON =
            ITEMS.registerItem("jar_label_frame_rare_icon", Item::new);
    public static final DeferredItem<Item> JAR_LABEL_FRAME_PRECIOUS_ICON =
            ITEMS.registerItem("jar_label_frame_precious_icon", Item::new);
    public static final DeferredItem<Item> THE_HERMETICA_ICON =
            ITEMS.registerItem("the_hermetica_icon", Item::new);

    public static final DeferredItem<Item> SALT_ICON =
            ITEMS.registerItem("salt_icon", Item::new);


    public static final DeferredItem<Item> GEMS_ABUNDANT_ICON = ITEMS.registerItem("gems_abundant_icon", Item::new);
    public static final DeferredItem<Item> GEMS_COMMON_ICON = ITEMS.registerItem("gems_common_icon", Item::new);
    public static final DeferredItem<Item> GEMS_RARE_ICON = ITEMS.registerItem("gems_rare_icon", Item::new);
    public static final DeferredItem<Item> GEMS_PRECIOUS_ICON = ITEMS.registerItem("gems_precious_icon", Item::new);

    public static final DeferredItem<Item> METALS_ABUNDANT_ICON = ITEMS.registerItem("metals_abundant_icon", Item::new);
    public static final DeferredItem<Item> METALS_COMMON_ICON = ITEMS.registerItem("metals_common_icon", Item::new);
    public static final DeferredItem<Item> METALS_RARE_ICON = ITEMS.registerItem("metals_rare_icon", Item::new);
    public static final DeferredItem<Item> METALS_PRECIOUS_ICON = ITEMS.registerItem("metals_precious_icon", Item::new);

    public static final DeferredItem<Item> OTHER_MINERALS_ABUNDANT_ICON = ITEMS.registerItem("other_minerals_abundant_icon", Item::new);
    public static final DeferredItem<Item> OTHER_MINERALS_COMMON_ICON = ITEMS.registerItem("other_minerals_common_icon", Item::new);
    public static final DeferredItem<Item> OTHER_MINERALS_RARE_ICON = ITEMS.registerItem("other_minerals_rare_icon", Item::new);
    public static final DeferredItem<Item> OTHER_MINERALS_PRECIOUS_ICON = ITEMS.registerItem("other_minerals_precious_icon", Item::new);
    //Divination rods
    public static final DeferredItem<DivinationRodItem> DIVINATION_ROD_T1 =
            ITEMS.registerItem("divination_rod_t1", p -> new DivinationRodItem(
                    p.stacksTo(1).durability(1),
                    ToolMaterial.STONE, BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 8, true));

    public static final DeferredItem<DivinationRodItem> DIVINATION_ROD_T2 =
            ITEMS.registerItem("divination_rod_t2", p -> new DivinationRodItem(
                    p.stacksTo(1).durability(1),
                    ToolMaterial.IRON, BlockTagRegistry.DIVINATION_ROD_T2_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T2_DISALLOWED_BLOCKS,
                    96, 40, 16, true));
    public static final DeferredItem<DivinationRodItem> DIVINATION_ROD_T3 =
            ITEMS.registerItem("divination_rod_t3", p -> new DivinationRodItem(
                    p.stacksTo(1).durability(1),
                    ToolMaterial.DIAMOND, BlockTagRegistry.DIVINATION_ROD_T3_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T3_DISALLOWED_BLOCKS,
                    96, 40, 32, true));
    public static final DeferredItem<DivinationRodItem> DIVINATION_ROD_T4 =

            ITEMS.registerItem("divination_rod_t4", p -> new DivinationRodItem(
                    p.stacksTo(1).durability(1),
                    ToolMaterial.NETHERITE, BlockTagRegistry.DIVINATION_ROD_T4_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T4_DISALLOWED_BLOCKS,
                    96, 40, 128, true));

    public static final DeferredItem<DivinationRodItem> SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT =
            ITEMS.registerItem("sulfur_attuned_divination_rod_abundant", p -> new DivinationRodItem(
                    p.stacksTo(1).durability(1),
                    ToolMaterial.STONE, BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 16, false));

    public static final DeferredItem<DivinationRodItem> SULFUR_ATTUNED_DIVINATION_ROD_COMMON =
            ITEMS.registerItem("sulfur_attuned_divination_rod_common", p -> new DivinationRodItem(
                    p.stacksTo(1).durability(1),
                    ToolMaterial.STONE, BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 16, false));

    public static final DeferredItem<DivinationRodItem> SULFUR_ATTUNED_DIVINATION_ROD_RARE =
            ITEMS.registerItem("sulfur_attuned_divination_rod_rare", p -> new DivinationRodItem(
                    p.stacksTo(1).durability(1),
                    ToolMaterial.STONE, BlockTagRegistry.DIVINATION_ROD_T2_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T2_DISALLOWED_BLOCKS,
                    96, 40, 32, false));

    public static final DeferredItem<DivinationRodItem> SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS =
            ITEMS.registerItem("sulfur_attuned_divination_rod_precious", p -> new DivinationRodItem(
                    p.stacksTo(1).durability(1),
                    ToolMaterial.STONE, BlockTagRegistry.DIVINATION_ROD_T3_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T3_DISALLOWED_BLOCKS,
                    96, 40, 32, false));

    public static final DeferredItem<DivinationRodItem> AMETHYST_DIVINATION_ROD =
            ITEMS.registerItem("amethyst_divination_rod", p -> new DivinationRodItem(
                    p.stacksTo(1).durability(1),
                    ToolMaterial.STONE, BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 16, false));

    //Other Tools
    public static final DeferredItem<TheHermeticaItem> THE_HERMETICA =
            ITEMS.registerItem("the_hermetica", TheHermeticaItem::new);

    public static final DeferredItem<Item> COPPER_WIRE =
            ITEMS.registerItem("copper_wire", p -> new WireItem(p, 32));

    public static final DeferredItem<MercurialWandItem> MERCURIAL_WAND =
            ITEMS.registerItem("mercurial_wand", p -> new MercurialWandItem(p
                    .component(DataComponentRegistry.MERCURIAL_WAND_ITEM_MODE.get(), MercurialWandItemMode.Type.CYCLE_DIRECTION.mode())
                    .component(DataComponentRegistry.SELECTED_FREQUENCY.get(), 0)
            ));

    public static final DeferredItem<Item> LIST_FILTER = ITEMS.registerItem("list_filter", p -> new ListFilterItem(p, FilterUiStyles.THEURGY_LIST));
    public static final DeferredItem<Item> ATTRIBUTE_FILTER = ITEMS.registerItem("attribute_filter", p -> new AttributeFilterItem(p, FilterUiStyles.THEURGY_ATTRIBUTE));

    //Buckets
    public static final DeferredItem<Item> SAL_AMMONIAC_BUCKET =
            ITEMS.registerItem("sal_ammoniac_bucket", p -> new BucketItem(
                    FluidRegistry.SAL_AMMONIAC.get(), p.craftRemainder(Items.BUCKET).stacksTo(1))
            );

    //Ingredients
    public static final DeferredItem<Item> MERCURY_SHARD =
            ITEMS.registerItem("mercury_shard", Item::new);
    public static final DeferredItem<Item> MERCURY_CRYSTAL =
            ITEMS.registerItem("mercury_crystal", Item::new);
    public static final DeferredItem<Item> SAL_AMMONIAC_CRYSTAL =
            ITEMS.registerItem("sal_ammoniac_crystal", Item::new);
    public static final DeferredItem<Item> PURIFIED_GOLD =
            ITEMS.registerItem("purified_gold", Item::new);
    public static final DeferredItem<Item> FERMENTATION_STARTER =
            ITEMS.registerItem("fermentation_starter", Item::new);

    public static final DeferredItem<Item> CRYSTALLIZED_WATER =
            ITEMS.registerItem("crystallized_water", Item::new);
    public static final DeferredItem<Item> CRYSTALLIZED_LAVA =
            ITEMS.registerItem("crystallized_lava", Item::new);

    //Blocks
    public static final DeferredItem<BlockItem> SAL_AMMONIAC_ORE =
            ITEMS.registerItem("sal_ammoniac_ore", p -> new BlockItem(BlockRegistry.SAL_AMMONIAC_ORE.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> DEEPSLATE_SAL_AMMONIAC_ORE =
            ITEMS.registerItem("deepslate_sal_ammoniac_ore", p -> new BlockItem(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> CALCINATION_OVEN =
            ITEMS.registerItem("calcination_oven", p -> new CalcinationOvenBlockItem(BlockRegistry.CALCINATION_OVEN.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> PYROMANTIC_BRAZIER =
            ITEMS.registerItem("pyromantic_brazier", p -> new BlockItem(BlockRegistry.PYROMANTIC_BRAZIER.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> LIQUEFACTION_CAULDRON =
            ITEMS.registerItem("liquefaction_cauldron", p -> new BlockItem(BlockRegistry.LIQUEFACTION_CAULDRON.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> DISTILLER =
            ITEMS.registerItem("distiller", p -> new DistillerBlockItem(BlockRegistry.DISTILLER.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> INCUBATOR =
            ITEMS.registerItem("incubator", p -> new BlockItem(BlockRegistry.INCUBATOR.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> INCUBATOR_MERCURY_VESSEL =
            ITEMS.registerItem("incubator_mercury_vessel", p -> new IncubatorMercuryVesselBlockItem(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> INCUBATOR_SALT_VESSEL =
            ITEMS.registerItem("incubator_salt_vessel", p -> new IncubatorSaltVesselBlockItem(BlockRegistry.INCUBATOR_SALT_VESSEL.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> INCUBATOR_SULFUR_VESSEL =
            ITEMS.registerItem("incubator_sulfur_vessel", p -> new IncubatorSulfurVesselBlockItem(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> SAL_AMMONIAC_ACCUMULATOR =
            ITEMS.registerItem("sal_ammoniac_accumulator", p -> new SalAmmoniacAccumulatorBlockItem(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> SAL_AMMONIAC_TANK =
            ITEMS.registerItem("sal_ammoniac_tank", p -> new SalAmmoniacTankBlockItem(BlockRegistry.SAL_AMMONIAC_TANK.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> LOGISTICS_NEXUS =
            ITEMS.registerItem("logistics_nexus", p -> new LogisticsNexusBlockItem(BlockRegistry.LOGISTICS_NEXUS.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<MercuryCatalystBlockItem> MERCURY_CATALYST =
            ITEMS.registerItem("mercury_catalyst", p -> new MercuryCatalystBlockItem(BlockRegistry.MERCURY_CATALYST.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<MercuryCapacitorBlockItem> MERCURY_CAPACITOR =
            ITEMS.registerItem("mercury_capacitor", p -> new MercuryCapacitorBlockItem(BlockRegistry.MERCURY_CAPACITOR.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<CaloricFluxEmitterBlockItem> CALORIC_FLUX_EMITTER =
            ITEMS.registerItem("caloric_flux_emitter", p -> new CaloricFluxEmitterBlockItem(BlockRegistry.CALORIC_FLUX_EMITTER.get(), p.useBlockDescriptionPrefix()));
    public static final DeferredItem<SulfuricFluxEmitterBlockItem> SULFURIC_FLUX_EMITTER =
            ITEMS.registerItem("sulfuric_flux_emitter", p -> new SulfuricFluxEmitterBlockItem(BlockRegistry.SULFURIC_FLUX_EMITTER.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<MercuryFluxEmitterBlockItem> MERCURY_FLUX_EMITTER =
            ITEMS.registerItem("mercury_flux_emitter", p -> new MercuryFluxEmitterBlockItem(BlockRegistry.MERCURY_FLUX_EMITTER.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<BlockItem> REFORMATION_SOURCE_PEDESTAL =
            ITEMS.registerItem("reformation_source_pedestal", p -> new BlockItem(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<BlockItem> REFORMATION_TARGET_PEDESTAL =
            ITEMS.registerItem("reformation_target_pedestal", p -> new BlockItem(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<BlockItem> REFORMATION_RESULT_PEDESTAL =
            ITEMS.registerItem("reformation_result_pedestal", p -> new BlockItem(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<BlockItem> FERMENTATION_VAT =
            ITEMS.registerItem("fermentation_vat", p -> new BlockItem(BlockRegistry.FERMENTATION_VAT.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<BlockItem> DIGESTION_VAT =
            ITEMS.registerItem("digestion_vat", p -> new BlockItem(BlockRegistry.DIGESTION_VAT.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<BlockItem> LOGISTICS_CONNECTION_NODE =
            ITEMS.registerItem(BlockRegistry.LOGISTICS_CONNECTION_NODE.getId().getPath(), p -> new BlockItem(BlockRegistry.LOGISTICS_CONNECTION_NODE.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<BlockItem> LOGISTICS_ITEM_INSERTER =
            ITEMS.registerItem(BlockRegistry.LOGISTICS_ITEM_INSERTER.getId().getPath(), p -> new LogisticsItemConnectorBlockItem(BlockRegistry.LOGISTICS_ITEM_INSERTER.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<BlockItem> LOGISTICS_ITEM_EXTRACTOR =
            ITEMS.registerItem(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.getId().getPath(), p -> new LogisticsItemConnectorBlockItem(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<BlockItem> LOGISTICS_FLUID_INSERTER =
            ITEMS.registerItem(BlockRegistry.LOGISTICS_FLUID_INSERTER.getId().getPath(), p -> new LogisticsFluidConnectorBlockItem(BlockRegistry.LOGISTICS_FLUID_INSERTER.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<BlockItem> LOGISTICS_FLUID_EXTRACTOR =
            ITEMS.registerItem(BlockRegistry.LOGISTICS_FLUID_EXTRACTOR.getId().getPath(), p -> new LogisticsFluidConnectorBlockItem(BlockRegistry.LOGISTICS_FLUID_EXTRACTOR.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<BlockItem> LOGISTICS_MERCURY_FLUX_CONNECTOR =
            ITEMS.registerItem(BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.getId().getPath(), p -> new BlockItem(BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<BlockItem> LOGISTICS_CAPABILITY_PROBE =
            ITEMS.registerItem(BlockRegistry.LOGISTICS_CAPABILITY_PROBE.getId().getPath(), p -> new BlockItem(BlockRegistry.LOGISTICS_CAPABILITY_PROBE.get(), p.useBlockDescriptionPrefix()));

    public static final DeferredItem<BlockItem> LOGISTICS_CAPABILITY_PROXY =
            ITEMS.registerItem(BlockRegistry.LOGISTICS_CAPABILITY_PROXY.getId().getPath(), p -> new BlockItem(BlockRegistry.LOGISTICS_CAPABILITY_PROXY.get(), p.useBlockDescriptionPrefix()));
}
