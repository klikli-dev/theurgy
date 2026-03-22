// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.render;

import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlockEntity;
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
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jspecify.annotations.Nullable;


public class LiquefactionCauldronRenderer implements BlockEntityRenderer<LiquefactionCauldronBlockEntity, LiquefactionCauldronRenderer.LiquefactionCauldronRenderState> {

    public LiquefactionCauldronRenderer(BlockEntityRendererProvider.Context pContext) {
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
        var fluidClientExtension = IClientFluidTypeExtensions.of(fluid);

        state.color = fluidClientExtension.getTintColor(fluidStack);
        int blockLightIn = (state.lightCoords >> 4) & 0xF;
        int luminosity = Math.max(blockLightIn, fluidType.getLightLevel(fluidStack));
        state.fluidLight = (state.lightCoords & 0xF00000) | luminosity << 4;
        state.fluidHeight = fluidStack.getAmount() / (float) blockEntity.storageBehaviour.solventTank.getCapacity();
        state.fluidHeight *= 0.875f;
        state.fluidHeight += 0.25f;
    }

    @Override
    public void submit(LiquefactionCauldronRenderState state, PoseStack pPoseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.empty) {
            return;
        }

        pPoseStack.pushPose();

        var min = 0.25f;
        var max = 1 - min;

        //Hack: the UV calculation doesn't work on 1.20.4, so we just use the full texture for now
        //TODO: fix this for 1.20.4
        var u1 = 0.0f;
        var u2 = 1.0f;
        var v1 = 0.0f;
        var v2 = 1.0f;

        submitNodeCollector.submitCustomGeometry(pPoseStack, RenderTypes.fluid(), (pose, builder) -> {
            putVertex(builder, pose, min, state.fluidHeight, min, state.color, u1, v1, Direction.UP, state.fluidLight);
            putVertex(builder, pose, min, state.fluidHeight, max, state.color, u1, v2, Direction.UP, state.fluidLight);
            putVertex(builder, pose, max, state.fluidHeight, max, state.color, u2, v2, Direction.UP, state.fluidLight);
            putVertex(builder, pose, max, state.fluidHeight, min, state.color, u2, v1, Direction.UP, state.fluidLight);
        });

        pPoseStack.popPose();
    }

    public static class LiquefactionCauldronRenderState extends BlockEntityRenderState {
        public boolean empty = true;
        public int color;
        public int fluidLight;
        public float fluidHeight;
    }
}
