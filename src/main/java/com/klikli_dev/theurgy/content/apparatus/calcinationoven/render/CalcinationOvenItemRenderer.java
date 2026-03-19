// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.calcinationoven.render;

import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlockItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CalcinationOvenItemRenderer extends GeoItemRenderer<CalcinationOvenBlockItem> {

    private final ItemTransform transform;

    public CalcinationOvenItemRenderer() {
        super(new CalcinationOvenModel());
        this.withScale(0.5f);
        this.transform = new ItemTransform(new Vector3f(30, 225, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
    }

    @Override
    public void preRender(PoseStack poseStack, CalcinationOvenBlockItem animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
//        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        if (isReRender)
            return;

        //copied from parent, but fixed to also center the model when scaling
        this.itemRenderTranslations = new Matrix4f(poseStack.last().pose());

        if (this.scaleWidth != 1 && this.scaleHeight != 1) {
            poseStack.scale(this.scaleWidth, this.scaleHeight, this.scaleWidth);

            //this is not as clean as I would like it - but it exactly centers the model for 0.5 scale :D
            poseStack.translate(this.scaleWidth / 0.5 - 0.5, -0.1, this.scaleWidth / 0.5 - 0.5);
        }
        poseStack.translate(0.5f, 0.51f, 0.5f);

        if (this.renderPerspective == ItemDisplayContext.GUI) {
            this.transform.apply(false, poseStack);
        }
    }

    public record Unbaked() implements net.minecraft.client.renderer.special.SpecialModelRenderer.Unbaked {
        public static final com.mojang.serialization.MapCodec<Unbaked> MAP_CODEC = com.mojang.serialization.MapCodec.unit(new Unbaked());

        @Override
        public net.minecraft.client.renderer.special.SpecialModelRenderer<?> bake(net.minecraft.client.model.geom.EntityModelSet modelSet) {
            return (net.minecraft.client.renderer.special.SpecialModelRenderer<?>) new CalcinationOvenItemRenderer();
        }

        @Override
        public com.mojang.serialization.MapCodec<? extends net.minecraft.client.renderer.special.SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }

    

    
}
