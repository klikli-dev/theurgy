// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator.render;

import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSulfurVesselBlockItem;
import com.mojang.blaze3d.vertex.PoseStack;

public class IncubatorSulfurVesselItemRenderer extends GeoItemRenderer<IncubatorSulfurVesselBlockItem> {

    public IncubatorSulfurVesselItemRenderer() {
        super(new IncubatorSulfurVesselModel<>());
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
