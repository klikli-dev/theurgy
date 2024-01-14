// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.render;

import com.klikli_dev.theurgy.config.ClientConfig;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurTier;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class AlchemicalSulfurBEWLR extends BlockEntityWithoutLevelRenderer {

    private static final AlchemicalSulfurBEWLR instance = new AlchemicalSulfurBEWLR();
    private static final ItemStack labeledEmptyJarStack = new ItemStack(ItemRegistry.EMPTY_JAR_LABELED_ICON.get());
    private static final ItemStack labelStack = new ItemStack(ItemRegistry.JAR_LABEL_ICON.get());

    private static final Map<AlchemicalSulfurTier, ItemStack> tierToIconMap = Map.of(
            AlchemicalSulfurTier.ABUNDANT, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_ABUNDANT_ICON.get()),
            AlchemicalSulfurTier.COMMON, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_COMMON_ICON.get()),
            AlchemicalSulfurTier.RARE, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_RARE_ICON.get()),
            AlchemicalSulfurTier.PRECIOUS, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_PRECIOUS_ICON.get())
    );

    public AlchemicalSulfurBEWLR() {
        super(null, null);
    }

    public static AlchemicalSulfurBEWLR get() {
        return instance;
    }

    private static boolean isLeftHand(ItemDisplayContext displayContext) {
        return displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
    }

    @Override
    public void renderByItem(ItemStack sulfurStack, ItemDisplayContext displayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        var renderSource = ClientConfig.get().rendering.renderSulfurSourceItem.get();

        //if we do not render the source we show a simplified labeled icon with pixels representing fictional text
        var jarStack = renderSource ? AlchemicalSulfurItem.getEmptyJarStack(sulfurStack)
                : labeledEmptyJarStack;

        pPoseStack.popPose();
        pPoseStack.pushPose(); //reset pose that we get from the item renderer, it moves by half a block which we don't want

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        BakedModel model = itemRenderer.getModel(jarStack, null, null, 0);

        var flatLighting = displayContext == ItemDisplayContext.GUI && !model.usesBlockLight();
        if (flatLighting)
            Lighting.setupForFlatItems();

        itemRenderer.render(jarStack, displayContext, isLeftHand(displayContext), pPoseStack, pBuffer, pPackedLight, pPackedOverlay, model);

        //note: if we reset to 3d item light here it ignores it above and renders dark .. idk why


        this.renderLabelFrame(sulfurStack, displayContext, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);

        //if we render the source we render a text-less clean label and the source item on top of the jar stack
        if (renderSource) {
            this.renderLabel(sulfurStack, displayContext, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
            this.renderContainedItem(sulfurStack, displayContext, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        }

    }

    public void renderLabelFrame(ItemStack sulfurStack, ItemDisplayContext displayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        var tierStack = tierToIconMap.get(AlchemicalSulfurItem.getTier(sulfurStack));
        BakedModel labelModel = itemRenderer.getModel(tierStack, null, null, 0);

        pPoseStack.pushPose();

        //now apply the transform to the label to make it look right in-world -> because below we render with gui transform which would mess it up
        //despite this returning a model (self in fact) it actually modifies the pose stack, hence the pushPose above!
        labelModel.applyTransform(displayContext, pPoseStack, isLeftHand(displayContext));

        pPoseStack.pushPose();

        float pixel = 1f / 16f;
        pPoseStack.translate(0, 0, pixel * 0.5); //move it before item

        //pPoseStack.translate(0, -pixel * 3, 0); //position it on the item -> center
        pPoseStack.scale(1F, 1F, 0.01F); //flatten item

        Lighting.setupForFlatItems(); //always render label flat
        itemRenderer.render(tierStack, ItemDisplayContext.GUI, isLeftHand(displayContext), pPoseStack, pBuffer, pPackedLight, pPackedOverlay, labelModel);
        //note: if we reset to 3d item light here it ignores it above and renders dark .. idk why

        pPoseStack.popPose();

        pPoseStack.popPose();
    }

    public void renderLabel(ItemStack sulfurStack, ItemDisplayContext displayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        BakedModel labelModel = itemRenderer.getModel(labelStack, null, null, 0);

        pPoseStack.pushPose();

        //now apply the transform to the label to make it look right in-world -> because below we render with gui transform which would mess it up
        //despite this returning a model (self in fact) it actually modifies the pose stack, hence the pushPose above!
        labelModel.applyTransform(displayContext, pPoseStack, isLeftHand(displayContext));

        pPoseStack.pushPose();

        float pixel = 1f / 16f;
        pPoseStack.translate(0, 0, pixel * 0.5); //move it before item

        //pPoseStack.translate(0, -pixel * 3, 0); //position it on the item -> center
        pPoseStack.scale(1F, 1F, 0.01F); //flatten item

        Lighting.setupForFlatItems(); //always render label flat
        itemRenderer.render(labelStack, ItemDisplayContext.GUI, isLeftHand(displayContext), pPoseStack, pBuffer, pPackedLight, pPackedOverlay, labelModel);
        //note: if we reset to 3d item light here it ignores it above and renders dark .. idk why

        pPoseStack.popPose();

        pPoseStack.popPose();
    }

    public void renderContainedItem(ItemStack sulfurStack, ItemDisplayContext pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        var containedStack = AlchemicalSulfurItem.getSourceStack(sulfurStack);
        if (!containedStack.isEmpty()) {
            BakedModel containedModel = itemRenderer.getModel(containedStack, null, null, 0);
            BakedModel labelModel = itemRenderer.getModel(labelStack, null, null, 0);
            pPoseStack.pushPose();

            //now apply the transform to the contained item to make it look right in-world -> because below we render with gui transform which would mess it up
            //despite this returning a model (self in fact) it actually modifies the pose stack, hence the pushPose above!
            labelModel.applyTransform(pTransformType, pPoseStack, isLeftHand(pTransformType)); //reuse the label transform to simulate flat items even if the contained item is 3d

            pPoseStack.pushPose();

            float pixel = 1f / 16f;
            pPoseStack.translate(0, 0, pixel * 0.6); //move it before label

            var scale = 0.36f;
            pPoseStack.scale(scale, scale, scale);
            pPoseStack.translate(0, -pixel * 3.2, 0); //position it on the item -> center
            pPoseStack.scale(0.74F, 0.74F, 0.01F); //flatten item

            Lighting.setupForFlatItems(); //always render "labeled" item flat

            //set graysacle shader color
            itemRenderer.render(containedStack, ItemDisplayContext.GUI, isLeftHand(pTransformType), pPoseStack, pBuffer, pPackedLight,

                    pPackedOverlay, containedModel);
            //note: if we reset to 3d item light here it ignores it above and renders dark .. idk why

            pPoseStack.popPose();
            pPoseStack.popPose();
        }
    }
}
