// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray.render;

import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationTargetPedestalBlockEntity;
import com.klikli_dev.theurgy.content.render.ClientTicks;
import com.klikli_dev.theurgy.content.render.Color;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class ReformationTargetPedestalRenderer implements BlockEntityRenderer<ReformationTargetPedestalBlockEntity> {

    @SuppressWarnings("deprecation")
    public static final Material GLOW = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("theurgy", "particle/particle_glow"));

    public ReformationTargetPedestalRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public static void renderQuad(PoseStack poseStack, VertexConsumer builder, float x, float y, int width, int height, float r, float g, float b, int light, int overlay) {
        var matrix = poseStack.last().pose();
        var normal = poseStack.last().normal();

        builder.vertex(matrix, x, y, 0)
                .color(r, g, b, 1F)
                .uv(0, 0)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(normal, 0, 1, 0)
                .endVertex();

        builder.vertex(matrix, x + width, y, 0)
                .color(r, g, b, 1F)
                .uv(1, 0)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(normal, 0, 1, 0)
                .endVertex();

        builder.vertex(matrix, x + width, y + height, 0)
                .color(r, g, b, 1F)
                .uv(1, 1)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(normal, 0, 1, 0)
                .endVertex();

        builder.vertex(matrix, x, y + height, 0)
                .color(r, g, b, 1F)
                .uv(0, 1)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(normal, 0, 1, 0)
                .endVertex();
    }

    @Override
    public void render(ReformationTargetPedestalBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        var color = new Color(0xFFFFFF);

        float r = color.getRed();
        float g = color.getGreen();
        float b = color.getBlue();
        var angle = -((ClientTicks.ticks + pPartialTick) % 360);

        pPoseStack.pushPose();
        pPoseStack.translate(0, 1, 0);

        pPoseStack.translate(0.5, 0.3, 0.5);
        pPoseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().camera.rotation()); //face camera
        pPoseStack.scale(0.016F, 0.016F, 0.016F);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(angle)); //rotate around Z axis (like a windmill)
        pPoseStack.translate(-16, -16, 0);

        renderQuad(pPoseStack, GLOW.buffer(pBufferSource, RenderType::entityTranslucent), 0, 0, 32, 32, r, g, b, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);


        pPoseStack.popPose();
    }
}
