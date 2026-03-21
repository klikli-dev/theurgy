// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniactank.render;

import com.geckolib.constant.DataTickets;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.internal.GeckolibItemSpecialRenderer;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.SalAmmoniacTankBlockItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;

public class SalAmmoniacTankItemRenderer extends GeoItemRenderer<SalAmmoniacTankBlockItem> {

    private final ItemTransform transform;

    public SalAmmoniacTankItemRenderer() {
        super(new SalAmmoniacTankModel<>());
        this.withScale(0.35f);
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

    public record Unbaked() implements SpecialModelRenderer.Unbaked<GeckolibItemSpecialRenderer.RenderData<SalAmmoniacTankBlockItem>> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<GeckolibItemSpecialRenderer.RenderData<SalAmmoniacTankBlockItem>> bake(SpecialModelRenderer.BakingContext context) {
            return new GeckolibItemSpecialRenderer<>();
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked<GeckolibItemSpecialRenderer.RenderData<SalAmmoniacTankBlockItem>>> type() {
            return MAP_CODEC;
        }
    }
}
