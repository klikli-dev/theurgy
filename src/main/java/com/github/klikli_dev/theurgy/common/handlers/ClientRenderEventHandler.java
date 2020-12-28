package com.github.klikli_dev.theurgy.common.handlers;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.theurgy.IEssentiaInformationProvider;
import com.github.klikli_dev.theurgy.registry.TagRegistry;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Theurgy.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientRenderEventHandler {

    //region Static Methods
    public static void renderEssentiaInfo(RenderGameOverlayEvent.Post event, PlayerEntity player, World world) {
        MainWindow window = Minecraft.getInstance().getMainWindow();
        int width = window.getScaledWidth();
        int height = window.getScaledHeight();

        int centerX = width / 2;
        int centerY = height / 2;

        if (!player.getHeldItemMainhand().isEmpty()) {
            //TODO: check for essentia gauge item here
            if (TagRegistry.RODS_WOODEN.contains(player.getHeldItemMainhand().getItem())) {

                //raytrace for looked at block
                RayTraceResult lookingAt = Minecraft.getInstance().objectMouseOver;
                if (lookingAt != null && lookingAt.getType() == RayTraceResult.Type.BLOCK) {
                    BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) lookingAt;
                    BlockState state = world.getBlockState(blockRayTraceResult.getPos());

                    //get essentia tooltip
                    List<ITextComponent> text = new ArrayList<>();
                    if (state.getBlock() instanceof IEssentiaInformationProvider) {
                        text = ((IEssentiaInformationProvider) state.getBlock())
                                            .getEssentiaInformation(world, blockRayTraceResult.getPos(), state, text);
                    }

                    //render essentia tooltip
                    MatrixStack matrixStack = event.getMatrixStack();
                    FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
                    for (int i = 0; i < text.size(); i++) {

                        //move rendering down by font size 11
//                        String formattedText = text.get(i).getString();
//
//                        fontRenderer.drawStringWithShadow(
//                                matrixStack,
//                                formattedText,
//                                centerX - fontRenderer.getStringWidth(formattedText) / 2.0f,
//                                centerY + 40 + 11 * i, 0xFFFFFF
//                        );

                        IRenderTypeBuffer.Impl renderType = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());

                        ITextComponent component = text.get(i);
                                fontRenderer.func_238416_a_(LanguageMap.getInstance().func_241870_a(component),
                                        centerX - fontRenderer.getStringPropertyWidth(component) / 2.0f,
                                        centerY + 40 + 11 * i, -1, true, matrixStack.getLast().getMatrix(), renderType, false, 0, 15728880);

                        renderType.finish();
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            PlayerEntity player = Minecraft.getInstance().player;
            World world = player.getEntityWorld();
            renderEssentiaInfo(event, player, world);
        }
    }
    //endregion Static Methods
}
