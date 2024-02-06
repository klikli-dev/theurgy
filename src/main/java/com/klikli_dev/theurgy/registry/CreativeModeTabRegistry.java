// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.DivinationRodItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeModeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Theurgy.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> THEURGY = CREATIVE_MODE_TABS.register(Theurgy.MODID, () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ItemRegistry.EMPTY_JAR_ICON.get().getDefaultInstance())
            .title(Component.translatable(TheurgyConstants.I18n.ITEM_GROUP))
            .displayItems((parameters, output) -> {
                DivinationRodItem.registerCreativeModeTabs(ItemRegistry.DIVINATION_ROD_T1.get(), output);
                DivinationRodItem.registerCreativeModeTabs(ItemRegistry.DIVINATION_ROD_T2.get(), output);
                DivinationRodItem.registerCreativeModeTabs(ItemRegistry.DIVINATION_ROD_T3.get(), output);
                DivinationRodItem.registerCreativeModeTabs(ItemRegistry.DIVINATION_ROD_T4.get(), output);
                DivinationRodItem.registerCreativeModeTabs(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get(), output);
                DivinationRodItem.registerCreativeModeTabs(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_COMMON.get(), output);
                DivinationRodItem.registerCreativeModeTabs(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE.get(), output);
                DivinationRodItem.registerCreativeModeTabs(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get(), output);
                DivinationRodItem.registerCreativeModeTabs(ItemRegistry.AMETHYST_DIVINATION_ROD.get(), output);

                output.accept(ItemRegistry.SAL_AMMONIAC_BUCKET.get());
                output.accept(ItemRegistry.COPPER_WIRE.get());
                output.accept(ItemRegistry.MERCURIAL_WAND.get());

                output.accept(ItemRegistry.MERCURY_SHARD.get());
                output.accept(ItemRegistry.MERCURY_CRYSTAL.get());

                output.accept(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get());
                output.accept(ItemRegistry.SAL_AMMONIAC_ORE.get());
                output.accept(ItemRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get());
                output.accept(ItemRegistry.PURIFIED_GOLD.get());

                output.accept(ItemRegistry.CALCINATION_OVEN.get());
                output.accept(ItemRegistry.PYROMANTIC_BRAZIER.get());
                output.accept(ItemRegistry.LIQUEFACTION_CAULDRON.get());
                output.accept(ItemRegistry.DISTILLER.get());
                output.accept(ItemRegistry.INCUBATOR.get());
                output.accept(ItemRegistry.INCUBATOR_MERCURY_VESSEL.get());
                output.accept(ItemRegistry.INCUBATOR_SALT_VESSEL.get());
                output.accept(ItemRegistry.INCUBATOR_SULFUR_VESSEL.get());

                output.accept(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get());
                output.accept(ItemRegistry.SAL_AMMONIAC_TANK.get());

                output.accept(ItemRegistry.MERCURY_CATALYST.get());
                output.accept(ItemRegistry.CALORIC_FLUX_EMITTER.get());

                output.accept(ItemRegistry.SULFURIC_FLUX_EMITTER.get());
                output.accept(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get());
                output.accept(ItemRegistry.REFORMATION_TARGET_PEDESTAL.get());
                output.accept(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get());

                output.accept(ItemRegistry.FERMENTATION_VAT.get());
                output.accept(ItemRegistry.DIGESTION_VAT.get());

                output.accept(ItemRegistry.LOGISTICS_ITEM_INSERTER.get());
                output.accept(ItemRegistry.LOGISTICS_ITEM_EXTRACTOR.get());

            }).build());

}
