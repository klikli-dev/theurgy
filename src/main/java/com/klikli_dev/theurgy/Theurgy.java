// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy;

import com.klikli_dev.modonomicon.client.render.page.PageRendererRegistry;
import com.klikli_dev.theurgy.config.ClientConfig;
import com.klikli_dev.theurgy.config.CommonConfig;
import com.klikli_dev.theurgy.config.ServerConfig;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.render.CalcinationOvenBEWLR;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.render.CalcinationOvenRenderer;
import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatBEWLR;
import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatRenderer;
import com.klikli_dev.theurgy.content.apparatus.distiller.render.DistillerBEWLR;
import com.klikli_dev.theurgy.content.apparatus.distiller.render.DistillerRenderer;
import com.klikli_dev.theurgy.content.apparatus.incubator.render.*;
import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.render.LiquefactionCauldronRenderer;
import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlock;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.render.SalAmmoniacAccumulatorBEWLR;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.render.SalAmmoniacAccumulatorRenderer;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.render.SalAmmoniacTankBEWLR;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.render.SalAmmoniacTankRenderer;
import com.klikli_dev.theurgy.content.item.HandlesOnLeftClick;
import com.klikli_dev.theurgy.content.item.HandlesOnScroll;
import com.klikli_dev.theurgy.content.item.divinationrod.DivinationRodItem;
import com.klikli_dev.theurgy.content.item.filter.AttributeFilterScreen;
import com.klikli_dev.theurgy.content.item.filter.ListFilterScreen;
import com.klikli_dev.theurgy.content.item.salt.AlchemicalSaltItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalDerivativeItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.sulfur.render.AlchemicalDerivativeBEWLR;
import com.klikli_dev.theurgy.content.render.*;
import com.klikli_dev.theurgy.content.render.itemhud.ItemHUD;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
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
import com.klikli_dev.theurgy.util.ScrollHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
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
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;


@Mod(Theurgy.MODID)
public class Theurgy {
    public static final String MODID = "theurgy";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Theurgy INSTANCE;

    public Theurgy(IEventBus modEventBus, ModContainer modContainer) {
        INSTANCE = this;

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
        DataComponentRegistry.DATA_COMPONENTS.register(modEventBus);
        MenuTypeRegistry.MENU_TYPES.register(modEventBus);

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onServerSetup);
        modEventBus.addListener(Networking::register);

        modEventBus.addListener(TheurgyDataGenerators::onGatherData);

        modEventBus.addListener(TheurgyRegistries::onRegisterRegistries);
        modEventBus.addListener(SulfurRegistry::onBuildCreativeModTabs);
        modEventBus.addListener(NiterRegistry::onBuildCreativeModTabs);
        modEventBus.addListener(SaltRegistry::onBuildCreativeModTabs);
        modEventBus.addListener(CapabilityRegistry::onRegisterCapabilities);

        NeoForge.EVENT_BUS.addListener(TooltipHandler::onItemTooltipEvent);
        NeoForge.EVENT_BUS.addListener(Logistics::onLevelUnload);
        NeoForge.EVENT_BUS.addListener(Wires::onLevelUnload);
        NeoForge.EVENT_BUS.addListener(WireSync.get()::onChunkWatch);
        NeoForge.EVENT_BUS.addListener(WireSync.get()::onChunkUnWatch);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(ParticleRegistry::registerFactories);
            modEventBus.addListener(Client::onClientSetup);
            modEventBus.addListener(Client::onRegisterEntityRendererLayerDefinitions);
            modEventBus.addListener(Client::onRegisterEntityRenderers);
            modEventBus.addListener(Client::onRegisterClientExtensions);
            modEventBus.addListener(Client::onRegisterItemColors);
            modEventBus.addListener(Client::onRegisterBlockColors);
            modEventBus.addListener(Client::onRegisterGuiOverlays);
            modEventBus.addListener(Client::onRegisterMenuScreens);
            modEventBus.addListener(BlockOverlays::onTextureAtlasStitched);
            modEventBus.addListener(KeyMappingsRegistry::onRegisterKeyMappings);
            NeoForge.EVENT_BUS.addListener(Client::onRenderLevelStage);
            NeoForge.EVENT_BUS.addListener(Client::onClientTick);
            NeoForge.EVENT_BUS.addListener(Client::onRecipesUpdated);
            NeoForge.EVENT_BUS.addListener(Client::onMouseScrolling);
            NeoForge.EVENT_BUS.addListener(Client::onRightClick);
            NeoForge.EVENT_BUS.addListener(Client::onLeftClick);
            NeoForge.EVENT_BUS.addListener(Client::onLeftClickEmpty);
            NeoForge.EVENT_BUS.addListener(BlockHighlightRenderer::onRenderBlockHighlight);
            NeoForge.EVENT_BUS.addListener(KeyMappingsRegistry::onKeyInput);
            NeoForge.EVENT_BUS.addListener(KeyMappingsRegistry::onMouseInput);

            Client.registerConfigScreen(modContainer);
        }
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
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

            WireRenderer.get().onRenderLevelStage(event);
        }

        public static void onRecipesUpdated(RecipesUpdatedEvent event) {
            //now disable rendering of sulfurs that have no recipe in modonomicon -> otherwise we see "no source" sulfurs in tag recipes
            //See also JeiPlugin.registerRecipes
            var liquefactionRecipes = event.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get());

            //noinspection ConstantValue
            SulfurRegistry.SULFURS.getEntries().stream()
                    .map(DeferredHolder::get)
                    .map(AlchemicalSulfurItem.class::cast)
                    .filter(sulfur -> liquefactionRecipes.stream().noneMatch(r -> r.value().getResultItem(RegistryAccess.EMPTY) != null && r.value().getResultItem(RegistryAccess.EMPTY).getItem() == sulfur)).map(ItemStack::new).forEach(PageRendererRegistry::registerItemStackNotToRender);
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

        public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
            var alchemicalDerivativeItemExtension = new IClientItemExtensions() {
                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return AlchemicalDerivativeBEWLR.get();
                }
            };

            SulfurRegistry.SULFURS.getEntries().forEach(sulfur -> {
                event.registerItem(alchemicalDerivativeItemExtension, sulfur.get());
            });

            NiterRegistry.NITERS.getEntries().forEach(niter -> {
                event.registerItem(alchemicalDerivativeItemExtension, niter.get());
            });


            event.registerItem(new IClientItemExtensions() {
                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return IncubatorSaltVesselBEWLR.get();
                }
            }, ItemRegistry.INCUBATOR_SALT_VESSEL.get());

            event.registerItem(new IClientItemExtensions() {
                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return IncubatorMercuryVesselBEWLR.get();
                }
            }, ItemRegistry.INCUBATOR_MERCURY_VESSEL.get());

            event.registerItem(new IClientItemExtensions() {
                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return IncubatorSulfurVesselBEWLR.get();
                }
            }, ItemRegistry.INCUBATOR_SULFUR_VESSEL.get());


            event.registerItem(new IClientItemExtensions() {
                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return SalAmmoniacTankBEWLR.get();
                }
            }, ItemRegistry.SAL_AMMONIAC_TANK.get());

            event.registerItem(new IClientItemExtensions() {
                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return SalAmmoniacAccumulatorBEWLR.get();
                }
            }, ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get());

            event.registerItem(new IClientItemExtensions() {

                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return DigestionVatBEWLR.get();
                }
            }, ItemRegistry.DIGESTION_VAT.get());

            event.registerItem(new IClientItemExtensions() {
                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return CalcinationOvenBEWLR.get();
                }
            }, ItemRegistry.CALCINATION_OVEN.get());

            event.registerItem(new IClientItemExtensions() {
                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return DistillerBEWLR.get();
                }
            }, ItemRegistry.DISTILLER.get());


            event.registerFluidType(new IClientFluidTypeExtensions() {
                @Override
                public @NotNull ResourceLocation getStillTexture() {
                    return FluidTypeRegistry.SAL_AMMONIAC.get().still;
                }

                @Override
                public @NotNull ResourceLocation getFlowingTexture() {
                    return FluidTypeRegistry.SAL_AMMONIAC.get().flowing;
                }

                @Override
                public ResourceLocation getOverlayTexture() {
                    return FluidTypeRegistry.SAL_AMMONIAC.get().overlay;
                }

                @Override
                public int getTintColor() {
                    return FluidTypeRegistry.SAL_AMMONIAC.get().tint;
                }
            }, FluidTypeRegistry.SAL_AMMONIAC.get());
        }

        public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
            event.register(new DynamicFluidContainerModel.Colors(), ItemRegistry.SAL_AMMONIAC_BUCKET.get());
            event.register(MercuryCatalystBlock::getItemColor, ItemRegistry.MERCURY_CATALYST.get());
        }

        public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
            event.register(MercuryCatalystBlock::getBlockColor, BlockRegistry.MERCURY_CATALYST.get());
        }

        public static void onRegisterGuiOverlays(RegisterGuiLayersEvent event) {
            event.registerAbove(VanillaGuiLayers.HOTBAR, Theurgy.loc("item_hud"), ItemHUD.get());
        }

        public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
            event.register(MenuTypeRegistry.LIST_FILTER.get(), ListFilterScreen::new);
            event.register(MenuTypeRegistry.ATTRIBUTE_FILTER.get(), AttributeFilterScreen::new);
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
                return;
            }

            if (BlockRegistry.SULFURIC_FLUX_EMITTER.get().selectionBehaviour().onLeftClickBlock(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace())) {
                event.setCanceled(true);
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
