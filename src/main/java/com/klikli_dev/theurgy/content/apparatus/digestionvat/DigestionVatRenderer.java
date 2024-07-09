// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.render.TheurgyModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.EnumSet;

public class DigestionVatRenderer implements BlockEntityRenderer<DigestionVatBlockEntity> {
    private static final ResourceLocation BASE_OPEN_TEXTURE = Theurgy.loc("textures/entity/digestion_vat/digestion_vat_base_open.png");
    private static final ResourceLocation BASE_TEXTURE = Theurgy.loc("textures/entity/digestion_vat/digestion_vat_base.png");
    private static final ResourceLocation SIDE_TEXTURE = Theurgy.loc("textures/entity/digestion_vat/digestion_vat_side.png");
    private static final ResourceLocation FRONT_TEXTURE = Theurgy.loc("textures/entity/digestion_vat/digestion_vat_side_front.png");
    private static final ResourceLocation FRONT_ACTIVE_TEXTURE = Theurgy.loc("textures/entity/digestion_vat/digestion_vat_side_front_active.png");
    private final ModelPart neck;
    private final ModelPart frontSide;
    private final ModelPart backSide;
    private final ModelPart leftSide;
    private final ModelPart rightSide;
    private final ModelPart top;
    private final ModelPart bottom;

    public DigestionVatRenderer(BlockEntityRendererProvider.Context pContext) {
        ModelPart modelpart = pContext.bakeLayer(TheurgyModelLayers.DIGESTION_VAT_BASE);
        this.neck = modelpart.getChild("neck");
        this.top = modelpart.getChild("top");
        this.bottom = modelpart.getChild("bottom");
        ModelPart modelpart1 = pContext.bakeLayer(TheurgyModelLayers.DIGESTION_VAT_SIDES);
        this.frontSide = modelpart1.getChild("front");
        this.backSide = modelpart1.getChild("back");
        this.leftSide = modelpart1.getChild("left");
        this.rightSide = modelpart1.getChild("right");
    }

    public static LayerDefinition createBaseLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        CubeDeformation cubedeformation = new CubeDeformation(0.2F);
        CubeDeformation cubedeformation1 = new CubeDeformation(-0.1F);
        partdefinition.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 0).addBox(4.0F, 17.0F, 4.0F, 8.0F, 3.0F, 8.0F, cubedeformation1).texOffs(0, 5).addBox(5.0F, 20.0F, 5.0F, 6.0F, 1.0F, 6.0F, cubedeformation), PartPose.offsetAndRotation(0.0F, 37.0F, 16.0F, (float) Math.PI, 0.0F, 0.0F));
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(-14, 13).addBox(0.0F, 0.0F, 0.0F, 14.0F, 0.0F, 14.0F);
        partdefinition.addOrReplaceChild("top", cubelistbuilder, PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("bottom", cubelistbuilder, PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public static LayerDefinition createSidesLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F, EnumSet.of(Direction.NORTH));
        partdefinition.addOrReplaceChild("back", cubelistbuilder, PartPose.offsetAndRotation(15.0F, 16.0F, 1.0F, 0.0F, 0.0F, (float) Math.PI));
        partdefinition.addOrReplaceChild("left", cubelistbuilder, PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, 0.0F, (-(float) Math.PI / 2F), (float) Math.PI));
        partdefinition.addOrReplaceChild("right", cubelistbuilder, PartPose.offsetAndRotation(15.0F, 16.0F, 15.0F, 0.0F, ((float) Math.PI / 2F), (float) Math.PI));
        partdefinition.addOrReplaceChild("front", cubelistbuilder, PartPose.offsetAndRotation(1.0F, 16.0F, 15.0F, (float) Math.PI, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void render(DigestionVatBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        Direction direction = pBlockEntity.getDirection();
        pPoseStack.translate(0.5D, 0.0D, 0.5D);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - direction.toYRot()));
        pPoseStack.translate(-0.5D, 0.0D, -0.5D);

        var baseTexture = pBlockEntity.getBlockState().getValue(BlockStateProperties.OPEN) ? BASE_OPEN_TEXTURE : BASE_TEXTURE;
        var baseRenderType = RenderType.entitySolid(baseTexture);
//        VertexConsumer vertexconsumer = this.baseMaterial.buffer(pBuffer, RenderType::entitySolid);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(baseRenderType);
        this.neck.render(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay);
        this.top.render(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay);
        this.bottom.render(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay);

        var isActive = pBlockEntity.storageBehaviour().hasOutput();

        this.renderFront(this.frontSide, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, isActive);
        this.renderSide(this.backSide, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        this.renderSide(this.leftSide, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        this.renderSide(this.rightSide, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        pPoseStack.popPose();
    }

    private void renderSide(ModelPart pModelPart, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        var renderType = RenderType.entitySolid(SIDE_TEXTURE);
        pModelPart.render(pPoseStack, pBuffer.getBuffer(renderType), pPackedLight, pPackedOverlay);
    }

    private void renderFront(ModelPart pModelPart, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay, boolean isActive) {
        var renderType = RenderType.entitySolid(isActive ? FRONT_ACTIVE_TEXTURE : FRONT_TEXTURE);
        pModelPart.render(pPoseStack, pBuffer.getBuffer(renderType), pPackedLight, pPackedOverlay);
    }
}
