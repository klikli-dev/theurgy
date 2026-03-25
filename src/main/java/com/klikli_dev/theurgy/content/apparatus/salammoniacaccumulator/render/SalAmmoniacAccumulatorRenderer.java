// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.render;

import com.geckolib.renderer.GeoBlockRenderer;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.SalAmmoniacAccumulatorBlockEntity;
import com.klikli_dev.theurgy.content.render.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;


public class SalAmmoniacAccumulatorRenderer extends GeoBlockRenderer<SalAmmoniacAccumulatorBlockEntity, SalAmmoniacAccumulatorRenderer.RenderState> {

    public SalAmmoniacAccumulatorRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext, new SalAmmoniacAccumulatorModel());
    }

    private static void putVertex(VertexConsumer builder, PoseStack.Pose pose, float x, float y, float z, int color, float u, float v, Direction face, int light) {
        Vec3i normal = face.getUnitVec3i();
        int a = color >> 24 & 0xff;
        int r = color >> 16 & 0xff;
        int g = color >> 8 & 0xff;
        int b = color & 0xff;

        builder.addVertex(pose.pose(), x, y, z)
                .setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, normal.getX(), normal.getY(), normal.getZ());
    }

    /**
     * See com.simibubi.create.content.fluids.tank.FluidTankRenderer
     */

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(
            SalAmmoniacAccumulatorBlockEntity blockEntity,
            RenderState state,
            float partialTick,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        super.extractRenderState(blockEntity, state, partialTick, cameraPosition, breakProgress);

        if (blockEntity.waterTank.isEmpty()) {
            state.empty = true;
            return;
        }

        state.empty = false;

        var fluidStack = blockEntity.waterTank.getFluid();
        var fluid = fluidStack.getFluid();
        var fluidType = fluid.getFluidType();
        var fluidHeight = fluidStack.getAmount() / (float) blockEntity.waterTank.getCapacity();

        float blockHeight = 12 / 16f - 2 / 128f;
        float capHeight = 0 / 4f;
        float minPuddleHeight = 1 / 16f;
        float totalHeight = blockHeight - 2 * capHeight - minPuddleHeight;
        float clampedLevel = Mth.clamp(fluidHeight * totalHeight, 0, totalHeight);
        int blockLightIn = (state.lightCoords >> 4) & 0xF;
        int luminosity = Math.max(blockLightIn, fluidType.getLightLevel(fluidStack));

        state.color = 0xFFFFFFFF; // SAL_AMMONIAC uses water base, default no-tint (IClientFluidTypeExtensions.getTintColor removed)
        state.fluidLight = (state.lightCoords & 0xF00000) | luminosity << 4;
        state.surfaceY = capHeight + minPuddleHeight + clampedLevel;
    }

    @Override
    public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        super.submit(state, poseStack, submitNodeCollector, cameraRenderState);

        if (state.empty) {
            return;
        }

        float blockWidth = 1;
        float tankHullWidth = 3 / 16f + 2 / 128f;
        float xMin = tankHullWidth;
        float xMax = xMin + blockWidth - 2 * tankHullWidth;
        float zMin = tankHullWidth;
        float zMax = zMin + blockWidth - 2 * tankHullWidth;

        poseStack.pushPose();
        poseStack.translate(-0.5f, 0, -0.5f);

        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.fluid(), (pose, builder) -> {
            putVertex(builder, pose, xMin, state.surfaceY, zMin, state.color, 0.0f, 0.0f, Direction.UP, state.fluidLight);
            putVertex(builder, pose, xMin, state.surfaceY, zMax, state.color, 0.0f, 1.0f, Direction.UP, state.fluidLight);
            putVertex(builder, pose, xMax, state.surfaceY, zMax, state.color, 1.0f, 1.0f, Direction.UP, state.fluidLight);
            putVertex(builder, pose, xMax, state.surfaceY, zMin, state.color, 1.0f, 0.0f, Direction.UP, state.fluidLight);
        });

        poseStack.popPose();
    }

    public static class RenderState extends BlockEntityRenderState {
        public boolean empty = true;
        public int color;
        public int fluidLight;
        public float surfaceY;
    }
}
