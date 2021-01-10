/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.client.render.entity;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.client.model.entity.EndInfusedCrystalSpearModel;
import com.github.klikli_dev.theurgy.common.entity.EndInfusedCrystalSpearEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EndInfusedCrystalSpearRenderer extends EntityRenderer<EndInfusedCrystalSpearEntity> {
    //region Fields
    public static final ResourceLocation SPEAR =
            new ResourceLocation(Theurgy.MODID, "textures/entity/end_crystal_spear.png");
    private final EndInfusedCrystalSpearModel spearModel = new EndInfusedCrystalSpearModel();
    //endregion Fields

    //region Initialization
    public EndInfusedCrystalSpearRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }
    //endregion Initialization

    //region Overrides
    @Override
    public void render(EndInfusedCrystalSpearEntity entityIn, float entityYaw, float partialTicks,
                       MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.rotate(
                Vector3f.YP.rotationDegrees(
                        MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw) - 90.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(
                MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch) + 90.0F));
        IVertexBuilder
                ivertexbuilder =
                net.minecraft.client.renderer.ItemRenderer.getEntityGlintVertexBuilder(bufferIn, this.spearModel
                                                                                                         .getRenderType(
                                                                                                                 this.getEntityTexture(
                                                                                                                         entityIn)),
                        false, true);
        //TODO: maybe render with glint false like empty end spear
        this.spearModel
                .render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
                        1.0F);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(EndInfusedCrystalSpearEntity entity) {
        return SPEAR;
    }
    //endregion Overrides
}
