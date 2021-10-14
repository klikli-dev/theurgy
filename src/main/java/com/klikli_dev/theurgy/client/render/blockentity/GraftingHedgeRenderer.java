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

package com.klikli_dev.theurgy.client.render.blockentity;

import com.klikli_dev.theurgy.block.GraftingHedgeBlock;
import com.klikli_dev.theurgy.blockentity.GraftingHedgeBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;

public class GraftingHedgeRenderer implements BlockEntityRenderer<GraftingHedgeBlockEntity> {

    public GraftingHedgeRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(GraftingHedgeBlockEntity pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
        if (!pBlockEntity.getFruitToGrow().isEmpty() &&
                pBlockEntity.getBlockState().getValue(GraftingHedgeBlock.AGE) == GraftingHedgeBlock.MAX_AGE) {

            pMatrixStack.pushPose();

            //place at center of hedge
            pMatrixStack.translate(0.5, 0.7, 0.5);

            //scale
            float scale = 0.5f;
            pMatrixStack.scale(scale, scale, scale);

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            BakedModel model = itemRenderer.getModel(pBlockEntity.getFruitToGrow(), pBlockEntity.getLevel(), null, 0);
            itemRenderer.render(pBlockEntity.getFruitToGrow(), ItemTransforms.TransformType.FIXED, true, pMatrixStack, pBuffer,
                    pCombinedLight, pCombinedOverlay, model);

            pMatrixStack.popPose();
        }
    }
}
