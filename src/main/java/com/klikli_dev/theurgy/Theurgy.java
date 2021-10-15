/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
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

package com.klikli_dev.theurgy;

import com.klikli_dev.theurgy.config.ClientConfig;
import com.klikli_dev.theurgy.config.CommonConfig;
import com.klikli_dev.theurgy.config.ServerConfig;
import com.klikli_dev.theurgy.data.grafting_hedges.GraftingHedgeManager;
import com.klikli_dev.theurgy.datagen.DataGenerators;
import com.klikli_dev.theurgy.handlers.ClientSetupEventHandler;
import com.klikli_dev.theurgy.handlers.ColorEventHandler;
import com.klikli_dev.theurgy.handlers.RegistryEventHandler;
import com.klikli_dev.theurgy.handlers.TooltipHandler;
import com.klikli_dev.theurgy.item.TheurgyCreativeModeTab;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Theurgy.MODID)
public class Theurgy {
    public static final String MODID = TheurgyAPI.ID;
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final CreativeModeTab CREATIVE_MODE_TAB = new TheurgyCreativeModeTab();

    public static Theurgy INSTANCE;

    public Theurgy() {
        INSTANCE = this;

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.get().spec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.get().spec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.get().spec);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        BlockEntityRegistry.BLOCK_ENTITIES.register(modEventBus);

        //register event listener objects
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(GraftingHedgeManager.get());

        //register event listener classes
        modEventBus.register(RegistryEventHandler.class);
        modEventBus.register(DataGenerators.class);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.register(ColorEventHandler.class);
            modEventBus.register(ClientSetupEventHandler.class);
            MinecraftForge.EVENT_BUS.register(TooltipHandler.class);
        }
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }

    @SubscribeEvent
    public void onModConfigEvent(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == ClientConfig.get().spec) {
            //Clear the config cache on reload.
            ClientConfig.get().clear();
        }
        if (event.getConfig().getSpec() == CommonConfig.get().spec) {
            //Clear the config cache on reload.
            CommonConfig.get().clear();
        }
        if (event.getConfig().getSpec() == ServerConfig.get().spec) {
            //Clear the config cache on reload.
            ServerConfig.get().clear();
        }
    }

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(GraftingHedgeManager.get());
    }

    @SubscribeEvent
    public void onCommonSetup(final FMLCommonSetupEvent event) {
        Networking.registerMessages();
        LOGGER.info("Common setup complete.");
    }

    @SubscribeEvent
    public void onServerSetup(final FMLDedicatedServerSetupEvent event) {
        LOGGER.info("Dedicated server setup complete.");
    }
}
