// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.render;

import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlockEntity;
import com.klikli_dev.theurgy.content.render.FluidRenderer;
import com.klikli_dev.theurgy.content.render.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
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


public class LiquefactionCauldronRenderer implements BlockEntityRenderer<LiquefactionCauldronBlockEntity, LiquefactionCauldronRenderer.LiquefactionCauldronRenderState> {

    public LiquefactionCauldronRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    private static void putSideFace(
            VertexConsumer builder,
            PoseStack.Pose pose,
            Direction face,
            float horizontalMin,
            float horizontalMax,
            float yMin,
            float yMax,
            float depth,
            int color,
            float u0,
            float u1,
            float vTop,
            float vBottom,
            int light
    ) {
        switch (face) {
            case NORTH -> {
                putVertex(builder, pose, horizontalMin, yMin, depth, color, u0, vBottom, face, light);
                putVertex(builder, pose, horizontalMin, yMax, depth, color, u0, vTop, face, light);
                putVertex(builder, pose, horizontalMax, yMax, depth, color, u1, vTop, face, light);
                putVertex(builder, pose, horizontalMax, yMin, depth, color, u1, vBottom, face, light);
            }
            case SOUTH -> {
                putVertex(builder, pose, horizontalMax, yMin, depth, color, u0, vBottom, face, light);
                putVertex(builder, pose, horizontalMax, yMax, depth, color, u0, vTop, face, light);
                putVertex(builder, pose, horizontalMin, yMax, depth, color, u1, vTop, face, light);
                putVertex(builder, pose, horizontalMin, yMin, depth, color, u1, vBottom, face, light);
            }
            case WEST -> {
                putVertex(builder, pose, depth, yMin, horizontalMax, color, u0, vBottom, face, light);
                putVertex(builder, pose, depth, yMax, horizontalMax, color, u0, vTop, face, light);
                putVertex(builder, pose, depth, yMax, horizontalMin, color, u1, vTop, face, light);
                putVertex(builder, pose, depth, yMin, horizontalMin, color, u1, vBottom, face, light);
            }
            case EAST -> {
                putVertex(builder, pose, depth, yMin, horizontalMin, color, u0, vBottom, face, light);
                putVertex(builder, pose, depth, yMax, horizontalMin, color, u0, vTop, face, light);
                putVertex(builder, pose, depth, yMax, horizontalMax, color, u1, vTop, face, light);
                putVertex(builder, pose, depth, yMin, horizontalMax, color, u1, vBottom, face, light);
            }
            default -> {
            }
        }
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
     * Based on com.simibubi.create.content.contraptions.fluids.tank.FluidTankR
     */
    @Override
    public LiquefactionCauldronRenderState createRenderState() {
        return new LiquefactionCauldronRenderState();
    }

    @Override
    public void extractRenderState(
            LiquefactionCauldronBlockEntity blockEntity,
            LiquefactionCauldronRenderState state,
            float partialTick,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, cameraPosition, breakProgress);
        if (blockEntity.storageBehaviour.solventTank.isEmpty()) {
            state.empty = true;
            return;
        }

        state.empty = false;

        var fluidStack = blockEntity.storageBehaviour.solventTank.getFluid();
        var fluid = fluidStack.getFluid();
        var fluidType = fluid.getFluidType();
        var fillRatio = fluidStack.getAmount() / (float) blockEntity.storageBehaviour.solventTank.getCapacity();
        var sprite = FluidRenderer.getFluidTexture(fluidStack, FluidRenderer.FluidTextureType.STILL);

        int blockLightIn = (state.lightCoords >> 4) & 0xF;
        int luminosity = Math.max(blockLightIn, fluidType.getLightLevel(fluidStack));

        state.color = blockEntity.getLevel() instanceof ClientLevel clientLevel
                ? FluidRenderer.getFluidColor(fluidStack, clientLevel, blockEntity.getBlockPos())
                : FluidRenderer.getFluidColor(fluidStack);
        state.fluidLight = (state.lightCoords & 0xF00000) | luminosity << 4;
        state.bottomY = 0.25f;
        state.surfaceY = state.bottomY + fillRatio * 0.875f;
        state.u0 = sprite.getU0();
        state.u1 = sprite.getU1();
        state.v0 = sprite.getV0();
        state.v1 = sprite.getV1();
        state.sideV0 = Mth.lerp(1 - fillRatio, state.v0, state.v1);
    }

    @Override
    public void submit(LiquefactionCauldronRenderState state, PoseStack pPoseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.empty) {
            return;
        }

        pPoseStack.pushPose();

        var min = 0.25f;
        var max = 1 - min;
        var yMin = state.bottomY;

        submitNodeCollector.submitCustomGeometry(pPoseStack, RenderTypes.fluid(), (pose, builder) -> {
            putVertex(builder, pose, min, state.surfaceY, min, state.color, state.u0, state.v0, Direction.UP, state.fluidLight);
            putVertex(builder, pose, min, state.surfaceY, max, state.color, state.u0, state.v1, Direction.UP, state.fluidLight);
            putVertex(builder, pose, max, state.surfaceY, max, state.color, state.u1, state.v1, Direction.UP, state.fluidLight);
            putVertex(builder, pose, max, state.surfaceY, min, state.color, state.u1, state.v0, Direction.UP, state.fluidLight);

            putSideFace(builder, pose, Direction.NORTH, min, max, yMin, state.surfaceY, min, state.color, state.u0, state.u1, state.sideV0, state.v1, state.fluidLight);
            putSideFace(builder, pose, Direction.SOUTH, min, max, yMin, state.surfaceY, max, state.color, state.u0, state.u1, state.sideV0, state.v1, state.fluidLight);
            putSideFace(builder, pose, Direction.WEST, min, max, yMin, state.surfaceY, min, state.color, state.u0, state.u1, state.sideV0, state.v1, state.fluidLight);
            putSideFace(builder, pose, Direction.EAST, min, max, yMin, state.surfaceY, max, state.color, state.u0, state.u1, state.sideV0, state.v1, state.fluidLight);
        });

        pPoseStack.popPose();
    }

    public static class LiquefactionCauldronRenderState extends BlockEntityRenderState {
        public boolean empty = true;
        public int color;
        public int fluidLight;
        public float bottomY;
        public float surfaceY;
        public float u0;
        public float u1;
        public float v0;
        public float v1;
        public float sideV0;
    }
}
