// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.render;

import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.SalAmmoniacAccumulatorBlockItem;
import com.mojang.blaze3d.vertex.PoseStack;

public class SalAmmoniacAccumulatorItemRenderer extends GeoItemRenderer<SalAmmoniacAccumulatorBlockItem> {

    public SalAmmoniacAccumulatorItemRenderer() {
        super(new SalAmmoniacAccumulatorModel<>());
        this.withScale(0.5f);
    }

    @Override
    public void adjustRenderPose(RenderPassInfo<GeoRenderState> renderPassInfo) {
        super.adjustRenderPose(renderPassInfo);
        PoseStack poseStack = renderPassInfo.poseStack();

        if (this.scaleWidth != 1 && this.scaleHeight != 1) {
            poseStack.translate(this.scaleWidth / 0.5 - 0.5, -0.1, this.scaleWidth / 0.5 - 0.5);
        }
    }

}
