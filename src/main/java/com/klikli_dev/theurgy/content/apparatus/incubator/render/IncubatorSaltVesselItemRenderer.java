// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator.render;

import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSaltVesselBlockItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;
import com.geckolib.cache.model.BakedGeoModel;
import com.geckolib.constant.DataTickets;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.renderer.base.GeoRenderState;

public class IncubatorSaltVesselItemRenderer extends GeoItemRenderer<IncubatorSaltVesselBlockItem> {

    private final ItemTransform transform;

    public IncubatorSaltVesselItemRenderer() {
        super(new IncubatorSaltVesselModel<>());
        this.withScale(0.5f);
        this.transform = new ItemTransform(new Vector3f(30, 225, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
    }

    @Override
    public void adjustPositionForRender(GeoRenderState renderState, PoseStack poseStack, BakedGeoModel model, boolean isReRender) {
        super.adjustPositionForRender(renderState, poseStack, model, isReRender);

        if (isReRender) {
            return;
        }

        if (this.scaleWidth != 1 && this.scaleHeight != 1) {
            poseStack.translate(this.scaleWidth / 0.5 - 0.5, -0.1, this.scaleWidth / 0.5 - 0.5);
        }

        if (renderState.getOrDefaultGeckolibData(DataTickets.ITEM_RENDER_PERSPECTIVE, ItemDisplayContext.NONE) == ItemDisplayContext.GUI) {
            this.transform.apply(false, poseStack.last());
        }
    }

    public record Unbaked() implements net.minecraft.client.renderer.special.SpecialModelRenderer.Unbaked {
        public static final com.mojang.serialization.MapCodec<Unbaked> MAP_CODEC = com.mojang.serialization.MapCodec.unit(new Unbaked());

        @Override
        public net.minecraft.client.renderer.special.SpecialModelRenderer<?> bake(net.minecraft.client.model.geom.EntityModelSet modelSet) {
            return (net.minecraft.client.renderer.special.SpecialModelRenderer<?>) new IncubatorSaltVesselItemRenderer();
        }

        @Override
        public com.mojang.serialization.MapCodec<? extends net.minecraft.client.renderer.special.SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
