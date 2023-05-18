/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy;

import com.klikli_dev.theurgy.client.ClientSetupEventHandler;
import com.klikli_dev.theurgy.client.render.CalcinationOvenRenderer;
import com.klikli_dev.theurgy.config.ClientConfig;
import com.klikli_dev.theurgy.config.CommonConfig;
import com.klikli_dev.theurgy.config.ServerConfig;
import com.klikli_dev.theurgy.datagen.DataGenerators;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.registry.*;
import com.klikli_dev.theurgy.tooltips.TooltipHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod(Theurgy.MODID)
public class Theurgy {
    public static final String MODID = "theurgy";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Theurgy INSTANCE;

    public Theurgy() {
        INSTANCE = this;

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.get().spec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.get().spec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.get().spec);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        GeckoLib.initialize();

        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        BlockEntityRegistry.BLOCK_ENTITIES.register(modEventBus);
        EntityRegistry.ENTITIES.register(modEventBus);
        EntityDataSerializerRegistry.ENTITY_DATA_SERIALIZERS.register(modEventBus);
        ParticleRegistry.PARTICLES.register(modEventBus);
        SoundRegistry.SOUNDS.register(modEventBus);
        RecipeRegistry.RECIPE_SERIALIZERS.register(modEventBus);

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onServerSetup);
        modEventBus.addListener(this::onClientSetup);

        modEventBus.addListener(DataGenerators::gatherData);
        modEventBus.addListener(ItemRegistry::onRegisterCreativeModeTabs);

        MinecraftForge.EVENT_BUS.addListener(TooltipHandler::onItemTooltipEvent);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(ClientSetupEventHandler::onClientSetup);
            modEventBus.addListener(ParticleRegistry::registerFactories);
            modEventBus.addListener(ClientSetupEventHandler::onRegisterEntityRenderers);
        }
    }

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MODID, path);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        Networking.registerMessages();

        LOGGER.info("Common setup complete.");
    }

    public void onServerSetup(FMLDedicatedServerSetupEvent event) {
        LOGGER.info("Dedicated server setup complete.");
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(BlockEntityRegistry.CALCINATION_OVEN_ENTITY.get(), CalcinationOvenRenderer::new);
    }
}
