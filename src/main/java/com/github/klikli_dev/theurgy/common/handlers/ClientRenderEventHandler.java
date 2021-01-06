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
import com.github.klikli_dev.theurgy.common.theurgy.IEssentiaInformationProvider;
import com.github.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Theurgy.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientRenderEventHandler {

    //region Fields
    public static boolean displayChunkEssentia = false;

    public static Map<Item, Integer> chunkEssentia = new HashMap<>();
    //endregion Fields

    //region Static Methods
    @OnlyIn(Dist.CLIENT)
    public static void renderEssentiaInfo(RenderGameOverlayEvent.Post event, PlayerEntity player, World world) {
        MainWindow window = Minecraft.getInstance().getMainWindow();
        int width = window.getScaledWidth();
        int height = window.getScaledHeight();

        int centerX = width / 2;
        int centerY = height / 2 - height / 4; //start rendering slightly above center

        List<ITextComponent> tooltip = new ArrayList<>();

        if (displayChunkEssentia) {
            if (player.getHeldItemMainhand().getItem() != ItemRegistry.ESSENTIA_GAUGE.get()) {
                //if item was switched by scrolling or dropping, disable essentia display
                displayChunkEssentia = false;
            }

            //Render golden bold header
            tooltip.add(new TranslationTextComponent(
                    "tooltip." + Theurgy.MODID + ".essentia_information.chunk.heading")
                                .mergeStyle(TextFormatting.BOLD)
                                .mergeStyle(TextFormatting.GOLD));

            if (chunkEssentia.size() > 0) {
                //Render each essentia type
                chunkEssentia.forEach((essentia, amount) -> {
                    tooltip.add(new TranslationTextComponent(
                            "tooltip." + Theurgy.MODID + ".essentia_information.chunk.content",
                            new TranslationTextComponent(essentia.getTranslationKey()), amount));
                });
            }
            else {
                tooltip.add(new TranslationTextComponent(
                        "tooltip." + Theurgy.MODID + ".essentia_information.chunk.no_content"));
            }
        }

        //Gather essentia info for looked at item
        if (!player.getHeldItemMainhand().isEmpty()) {
            if (player.getHeldItemMainhand().getItem() == ItemRegistry.ESSENTIA_GAUGE.get()) {

                //raytrace for looked at block
                RayTraceResult lookingAt = Minecraft.getInstance().objectMouseOver;
                if (lookingAt != null && lookingAt.getType() == RayTraceResult.Type.BLOCK) {
                    BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) lookingAt;
                    BlockState state = world.getBlockState(blockRayTraceResult.getPos());

                    //get essentia tooltip
                    if (state.getBlock() instanceof IEssentiaInformationProvider) {
                        ((IEssentiaInformationProvider) state.getBlock())
                                .getEssentiaInformation(world, blockRayTraceResult.getPos(), state, tooltip);
                    }

                    //render essentia tooltip
                }
            }
        }

        //If we have any tooltips, render them here
        if (tooltip.size() > 0) {
            MatrixStack matrixStack = event.getMatrixStack();
            FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
            for (int i = 0; i < tooltip.size(); i++) {

                IRenderTypeBuffer.Impl renderType = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());

                ITextComponent component = tooltip.get(i);
                fontRenderer.func_238416_a_(LanguageMap.getInstance().func_241870_a(component),
                        centerX - fontRenderer.getStringPropertyWidth(component) / 2.0f,
                        centerY + 40 + 11 * i, -1, true, matrixStack.getLast().getMatrix(), renderType, false, 0,
                        15728880);

                renderType.finish();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
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
