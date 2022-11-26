/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.client.render;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SulfurBEWLR extends BlockEntityWithoutLevelRenderer {

    private static ItemStack emptyJarStack = new ItemStack(ItemRegistry.EMPTY_JAR.get());
    private static ItemStack labelStack = new ItemStack(ItemRegistry.JAR_LABEL.get());

    private static final SulfurBEWLR instance = new SulfurBEWLR();

    public SulfurBEWLR() {
        super(null, null);
    }

    public static SulfurBEWLR get() {
        return instance;
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        pPoseStack.popPose();
        pPoseStack.pushPose();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        BakedModel model = itemRenderer.getModel(emptyJarStack, null, null, 0);

        if(pTransformType == ItemTransforms.TransformType.GUI && !model.usesBlockLight())
            Lighting.setupForFlatItems();
//            Lighting.setupFor3DItems();

        itemRenderer.render(emptyJarStack, pTransformType, isLeftHand(pTransformType), pPoseStack, pBuffer, pPackedLight, pPackedOverlay, model);
//
        if(pTransformType == ItemTransforms.TransformType.GUI && !model.usesBlockLight())
            Lighting.setupFor3DItems();

        BakedModel labelModel = itemRenderer.getModel(labelStack, null, null, 0);

        pPoseStack.pushPose();

        float pixel = 1f/16f;
        pPoseStack.translate(0 , 0, pixel * 0.5); //move it before item
        var scale = 0.7f;
        pPoseStack.scale(0.7f,0.5f, 1);
        pPoseStack.translate(0 , -pixel * 3,0); //position it on the item -> center
        pPoseStack.scale(0.74F, 0.74F, 0.01F); //flatten item

        Lighting.setupForFlatItems(); //always render label flat
        itemRenderer.render(labelStack,  ItemTransforms.TransformType.GUI, isLeftHand(pTransformType), pPoseStack, pBuffer, pPackedLight, pPackedOverlay, labelModel);
        Lighting.setupFor3DItems();

        pPoseStack.popPose();

        //TODO: Get item from nbt
        var oreStack = new ItemStack(Items.COPPER_ORE);
        BakedModel oreModel = itemRenderer.getModel(oreStack, null, null, 0);

        pPoseStack.pushPose();

        pPoseStack.translate(0 , 0,pixel); //move it before item

        scale = 0.5f;
        pPoseStack.scale(scale,scale,scale);
        pPoseStack.translate(0 , -pixel * 3,0); //position it on the item -> center
        pPoseStack.scale(0.74F, 0.74F, 0.01F); //flatten item

        Lighting.setupForFlatItems(); //always render "labeled" item flat
        itemRenderer.render(oreStack,  ItemTransforms.TransformType.GUI, isLeftHand(pTransformType), pPoseStack, pBuffer, pPackedLight, pPackedOverlay, oreModel);
        Lighting.setupFor3DItems();

        pPoseStack.popPose();

    }

    private static boolean isLeftHand(ItemTransforms.TransformType type) {
        return type == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND || type == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND;
    }
}
