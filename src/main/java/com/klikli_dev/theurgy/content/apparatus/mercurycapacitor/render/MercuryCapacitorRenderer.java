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
    }

    @Override
    public void submit(MercuryCapacitorRenderState state, PoseStack pPoseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.hasEnergy || state.cameraPosition == null || ParticleSprites.GLOW == null) {
            return; // Don't render if empty, no camera position, or sprite not loaded
        }

        pPoseStack.pushPose();

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

        // Calculate quad vertices relative to center
        Vec3 p1 = localCenter.add(right.scale(-halfSize)).add(up.scale(-halfSize)); // Bottom-left
        Vec3 p2 = localCenter.add(right.scale(halfSize)).add(up.scale(-halfSize));   // Bottom-right
        Vec3 p3 = localCenter.add(right.scale(halfSize)).add(up.scale(halfSize));   // Top-right
        Vec3 p4 = localCenter.add(right.scale(-halfSize)).add(up.scale(halfSize));  // Top-left

        // The normal points toward the camera
        Vec3 normal = cameraDirection;

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
        int a = color >> 24 & 0xff;
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
    }
}
