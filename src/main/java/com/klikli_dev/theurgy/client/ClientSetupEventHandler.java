/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.client;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.tooltips.TooltipHandler;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetupEventHandler {
    public static void onClientSetup(FMLClientSetupEvent event) {

        registerTooltipDataProviders(event);

        Theurgy.LOGGER.info("Client setup complete.");
    }

    public static void registerTooltipDataProviders(FMLClientSetupEvent event) {
        TooltipHandler.registerNamespaceToListenTo(Theurgy.MODID);

        TooltipHandler.registerTooltipDataProvider(ItemRegistry.ALCHEMICAL_SULFUR.get(), AlchemicalSulfurItem::getTooltipData);
    }
}
