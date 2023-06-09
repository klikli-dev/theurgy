/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.item.DivinationRodItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Theurgy.MODID);

    public static final RegistryObject<CreativeModeTab> THEURGY = CREATIVE_MODE_TABS.register(Theurgy.MODID, () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ItemRegistry.EMPTY_JAR.get().getDefaultInstance())
            .title(Component.translatable(TheurgyConstants.I18n.ITEM_GROUP))
            .displayItems((parameters, output) -> {
                AlchemicalSulfurItem.DistHelper.registerCreativeModeTabs(ItemRegistry.ALCHEMICAL_SULFUR.get(), output);
                DivinationRodItem.DistHelper.registerCreativeModeTabs(ItemRegistry.DIVINATION_ROD_T1.get(), output);
                DivinationRodItem.DistHelper.registerCreativeModeTabs(ItemRegistry.DIVINATION_ROD_T2.get(), output);
                DivinationRodItem.DistHelper.registerCreativeModeTabs(ItemRegistry.DIVINATION_ROD_T3.get(), output);
                DivinationRodItem.DistHelper.registerCreativeModeTabs(ItemRegistry.DIVINATION_ROD_T4.get(), output);
            }).build());

}
