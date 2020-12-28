/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
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

package com.github.klikli_dev.theurgy.client.render.tile;

import com.github.klikli_dev.theurgy.common.tile.CrucibleTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

public class CrucibleRenderer extends TileEntityRenderer<CrucibleTileEntity> {

    //region Initialization
    public CrucibleRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }
    //endregion Initialization

    //region Overrides
    @Override
    public void render(CrucibleTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.waterLevel > 0) {
            Minecraft minecraft = Minecraft.getInstance();

            //get vanilla water texture
            TextureAtlasSprite waterPlane = minecraft.getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE)
                                                    .apply(new ResourceLocation("minecraft", "block/water_still"));

            //water default color is 0x3F76E4, if it has contents show it as purple 0x9932CC
            int color = tileEntityIn.hasContents ? 0x9932CC : 0x3F76E4;
            int r = ColorHelper.PackedColor.getRed(color);
            int g = ColorHelper.PackedColor.getGreen(color);
            int b = ColorHelper.PackedColor.getBlue(color);
            int a = 192;
            //Draw water plan
            IVertexBuilder builder = bufferIn.getBuffer(RenderType.getTranslucentNoCrumbling());
            Matrix4f mat = matrixStackIn.getLast().getMatrix();

            float waterPlaneHeight = 0.1f + 0.2f * tileEntityIn.waterLevel;

            builder.pos(mat, 0.125f, waterPlaneHeight, 0.125f).color(r, g, b, a)
                    .tex(waterPlane.getInterpolatedU(2), waterPlane.getInterpolatedV(2)).lightmap(combinedLightIn)
                    .normal(0, 1, 0).endVertex();
            builder.pos(mat, 0.125f, waterPlaneHeight, 0.875f).color(r, g, b, a)
                    .tex(waterPlane.getInterpolatedU(14), waterPlane.getInterpolatedV(2)).lightmap(combinedLightIn)
                    .normal(0, 1, 0).endVertex();
            builder.pos(mat, 0.875f, waterPlaneHeight, 0.875f).color(r, g, b, a)
                    .tex(waterPlane.getInterpolatedU(14), waterPlane.getInterpolatedV(14)).lightmap(combinedLightIn)
                    .normal(0, 1, 0).endVertex();
            builder.pos(mat, 0.875f, waterPlaneHeight, 0.125f).color(r, g, b, a)
                    .tex(waterPlane.getInterpolatedU(2), waterPlane.getInterpolatedV(14)).lightmap(combinedLightIn)
                    .normal(0, 1, 0).endVertex();
        }
    }
    //endregion Overrides

}
