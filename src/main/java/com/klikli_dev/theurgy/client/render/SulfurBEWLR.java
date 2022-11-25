/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.client.render;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.RenderTypeHelper;

public class SulfurBEWLR extends BlockEntityWithoutLevelRenderer {


    private static final SulfurBEWLR instance = new SulfurBEWLR();

    public SulfurBEWLR() {
        super(null, null);
    }

    public static SulfurBEWLR get() {
        return instance;
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
//        BakedModel model = itemRenderer.getModel(pStack, null, null, 0);
//        itemRenderer.render(pStack, ItemTransforms.TransformType.FIXED, true, pPoseStack, pBuffer,
//                pPackedLight, pPackedOverlay, model);
        //BakedModel model = Minecraft.getInstance().getModelManager().getModel(Theurgy.loc("models/item/empty_jar"));
        BakedModel model = itemRenderer.getModel(new ItemStack(ItemRegistry.EMPTY_JAR.get()), null, null, 0);
        model = model.applyTransform(pTransformType, pPoseStack, isLeftHand(pTransformType));
//        poseStack.translate(-.5, -.5, -.5);

        for (var renderPassModel : model.getRenderPasses(pStack, true)) {
            for (var renderType : renderPassModel.getRenderTypes(pStack, true)) {
                renderType = RenderTypeHelper.getEntityRenderType(renderType, true);
                var consumer = pBuffer.getBuffer(renderType);
                itemRenderer.renderModelLists(model, pStack, pPackedLight, pPackedOverlay, pPoseStack, consumer);
            }
        }

        var oreStack = new ItemStack(Items.COPPER_ORE);
        BakedModel oreModel = itemRenderer.getModel(oreStack, null, null, 0);

        pPoseStack.pushPose();
        var scale = 0.3f;

        pPoseStack.translate(0.5 - 0.5 * scale, 0.3, 1);
        pPoseStack.scale(scale,scale,scale);
        pPoseStack.translate(0.5 ,0, 0);
        itemRenderer.render(oreStack, ItemTransforms.TransformType.GUI, true, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, oreModel);
        pPoseStack.popPose();
    }

    private static boolean isLeftHand(ItemTransforms.TransformType type) {
        return type == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND || type == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND;
    }
}
