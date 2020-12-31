/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.common.handlers;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.client.itemproperties.EssentiaGaugeItemPropertyGetter;
import com.github.klikli_dev.theurgy.client.render.tile.CrucibleRenderer;
import com.github.klikli_dev.theurgy.common.item.tool.EssentiaGaugeItem;
import com.github.klikli_dev.theurgy.registry.BlockRegistry;
import com.github.klikli_dev.theurgy.registry.ItemRegistry;
import com.github.klikli_dev.theurgy.registry.TileRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Theurgy.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEventHandler {

    //region Static Methods
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        //Register client side event handlers

        //Register Entity Renderers

        //Register Tile Entity Renderers
        ClientRegistry.bindTileEntityRenderer(TileRegistry.CRUCIBLE.get(), CrucibleRenderer::new);

        //Setup block render layers
        RenderTypeLookup.setRenderLayer(BlockRegistry.PRIMA_MATERIA_CRYSTAL.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.PURE_CRYSTAL.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.AER_CRYSTAL.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.AQUA_CRYSTAL.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.IGNIS_CRYSTAL.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.TERRA_CRYSTAL.get(), RenderType.getTranslucent());

        //register item model properties
        registerItemModelProperties(event);

        //Not safe to call during parallel load, so register to run threadsafe.
        event.enqueueWork(() -> {
            //Register screen factories

            Theurgy.LOGGER.debug("Registered Screen Containers");
        });

        Theurgy.LOGGER.info("Client setup complete.");
    }

    public static void registerItemModelProperties(FMLClientSetupEvent event) {

        //Not safe to call during parallel load, so register to run threadsafe
        event.enqueueWork(() -> {
            //Register item model properties
            ItemModelsProperties.registerProperty(ItemRegistry.ESSENTIA_GAUGE.get(),
                    new ResourceLocation(Theurgy.MODID, EssentiaGaugeItem.PROPERTY_TAG_NAME),
                    new EssentiaGaugeItemPropertyGetter());
            Theurgy.LOGGER.debug("Registered Item Properties");
        });
    }
    //endregion Static Methods
}
