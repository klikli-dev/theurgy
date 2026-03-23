// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator.render;

import com.geckolib.constant.DataTickets;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSaltVesselBlockItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;

public class IncubatorSaltVesselItemRenderer extends GeoItemRenderer<IncubatorSaltVesselBlockItem> {

    private final ItemTransform transform;

    public IncubatorSaltVesselItemRenderer() {
        super(new IncubatorSaltVesselModel<>());
        this.withScale(0.5f);
        this.transform = new ItemTransform(new Vector3f(30, 225, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
    }

    @Override
    public void adjustRenderPose(RenderPassInfo<GeoRenderState> renderPassInfo) {
        super.adjustRenderPose(renderPassInfo);
        PoseStack poseStack = renderPassInfo.poseStack();

        if (this.scaleWidth != 1 && this.scaleHeight != 1) {
            poseStack.translate(this.scaleWidth / 0.5 - 0.5, -0.1, this.scaleWidth / 0.5 - 0.5);
        }

        if (renderPassInfo.renderState().getOrDefaultGeckolibData(DataTickets.ITEM_RENDER_PERSPECTIVE, ItemDisplayContext.NONE) == ItemDisplayContext.GUI) {
            this.transform.apply(false, poseStack.last());
        }
    }
}
