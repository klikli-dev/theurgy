/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.apparatus.salammoniactank.render;

import com.klikli_dev.theurgy.content.apparatus.salammoniactank.SalAmmoniacTankBlockEntity;
import com.klikli_dev.theurgy.content.render.FluidRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SalAmmoniacTankRenderer extends GeoBlockRenderer<SalAmmoniacTankBlockEntity> {
    public SalAmmoniacTankRenderer(BlockEntityRendererProvider.Context pContext) {
        super(new SalAmmoniacTankModel());
    }

    /**
     * See com.simibubi.create.content.fluids.tank.FluidTankRenderer
     */
    @Override
    public void actuallyRender(PoseStack poseStack, SalAmmoniacTankBlockEntity pBlockEntity, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int pPackedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, pBlockEntity, model, renderType, bufferSource, buffer, isReRender, partialTick, pPackedLight, packedOverlay, red, green, blue, alpha);

        if (pBlockEntity.tank.isEmpty())
            return;

        var fluidStack = pBlockEntity.tank.getFluid();

        var fluidHeight = fluidStack.getAmount() / (float) pBlockEntity.tank.getCapacity();

        float blockHeight = 15 / 16f;
        float blockWidth = 1;
        float capHeight = 1 / 4f;
        float tankHullWidth = 2 / 16f + 1 / 128f;
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
        poseStack.translate(0, clampedLevel - totalHeight, 0);
        FluidRenderer.renderFluidBox(fluidStack, xMin, yMin, zMin, xMax, yMax, zMax, bufferSource, poseStack, pPackedLight, false);
        poseStack.popPose();
    }
}
