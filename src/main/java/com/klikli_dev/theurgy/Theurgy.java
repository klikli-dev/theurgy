// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy;

import com.klikli_dev.theurgy.config.ClientConfig;
import com.klikli_dev.theurgy.config.CommonConfig;
import com.klikli_dev.theurgy.config.ServerConfig;
import com.klikli_dev.theurgy.gametest.GameTestRegistry;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.render.CalcinationOvenRenderer;
import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatRenderer;
import com.klikli_dev.theurgy.content.apparatus.distiller.render.DistillerRenderer;
import com.klikli_dev.theurgy.content.apparatus.incubator.render.IncubatorMercuryVesselRenderer;
import com.klikli_dev.theurgy.content.apparatus.incubator.render.IncubatorSaltVesselRenderer;
import com.klikli_dev.theurgy.content.apparatus.incubator.render.IncubatorSulfurVesselRenderer;
import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.render.LiquefactionCauldronRenderer;
import com.klikli_dev.theurgy.content.apparatus.logisticsnexus.render.LogisticsNexusRenderer;
import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlock;
import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.MercuryCapacitorBlock;
import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.render.MercuryCapacitorRenderer;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.render.SalAmmoniacAccumulatorRenderer;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.render.SalAmmoniacTankRenderer;
import com.klikli_dev.theurgy.content.item.HandlesOnLeftClick;
import com.klikli_dev.theurgy.content.item.HandlesOnScroll;
import com.klikli_dev.theurgy.content.item.filter.FilterScreenStyle;
import com.klikli_dev.theurgy.content.item.derivative.AlchemicalDerivativeItem;
import com.klikli_dev.theurgy.content.item.renderer.DivinationDistanceProperty;
import com.klikli_dev.theurgy.content.item.salt.AlchemicalSaltItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.wire.WireItem;
import com.klikli_dev.theurgy.content.render.*;
import com.klikli_dev.theurgy.content.render.inworldhud.ClientItemStacksTooltip;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUD;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDRegistry;
import com.klikli_dev.theurgy.content.render.inworldhud.ItemStacksTooltip;
import com.klikli_dev.theurgy.content.render.itemhud.ItemHUD;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import com.klikli_dev.theurgy.recipe.TheurgyRecipeManager;
import com.klikli_dev.theurgy.recipe.TheurgyRecipeManagerClient;
import com.klikli_dev.theurgy.util.ScrollHelper;
import com.klikli_dev.theurgy.datagen.TheurgyDataGenerators;
import com.klikli_dev.theurgy.integration.modonomicon.PageLoaders;
import com.klikli_dev.theurgy.integration.modonomicon.PageRenderers;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.logistics.WireRenderer;
import com.klikli_dev.theurgy.logistics.WireSync;
import com.klikli_dev.theurgy.logistics.Wires;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageOnLeftClickEmpty;
import com.klikli_dev.theurgy.registry.*;
import com.klikli_dev.theurgy.tooltips.TooltipHandler;
import com.klikli_dev.theurgy.util.LevelUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.fluid.FluidTintSources;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.slf4j.Logger;

import java.util.List;

@Mod(Theurgy.MODID)
public class Theurgy {
    public static final String MODID = "theurgy";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Theurgy(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.get().spec);
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.get().spec);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.get().spec);

        ItemRegistry.ITEMS.register(modEventBus);
        CreativeModeTabRegistry.CREATIVE_MODE_TABS.register(modEventBus);
        SulfurRegistry.SULFURS.register(modEventBus);
        NiterRegistry.NITERS.register(modEventBus);
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
        ConditionRegistry.CONDITION_SERIALIZERS.register(modEventBus);
        RecipeResultRegistry.RECIPE_RESULT_TYPES.register(modEventBus);
        RecipeDisplayRegistry.RECIPE_DISPLAYS.register(modEventBus);
        DataComponentRegistry.DATA_COMPONENTS.register(modEventBus);
        GameTestRegistry.TEST_FUNCTIONS.register(modEventBus);

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onServerSetup);
        modEventBus.addListener(Networking::register);

        modEventBus.addListener(TheurgyDataGenerators::onGatherData);

        modEventBus.addListener(TheurgyRegistries::onRegisterRegistries);
        modEventBus.addListener(SulfurRegistry::onBuildCreativeModTabs);
        modEventBus.addListener(NiterRegistry::onBuildCreativeModTabs);
        modEventBus.addListener(SaltRegistry::onBuildCreativeModTabs);
        modEventBus.addListener(CapabilityRegistry::onRegisterCapabilities);
        modEventBus.addListener(GameTestRegistry::onRegisterGameTests);

        NeoForge.EVENT_BUS.addListener(TooltipHandler::onItemTooltipEvent);
        NeoForge.EVENT_BUS.addListener(Logistics::onLevelUnload);
        NeoForge.EVENT_BUS.addListener(Wires::onLevelUnload);
        NeoForge.EVENT_BUS.addListener(WireSync.get()::onChunkWatch);
        NeoForge.EVENT_BUS.addListener(WireSync.get()::onChunkUnWatch);
        NeoForge.EVENT_BUS.addListener(TheurgyRecipeManager.get()::onDatapackSync);

        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            modEventBus.addListener(ParticleRegistry::registerFactories);
            modEventBus.addListener(Client::onClientSetup);
            modEventBus.addListener(Client::onRegisterEntityRendererLayerDefinitions);
            modEventBus.addListener(Client::onRegisterEntityRenderers);
            modEventBus.addListener(TheurgySpecialModelRenderers::onRegisterSpecialModelRenderers);
            modEventBus.addListener(Client::onRegisterFluidModels);
            modEventBus.addListener(Client::onRegisterItemColors);
            modEventBus.addListener(Client::onRegisterBlockColors);
            modEventBus.addListener(Client::onRegisterGuiOverlays);
            modEventBus.addListener(Client::onRegisterClientTooltipComponentFactories);
            modEventBus.addListener(BlockOverlays::onTextureAtlasStitched);
            modEventBus.addListener(ParticleSprites::onTextureAtlasStitched);
            modEventBus.addListener(KeyMappingsRegistry::onRegisterKeyMappings);
            modEventBus.addListener(Client::onRegisterItemProperties);
            NeoForge.EVENT_BUS.addListener(Client::onRenderLevelStage);
            NeoForge.EVENT_BUS.addListener(Client::onClientTick);
            NeoForge.EVENT_BUS.addListener(Client::onMouseScrolling);
            NeoForge.EVENT_BUS.addListener(Client::onRightClick);
            NeoForge.EVENT_BUS.addListener(Client::onLeftClick);
            NeoForge.EVENT_BUS.addListener(Client::onLeftClickEmpty);
            NeoForge.EVENT_BUS.addListener(BlockHighlightRenderer::onRenderBlockHighlight);
            NeoForge.EVENT_BUS.addListener(KeyMappingsRegistry::onKeyInput);
            NeoForge.EVENT_BUS.addListener(KeyMappingsRegistry::onMouseInput);
            NeoForge.EVENT_BUS.addListener(TheurgyRecipeManagerClient::onRecipesReceived);
            NeoForge.EVENT_BUS.addListener(TheurgyRecipeManagerClient::onClientLogout);

            Client.registerConfigScreen(modContainer);
        }
    }

    public static Identifier loc(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(InWorldHUDRegistry::registerDefaults);
        PageLoaders.onCommonSetup(event);

        LOGGER.info("Common setup complete.");
    }

    public void onServerSetup(FMLDedicatedServerSetupEvent event) {
        LOGGER.info("Dedicated server setup complete.");
    }

    public static class Client {
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(FilterScreenStyle::register);

            registerTooltipDataProviders(event);
            PageRenderers.onClientSetup(event);

            NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> {
                ClientTicks.endClientTick(Minecraft.getInstance());
            });
            NeoForge.EVENT_BUS.addListener((RenderFrameEvent.Pre e) -> {
                ClientTicks.renderTickStart(e.getPartialTick().getGameTimeDeltaPartialTick(true));

            });
            NeoForge.EVENT_BUS.addListener((RenderFrameEvent.Post e) -> {
                ClientTicks.renderTickEnd();
            });

            LOGGER.info("Client setup complete.");
        }

        public static void registerConfigScreen(ModContainer modContainer) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }

        public static void onClientTick(ClientTickEvent.Post event) {
            if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null)
                return;

            Player player = Minecraft.getInstance().player;

            InWorldHUD.get().tick(Minecraft.getInstance());
            Outliner.get().tick();
            BlockRegistry.CALORIC_FLUX_EMITTER.get().selectionBehaviour().tick(player);
            BlockRegistry.SULFURIC_FLUX_EMITTER.get().selectionBehaviour().tick(player);
            BlockRegistry.MERCURY_FLUX_EMITTER.get().selectionBehaviour().tick(player);


            WireItem.onClientTick(player);

            HeldStackFitOutline.onClientTick(player);
        }

        public static void onRenderLevelStage(RenderLevelStageEvent.AfterTranslucentParticles event) {
            PoseStack ms = event.getPoseStack();
            ms.pushPose();

            var buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            float partialTicks = ClientTicks.getPartialTicksHandlePause();
            Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera()
                    .position();

            Outliner.get().render(ms, buffer, camera, partialTicks);

            buffer.endBatch();
            ms.popPose();

            WireRenderer.get().onRenderLevelStage(event);
        }

        public static void registerTooltipDataProviders(FMLClientSetupEvent event) {
            TooltipHandler.registerNamespaceToListenTo(MODID);

            SulfurRegistry.SULFURS.getEntries().stream().map(DeferredHolder::get).map(AlchemicalDerivativeItem.class::cast).forEach(derivative -> {
                if (derivative.provideAutomaticTooltipData) {
                    TooltipHandler.registerTooltipDataProvider(derivative, derivative::getTooltipData);
                }
            });

            NiterRegistry.NITERS.getEntries().stream().map(DeferredHolder::get).map(AlchemicalDerivativeItem.class::cast).forEach(derivative -> {
                if (derivative.provideAutomaticTooltipData) {
                    TooltipHandler.registerTooltipDataProvider(derivative, derivative::getTooltipData);
                }
            });

            SaltRegistry.SALTS.getEntries().stream().map(DeferredHolder::get).map(AlchemicalSaltItem.class::cast).forEach(salt -> {
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
            event.registerBlockEntityRenderer(BlockEntityRegistry.LOGISTICS_NEXUS.get(), LogisticsNexusRenderer::new);
            event.registerBlockEntityRenderer(BlockEntityRegistry.DIGESTION_VAT.get(), DigestionVatRenderer::new);
            event.registerBlockEntityRenderer(BlockEntityRegistry.MERCURY_CAPACITOR.get(), MercuryCapacitorRenderer::new);
        }

        public static void onRegisterItemProperties(RegisterRangeSelectItemModelPropertyEvent event) {
            event.register(Theurgy.loc("divination_distance"), DivinationDistanceProperty.MAP_CODEC);
        }

        public static void onRegisterFluidModels(RegisterFluidModelsEvent event) {
            var salAmmoniac = FluidTypeRegistry.SAL_AMMONIAC.get();
            event.register(new FluidModel.Unbaked(
                    new Material(salAmmoniac.still),
                    new Material(salAmmoniac.flowing),
                    new Material(salAmmoniac.overlay),
                    FluidTintSources.constant(salAmmoniac.tint)
            ), FluidRegistry.SAL_AMMONIAC.get(), FluidRegistry.SAL_AMMONIAC_FLOWING.get());
        }

        public static void onRegisterItemColors(RegisterColorHandlersEvent.ItemTintSources event) {
            //event.register(new DynamicFluidContainerModel.Colors(), ItemRegistry.SAL_AMMONIAC_BUCKET.get());
            event.register(Theurgy.loc("mercury_catalyst_tint"), MercuryCatalystBlock.ItemTintSource.MAP_CODEC);
            event.register(Theurgy.loc("mercury_capacitor_tint"), MercuryCapacitorBlock.ItemTintSource.MAP_CODEC);
        }

        public static void onRegisterBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
            event.register(List.of(new MercuryCatalystBlock.BlockTintSource()), BlockRegistry.MERCURY_CATALYST.get());
            event.register(List.of(new MercuryCapacitorBlock.BlockTintSource()), BlockRegistry.MERCURY_CAPACITOR.get());
        }

        public static void onRegisterGuiOverlays(RegisterGuiLayersEvent event) {
            event.registerAbove(VanillaGuiLayers.HOTBAR, Theurgy.loc("in_world_hud"), InWorldHUD.get());
            event.registerAbove(VanillaGuiLayers.HOTBAR, Theurgy.loc("item_hud"), ItemHUD.get());
        }

        public static void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(ItemStacksTooltip.class, ClientItemStacksTooltip::new);
        }

        public static void onMouseScrolling(InputEvent.MouseScrollingEvent event) {
            var minecraft = Minecraft.getInstance();
            if (minecraft.player != null && minecraft.player.isShiftKeyDown()) {
                double delta = event.getScrollDeltaY();
                var stack = minecraft.player.getMainHandItem();

                if (delta != 0 && stack.getItem() instanceof HandlesOnScroll scrollableItem) {
                    int shift = ScrollHelper.scroll(delta);
                    if (shift != 0) {
                        scrollableItem.onScroll(minecraft.player, stack, shift);
                    }
                    event.setCanceled(true);
                }
            }
        }

        public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
            var player = event.getEntity();
            
            // If player is holding shift, don't interfere with normal block placement
            if (player != null && player.isShiftKeyDown()) {
                return;
            }

            if (BlockRegistry.CALORIC_FLUX_EMITTER.get().selectionBehaviour().onRightClickBlock(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
                return;
            }

            if (BlockRegistry.SULFURIC_FLUX_EMITTER.get().selectionBehaviour().onRightClickBlock(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
                return;
            }

            if (BlockRegistry.MERCURY_FLUX_EMITTER.get().selectionBehaviour().onRightClickBlock(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }


        public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
            if (BlockRegistry.CALORIC_FLUX_EMITTER.get().selectionBehaviour().onLeftClickBlock(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace())) {
                event.setCanceled(true);
                return;
            }

            if (BlockRegistry.SULFURIC_FLUX_EMITTER.get().selectionBehaviour().onLeftClickBlock(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace())) {
                event.setCanceled(true);
                return;
            }

            if (BlockRegistry.MERCURY_FLUX_EMITTER.get().selectionBehaviour().onLeftClickBlock(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace())) {
                event.setCanceled(true);
                return;
            }


            //filter for "abort" to avoid constant calls while held down
            if (event.getAction() == PlayerInteractEvent.LeftClickBlock.Action.ABORT &&
                    event.getItemStack().getItem() instanceof HandlesOnLeftClick leftClickableItem) {
                if (leftClickableItem.onLeftClickBlock(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace())) {
                    event.setCanceled(true);
                }
            }
        }

        public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
            if (event.getItemStack().getItem() instanceof HandlesOnLeftClick leftClickableItem) {
                leftClickableItem.onLeftClickEmpty(event.getLevel(), event.getEntity(), event.getHand());

                //this event is only called on client, so we send it to the server if we have a left clickable item
                Networking.sendToServer(new MessageOnLeftClickEmpty(event.getHand()));
            }
        }
    }
}
