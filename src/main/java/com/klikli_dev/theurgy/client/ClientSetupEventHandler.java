/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.client;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.client.render.BlankEntityRenderer;
import com.klikli_dev.theurgy.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.item.DivinationRodItem;
import com.klikli_dev.theurgy.registry.EntityRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.tooltips.TooltipHandler;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetupEventHandler {
    public static void onClientSetup(FMLClientSetupEvent event) {

        registerTooltipDataProviders(event);
        registerItemModelProperties(event);

        Theurgy.LOGGER.info("Client setup complete.");
    }

    public static void registerTooltipDataProviders(FMLClientSetupEvent event) {
        TooltipHandler.registerNamespaceToListenTo(Theurgy.MODID);

        TooltipHandler.registerTooltipDataProvider(ItemRegistry.ALCHEMICAL_SULFUR.get(), AlchemicalSulfurItem::getTooltipData);
    }

    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.FOLLOW_PROJECTILE.get(), BlankEntityRenderer::new);

    }

    public static void registerItemModelProperties(FMLClientSetupEvent event) {
        //Not safe to call during parallel load, so register to run threadsafe
        event.enqueueWork(() -> {
            ItemProperties.register(ItemRegistry.DIVINATION_ROD_T1.get(),
                    TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, DivinationRodItem.PropertyFunctions.DIVINATION_DISTANCE);

            Theurgy.LOGGER.debug("Registered Item Properties");
        });
    }
}
