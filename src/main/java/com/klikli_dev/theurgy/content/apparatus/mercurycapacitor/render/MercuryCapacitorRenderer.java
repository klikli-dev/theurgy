// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.render;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.MercuryCapacitorBlock;
import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.MercuryCapacitorBlockEntity;
import com.klikli_dev.theurgy.content.render.ParticleSprites;
import com.klikli_dev.theurgy.content.render.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;


public class MercuryCapacitorRenderer implements BlockEntityRenderer<MercuryCapacitorBlockEntity, MercuryCapacitorRenderer.MercuryCapacitorRenderState> {

    private static final Identifier PARTICLE_ATLAS = TextureAtlas.LOCATION_PARTICLES;

    public MercuryCapacitorRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    @Override
    public MercuryCapacitorRenderState createRenderState() {
        return new MercuryCapacitorRenderState();
    }

    @Override
    public void extractRenderState(
            MercuryCapacitorBlockEntity blockEntity,
            MercuryCapacitorRenderState state,
            float partialTick,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, cameraPosition, breakProgress);

        float fillLevel = blockEntity.mercuryFluxStorage.getEnergyStored() / (float) blockEntity.mercuryFluxStorage.getMaxEnergyStored();
        state.hasEnergy = blockEntity.mercuryFluxStorage.getEnergyStored() > 0;
        state.particleColor = MercuryCapacitorBlock.getParticleColorFromFillLevel(fillLevel);
        state.cameraPosition = cameraPosition;
        state.blockPos = blockEntity.getBlockPos();
        state.gameTime = blockEntity.getLevel().getGameTime() + (long) partialTick;
    }

    @Override
    public void submit(MercuryCapacitorRenderState state, PoseStack pPoseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.hasEnergy || state.cameraPosition == null || ParticleSprites.GLOW == null) {
            return; // Don't render if empty, no camera position, or sprite not loaded
        }

        pPoseStack.pushPose();

        // Animation parameters
        float time = state.gameTime;
        float rotationSpeed = 0.5f; // radians per tick
        float wobbleAmount = 0.1f; // max tilt angle in radians (~5.7 degrees)
        float wobbleSpeed1 = 0.7f;
        float wobbleSpeed2 = 1.1f;

        // Calculate rotation angle
        float rotation = time * rotationSpeed * ((float) Math.PI * 2f / 20f);

        // Calculate wobble angles
        float wobbleX = Mth.sin(time * wobbleSpeed1 * ((float) Math.PI * 2f / 60f)) * wobbleAmount;
        float wobbleY = Mth.sin(time * wobbleSpeed2 * ((float) Math.PI * 2f / 60f)) * wobbleAmount;

        // Center the quad at the block center (local coordinates 0-1)
        float halfSize = 0.25f;
        Vec3 localCenter = new Vec3(0.5, 0.5, 0.5);
        
        // Center in world coordinates for camera direction calculation
        Vec3 worldCenter = new Vec3(
                state.blockPos.getX() + 0.5,
                state.blockPos.getY() + 0.5,
                state.blockPos.getZ() + 0.5
        );

        // Get direction from quad center to camera
        Vec3 cameraDirection = state.cameraPosition.subtract(worldCenter).normalize();

        // Calculate the right and up vectors for the billboard
        // Default up is Y+, default right is X+
        Vec3 worldUp = new Vec3(0, 1, 0);
        Vec3 worldRight = new Vec3(1, 0, 0);

        // If camera is looking from above/below, use different reference vectors
        if (Math.abs(cameraDirection.y) > 0.99f) {
            worldRight = new Vec3(1, 0, 0);
            worldUp = cameraDirection.y > 0 ? new Vec3(0, 0, -1) : new Vec3(0, 0, 1);
        }

        // Project vectors onto a plane perpendicular to camera direction
        Vec3 right = worldRight.subtract(cameraDirection.scale(cameraDirection.dot(worldRight))).normalize();
        Vec3 up = worldUp.subtract(cameraDirection.scale(cameraDirection.dot(worldUp))).normalize();

        // If right is zero, recalculate
        if (right.lengthSqr() < 0.001f) {
            right = worldUp.cross(cameraDirection).normalize();
        }

        // Calculate the actual up as perpendicular to both camera direction and right
        up = cameraDirection.cross(right).normalize();

        // Apply rotation around the camera direction (the normal)
        float cosR = Mth.cos(rotation);
        float sinR = Mth.sin(rotation);
        Vec3 rotatedRight = right.scale(cosR).add(up.scale(sinR));
        Vec3 rotatedUp = up.scale(cosR).subtract(right.scale(sinR));

        // Apply wobble (tilt) - rotate around right axis for wobbleX, around up axis for wobbleY
        float cosWx = Mth.cos(wobbleX);
        float sinWx = Mth.sin(wobbleX);
        float cosWy = Mth.cos(wobbleY);
        float sinWy = Mth.sin(wobbleY);

        // Apply wobbleX: rotate up around right
        Vec3 wobbledUp = up.scale(cosWx).add(cameraDirection.scale(sinWx));
        Vec3 wobbledNormalX = cameraDirection.scale(cosWx).subtract(up.scale(sinWx));
        // Apply wobbleY: rotate right around up (on the wobbled plane)
        Vec3 wobbledRight = rotatedRight.scale(cosWy).add(wobbledUp.scale(sinWy));

        // Calculate final normal as perpendicular to wobbled vectors
        Vec3 finalNormal = wobbledRight.cross(wobbledUp).normalize();

        // Calculate quad vertices relative to center
        Vec3 p1 = localCenter.add(wobbledRight.scale(-halfSize)).add(wobbledUp.scale(-halfSize)); // Bottom-left
        Vec3 p2 = localCenter.add(wobbledRight.scale(halfSize)).add(wobbledUp.scale(-halfSize));   // Bottom-right
        Vec3 p3 = localCenter.add(wobbledRight.scale(halfSize)).add(wobbledUp.scale(halfSize));   // Top-right
        Vec3 p4 = localCenter.add(wobbledRight.scale(-halfSize)).add(wobbledUp.scale(halfSize));  // Top-left

        // The normal points toward the camera (wobbled)
        Vec3 normal = finalNormal;

        // Full brightness for glow effect
        int light = LightCoordsUtil.FULL_BRIGHT;

        // Get sprite UV coordinates
        float u0 = ParticleSprites.GLOW.getU0();
        float u1 = ParticleSprites.GLOW.getU1();
        float v0 = ParticleSprites.GLOW.getV0();
        float v1 = ParticleSprites.GLOW.getV1();

        submitNodeCollector.submitCustomGeometry(pPoseStack, RenderTypes.particleTranslucent(PARTICLE_ATLAS), (pose, builder) -> {
            putVertex(builder, pose, p1, state.particleColor, u0, v1, light, normal);
            putVertex(builder, pose, p2, state.particleColor, u1, v1, light, normal);
            putVertex(builder, pose, p3, state.particleColor, u1, v0, light, normal);
            putVertex(builder, pose, p4, state.particleColor, u0, v0, light, normal);
        });

        pPoseStack.popPose();
    }

    private void putVertex(VertexConsumer builder, PoseStack.Pose pose, Vec3 pos, int color, float u, float v, int light, Vec3 normal) {
        int a = (int) ((color >> 24 & 0xff) * 0.75f);
        int r = color >> 16 & 0xff;
        int g = color >> 8 & 0xff;
        int b = color & 0xff;

        builder.addVertex(pose.pose(), (float) pos.x, (float) pos.y, (float) pos.z)
                .setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, (float) normal.x, (float) normal.y, (float) normal.z);
    }

    public static class MercuryCapacitorRenderState extends BlockEntityRenderState {
        public boolean hasEnergy;
        public int particleColor;
        public Vec3 cameraPosition;
        public BlockPos blockPos;
        public long gameTime;
    }
}
