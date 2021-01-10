package com.github.klikli_dev.theurgy.client.model.entity;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.github.klikli_dev.theurgy.common.entity.EmptyEndCrystalSpearEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class EmptyEndCrystalSpearModel extends EntityModel<EmptyEndCrystalSpearEntity> {
    //region Fields
    private final ModelRenderer pole;
    //endregion Fields

    //region Initialization
    public EmptyEndCrystalSpearModel() {
        textureWidth = 32;
        textureHeight = 32;

        pole = new ModelRenderer(this);
        pole.setRotationPoint(0.0F, 0.0F, 0.0F);
        pole.setTextureOffset(0, 6).addBox(-0.5F, 2.0F, -0.5F, 1.0F, 25.0F, 1.0F, 0.01F, true);
        pole.setTextureOffset(4, 25).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.01F, true);
    }
    //endregion Initialization

    //region Overrides
    @Override
    public void setRotationAngles(EmptyEndCrystalSpearEntity entity, float limbSwing, float limbSwingAmount,
                                  float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
                       float green, float blue, float alpha) {
        pole.render(matrixStack, buffer, packedLight, packedOverlay);
    }
    //endregion Overrides

    //region Methods
    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    //endregion Methods
}