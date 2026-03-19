// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator.render;

import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorMercuryVesselBlockItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class IncubatorMercuryVesselItemRenderer extends GeoItemRenderer<IncubatorMercuryVesselBlockItem> {

    private final ItemTransform transform;

    public IncubatorMercuryVesselItemRenderer() {
        super(new IncubatorMercuryVesselModel<>());
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
            return (net.minecraft.client.renderer.special.SpecialModelRenderer<?>) new IncubatorMercuryVesselItemRenderer();
        }

        @Override
        public com.mojang.serialization.MapCodec<? extends net.minecraft.client.renderer.special.SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
