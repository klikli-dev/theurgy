// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy;

import com.klikli_dev.modonomicon.client.render.page.PageRendererRegistry;
import com.klikli_dev.theurgy.config.ClientConfig;
import com.klikli_dev.theurgy.config.CommonConfig;
import com.klikli_dev.theurgy.config.ServerConfig;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.render.CalcinationOvenRenderer;
import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatRenderer;
import com.klikli_dev.theurgy.content.apparatus.distiller.render.DistillerRenderer;
import com.klikli_dev.theurgy.content.apparatus.incubator.render.IncubatorMercuryVesselRenderer;
import com.klikli_dev.theurgy.content.apparatus.incubator.render.IncubatorSaltVesselRenderer;
import com.klikli_dev.theurgy.content.apparatus.incubator.render.IncubatorSulfurVesselRenderer;
import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.render.LiquefactionCauldronRenderer;
import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlock;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.render.SalAmmoniacAccumulatorRenderer;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.render.SalAmmoniacTankRenderer;
import com.klikli_dev.theurgy.content.item.AlchemicalSaltItem;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.DivinationRodItem;
import com.klikli_dev.theurgy.content.render.BlankEntityRenderer;
import com.klikli_dev.theurgy.content.render.ClientTicks;
import com.klikli_dev.theurgy.content.render.TheurgyModelLayers;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import com.klikli_dev.theurgy.datagen.TheurgyDataGenerators;
import com.klikli_dev.theurgy.integration.modonomicon.PageLoaders;
import com.klikli_dev.theurgy.integration.modonomicon.PageRenderers;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.registry.*;
import com.klikli_dev.theurgy.tooltips.TooltipHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.slf4j.Logger;


@Mod(Theurgy.MODID)
public class Theurgy {
    public static final String MODID = "theurgy";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Theurgy INSTANCE;

    public Theurgy(IEventBus modEventBus) {
        INSTANCE = this;

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.get().spec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.get().spec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.get().spec);

        ItemRegistry.ITEMS.register(modEventBus);
        CreativeModeTabRegistry.CREATIVE_MODE_TABS.register(modEventBus);
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
        modEventBus.addListener(SulfurRegistry::onBuildCreativeModTabs);
        modEventBus.addListener(SaltRegistry::onBuildCreativeModTabs);
        modEventBus.addListener(CapabilityRegistry::onRegisterCapabilities);
        modEventBus.addListener(RecipeSerializerRegistry::onRegisterRecipeSerializers);

        NeoForge.EVENT_BUS.addListener(TooltipHandler::onItemTooltipEvent);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(ParticleRegistry::registerFactories);
            modEventBus.addListener(Client::onClientSetup);
            modEventBus.addListener(Client::onRegisterEntityRendererLayerDefinitions);
            modEventBus.addListener(Client::onRegisterEntityRenderers);
            modEventBus.addListener(Client::onRegisterItemColors);
            modEventBus.addListener(Client::onRegisterBlockColors);
            NeoForge.EVENT_BUS.addListener(Client::onRenderLevelStage);
            NeoForge.EVENT_BUS.addListener(Client::onClientTick);
            NeoForge.EVENT_BUS.addListener(Client::onRecipesUpdated);
            NeoForge.EVENT_BUS.addListener(Client::onRightClick);
            NeoForge.EVENT_BUS.addListener(Client::onLeftClick);
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

            NeoForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent e) -> {
                if (e.phase == TickEvent.Phase.END) {
                    ClientTicks.endClientTick(Minecraft.getInstance());
                }
            });
            NeoForge.EVENT_BUS.addListener((TickEvent.RenderTickEvent e) -> {
                if (e.phase == TickEvent.Phase.START) {
                    ClientTicks.renderTickStart(e.renderTickTime);
                } else {
                    ClientTicks.renderTickEnd();
                }
            });

            LOGGER.info("Client setup complete.");
        }

        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null)
                return;

            if (event.phase == TickEvent.Phase.START) {
                return;
            }

            Outliner.get().tick();
            BlockRegistry.CALORIC_FLUX_EMITTER.get().selectionBehaviour().tick(Minecraft.getInstance().player);
            BlockRegistry.SULFURIC_FLUX_EMITTER.get().selectionBehaviour().tick(Minecraft.getInstance().player);
        }

        public static void onRenderLevelStage(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES)
                return;

            PoseStack ms = event.getPoseStack();
            ms.pushPose();

            var buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            float partialTicks = ClientTicks.getPartialTicksHandlePause();
            Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera()
                    .getPosition();

            Outliner.get().render(ms, buffer, camera, partialTicks);

            buffer.endBatch();
            RenderSystem.enableCull();
            ms.popPose();
        }

        public static void onRecipesUpdated(RecipesUpdatedEvent event) {
            //now disable rendering of sulfurs that have no recipe -> otherwise we see "no source" sulfurs in tag recipes
            //See also JeiPlugin.registerRecipes
            var registryAccess = Minecraft.getInstance().level.registryAccess();
            var liquefactionRecipes = event.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get());

            SulfurRegistry.SULFURS.getEntries().stream()
                    .map(RegistryObject::get)
                    .map(AlchemicalSulfurItem.class::cast)
                    .filter(sulfur -> !SulfurRegistry.keepInItemLists(sulfur))
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

        public static void onRegisterEntityRendererLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(TheurgyModelLayers.DIGESTION_VAT_BASE, DigestionVatRenderer::createBaseLayer);
            event.registerLayerDefinition(TheurgyModelLayers.DIGESTION_VAT_SIDES, DigestionVatRenderer::createSidesLayer);
        }

        public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(EntityRegistry.FOLLOW_PROJECTILE.get(), BlankEntityRenderer::new);
            event.registerBlockEntityRenderer(BlockEntityRegistry.LIQUEFACTION_CAULDRON.get(), LiquefactionCauldronRenderer::new);
            event.registerBlockEntityRenderer(BlockEntityRegistry.DISTILLER.get(), DistillerRenderer::new);
            event.registerBlockEntityRenderer(BlockEntityRegistry.CALCINATION_OVEN.get(), CalcinationOvenRenderer::new);
            event.registerBlockEntityRenderer(BlockEntityRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), SalAmmoniacAccumulatorRenderer::new);
            event.registerBlockEntityRenderer(BlockEntityRegistry.INCUBATOR_MERCURY_VESSEL.get(), IncubatorMercuryVesselRenderer::new);
            event.registerBlockEntityRenderer(BlockEntityRegistry.INCUBATOR_SULFUR_VESSEL.get(), IncubatorSulfurVesselRenderer::new);
            event.registerBlockEntityRenderer(BlockEntityRegistry.INCUBATOR_SALT_VESSEL.get(), IncubatorSaltVesselRenderer::new);
            event.registerBlockEntityRenderer(BlockEntityRegistry.SAL_AMMONIAC_TANK.get(), SalAmmoniacTankRenderer::new);
            event.registerBlockEntityRenderer(BlockEntityRegistry.DIGESTION_VAT.get(), DigestionVatRenderer::new);
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
            event.register(MercuryCatalystBlock::getItemColor, ItemRegistry.MERCURY_CATALYST.get());
        }

        public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
            event.register(MercuryCatalystBlock::getBlockColor, BlockRegistry.MERCURY_CATALYST.get());
        }

        public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
            if (BlockRegistry.CALORIC_FLUX_EMITTER.get().selectionBehaviour().onRightClickBlock(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
                return;
            }

            if (BlockRegistry.SULFURIC_FLUX_EMITTER.get().selectionBehaviour().onRightClickBlock(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }

        public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
            if (BlockRegistry.CALORIC_FLUX_EMITTER.get().selectionBehaviour().onLeftClickBlock(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
                return;
            }

            if (BlockRegistry.SULFURIC_FLUX_EMITTER.get().selectionBehaviour().onLeftClickBlock(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }
}
