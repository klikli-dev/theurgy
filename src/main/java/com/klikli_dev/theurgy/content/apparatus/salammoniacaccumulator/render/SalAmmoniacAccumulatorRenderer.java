// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.render;

import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.SalAmmoniacAccumulatorBlockEntity;
import com.klikli_dev.theurgy.content.render.FluidRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;


public class SalAmmoniacAccumulatorRenderer extends GeoBlockRenderer<SalAmmoniacAccumulatorBlockEntity> {

    public SalAmmoniacAccumulatorRenderer(BlockEntityRendererProvider.Context pContext) {
        super(new SalAmmoniacAccumulatorModel());
    }

    /**
     * See com.simibubi.create.content.fluids.tank.FluidTankRenderer
     */

    @Override
    public void render(SalAmmoniacAccumulatorBlockEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 cameraPosition) {
        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay, cameraPosition);

        if (animatable.waterTank.isEmpty())
            return;

        var fluidStack = animatable.waterTank.getFluid();

        var fluidHeight = fluidStack.getAmount() / (float) animatable.waterTank.getCapacity();

        float blockHeight = 12 / 16f - 2 / 128f;
        float blockWidth = 1;
        float capHeight = 0 / 4f;
        float tankHullWidth = 3 / 16f + 2 / 128f;
        float minPuddleHeight = 1 / 16f;
        float totalHeight = blockHeight - 2 * capHeight - minPuddleHeight;

        float level = fluidHeight;
//        if (level < 1 / (512f * totalHeight)) //leads to not rendering in fill levels below 10/1000 or so.
//            return;
        float clampedLevel = Mth.clamp(level * totalHeight, 0, totalHeight);

        float xMin = tankHullWidth;
        float xMax = xMin + blockWidth - 2 * tankHullWidth;
        float yMin = totalHeight + capHeight + minPuddleHeight - clampedLevel;
        float yMax = yMin + clampedLevel;

        float zMin = tankHullWidth;
        float zMax = zMin + blockWidth - 2 * tankHullWidth;

        poseStack.pushPose();
        poseStack.translate(-0.5f, 0, -0.5f);
        poseStack.translate(0, clampedLevel - totalHeight, 0);
        FluidRenderer.renderFluidBox(fluidStack, xMin, yMin, zMin, xMax, yMax, zMax, bufferSource, poseStack, packedLight, false);
        poseStack.popPose();
    }
}
