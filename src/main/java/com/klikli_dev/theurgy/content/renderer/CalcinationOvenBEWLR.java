/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.renderer;

import com.klikli_dev.theurgy.content.block.calcinationoven.CalcinationOvenBlockItem;
import com.klikli_dev.theurgy.content.renderer.model.CalcinationOvenModel;
import com.klikli_dev.theurgy.content.renderer.model.DistillerModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CalcinationOvenBEWLR extends GeoItemRenderer<CalcinationOvenBlockItem> {

    private static final CalcinationOvenBEWLR instance = new CalcinationOvenBEWLR();

    public CalcinationOvenBEWLR() {
        super(new CalcinationOvenModel());
        this.withScale(0.35f);
    }

    public static CalcinationOvenBEWLR get() {
        return instance;
    }

    @Override
    public void preRender(PoseStack poseStack, CalcinationOvenBlockItem animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        //copied from parent, but fixed to also center the model when scaling
        this.itemRenderTranslations = new Matrix4f(poseStack.last().pose());

        if (this.scaleWidth != 1 && this.scaleHeight != 1) {
            poseStack.scale(this.scaleWidth, this.scaleHeight, this.scaleWidth);

            //this is not as clean as I would like it - but it exactly centers the model for 0.35 scale :D
            poseStack.translate(this.scaleWidth / 0.5 + 0.15, -0.2, this.scaleWidth / 0.5);
        }

        poseStack.translate(0.5f, 0.51f, 0.5f);
    }
}
