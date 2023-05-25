/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy;

import com.klikli_dev.modonomicon.client.render.page.PageRendererRegistry;
import com.klikli_dev.theurgy.config.ClientConfig;
import com.klikli_dev.theurgy.config.CommonConfig;
import com.klikli_dev.theurgy.config.ServerConfig;
import com.klikli_dev.theurgy.content.item.AlchemicalSaltItem;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.DivinationRodItem;
import com.klikli_dev.theurgy.content.renderer.BlankEntityRenderer;
import com.klikli_dev.theurgy.content.renderer.DistillerRenderer;
import com.klikli_dev.theurgy.content.renderer.LiquefactionCauldronRenderer;
import com.klikli_dev.theurgy.datagen.TheurgyDataGenerators;
import com.klikli_dev.theurgy.integration.modonomicon.PageLoaders;
import com.klikli_dev.theurgy.integration.modonomicon.PageRenderers;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.registry.*;
import com.klikli_dev.theurgy.tooltips.TooltipHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.model.DynamicFluidContainerModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

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

        ItemRegistry.ITEMS.register(modEventBus);
        SulfurRegistry.SULFURS.register(modEventBus);
        SaltRegistry.SALTS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        BlockEntityRegistry.BLOCKS.register(modEventBus);
        FluidTypeRegistry.FLUID_TYPES.register(modEventBus);
        FluidRegistry.FLUIDS.register(modEventBus);
        EntityRegistry.ENTITIES.register(modEventBus);
        EntityDataSerializerRegistry.ENTITY_DATA_SERIALIZERS.register(modEventBus);
        ParticleRegistry.PARTICLES.register(modEventBus);
        SoundRegistry.SOUNDS.register(modEventBus);
        RecipeSerializerRegistry.RECIPE_SERIALIZERS.register(modEventBus);
        RecipeTypeRegistry.RECIPE_TYPES.register(modEventBus);
        MenuTypeRegistry.MENU_TYPES.register(modEventBus);

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onServerSetup);

        modEventBus.addListener(TheurgyDataGenerators::onGatherData);
        modEventBus.addListener(ItemRegistry::onRegisterCreativeModeTabs);
        modEventBus.addListener(SulfurRegistry::onBuildCreativeModTabs);
        modEventBus.addListener(SaltRegistry::onBuildCreativeModTabs);
        modEventBus.addListener(RecipeSerializerRegistry::onRegisterRecipeSerializers);

        MinecraftForge.EVENT_BUS.addListener(TooltipHandler::onItemTooltipEvent);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(ParticleRegistry::registerFactories);
            modEventBus.addListener(Client::onClientSetup);
            modEventBus.addListener(Client::onRegisterEntityRenderers);
            modEventBus.addListener(Client::onRegisterItemColors);
            MinecraftForge.EVENT_BUS.addListener(Client::onRecipesUpdated);
        }
    }

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MODID, path);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        Networking.registerMessages();

        PageLoaders.onCommonSetup(event);

        LOGGER.info("Common setup complete.");
    }

    public void onServerSetup(FMLDedicatedServerSetupEvent event) {
        LOGGER.info("Dedicated server setup complete.");
    }

    public static class Client {
        public static void onClientSetup(FMLClientSetupEvent event) {

            registerTooltipDataProviders(event);
            registerItemProperties(event);

            PageRenderers.onClientSetup(event);

            LOGGER.info("Client setup complete.");
        }

        public static void onRecipesUpdated(RecipesUpdatedEvent event) {
            //now disable rendering of sulfurs that have no recipe -> otherwise we see "no source" sulfurs in tag recipes
            //See also JeiPlugin.registerRecipes
            var registryAccess = Minecraft.getInstance().level.registryAccess();
            var liquefactionRecipes = event.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get());

            SulfurRegistry.SULFURS.getEntries().stream()
                    .map(RegistryObject::get)
                    .map(AlchemicalSulfurItem.class::cast)
                    .filter(sulfur -> liquefactionRecipes.stream().noneMatch(r -> r.getResultItem(registryAccess) != null && r.getResultItem(registryAccess).getItem() == sulfur)).map(ItemStack::new).forEach(PageRendererRegistry::registerItemStackNotToRender);
        }

        public static void registerTooltipDataProviders(FMLClientSetupEvent event) {
            TooltipHandler.registerNamespaceToListenTo(MODID);

            SulfurRegistry.SULFURS.getEntries().stream().map(RegistryObject::get).map(AlchemicalSulfurItem.class::cast).forEach(sulfur -> {
                if (sulfur.provideAutomaticTooltipData) {
                    TooltipHandler.registerTooltipDataProvider(sulfur, AlchemicalSulfurItem::getTooltipData);
                }
            });

            SaltRegistry.SALTS.getEntries().stream().map(RegistryObject::get).map(AlchemicalSaltItem.class::cast).forEach(salt -> {
                TooltipHandler.registerTooltipDataProvider(salt, AlchemicalSaltItem::getTooltipData);
            });
        }

        public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(EntityRegistry.FOLLOW_PROJECTILE.get(), BlankEntityRenderer::new);
            event.registerBlockEntityRenderer(BlockEntityRegistry.LIQUEFACTION_CAULDRON.get(), LiquefactionCauldronRenderer::new);
            event.registerBlockEntityRenderer(BlockEntityRegistry.DISTILLER.get(), DistillerRenderer::new);
        }


        public static void registerItemProperties(FMLClientSetupEvent event) {
            //Not safe to call during parallel load, so register to run threadsafe
            event.enqueueWork(() -> {
                ItemRegistry.ITEMS.getEntries().stream().filter(item -> item.get() instanceof DivinationRodItem).forEach(item -> {
                    ItemProperties.register(item.get(),
                            TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, DivinationRodItem.DistHelper.DIVINATION_DISTANCE);
                    LOGGER.debug("Registered Divination Rod Properties for: {}", item.getKey());
                });

                LOGGER.debug("Finished registering Item Properties.");
            });
        }

        public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
            event.register(new DynamicFluidContainerModel.Colors(), ItemRegistry.SAL_AMMONIAC_BUCKET.get());
        }
    }
}
