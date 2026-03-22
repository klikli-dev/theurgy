// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.render.TheurgyModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;

public class DigestionVatRenderer implements BlockEntityRenderer<DigestionVatBlockEntity, DigestionVatRenderer.DigestionVatRenderState> {
    private static final Identifier BASE_OPEN_TEXTURE = Theurgy.loc("textures/entity/digestion_vat/digestion_vat_base_open.png");
    private static final Identifier BASE_TEXTURE = Theurgy.loc("textures/entity/digestion_vat/digestion_vat_base.png");
    private static final Identifier SIDE_TEXTURE = Theurgy.loc("textures/entity/digestion_vat/digestion_vat_side.png");
    private static final Identifier FRONT_TEXTURE = Theurgy.loc("textures/entity/digestion_vat/digestion_vat_side_front.png");
    private static final Identifier FRONT_ACTIVE_TEXTURE = Theurgy.loc("textures/entity/digestion_vat/digestion_vat_side_front_active.png");
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
    public DigestionVatRenderState createRenderState() {
        return new DigestionVatRenderState();
    }

    @Override
    public void extractRenderState(
            DigestionVatBlockEntity blockEntity,
            DigestionVatRenderState state,
            float partialTick,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, cameraPosition, breakProgress);
        state.direction = blockEntity.getDirection();
        state.open = blockEntity.getBlockState().getValue(BlockStateProperties.OPEN);
        state.active = blockEntity.storageBehaviour().hasOutput();
    }

    @Override
    public void submit(DigestionVatRenderState state, PoseStack pPoseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        pPoseStack.pushPose();
        Direction direction = state.direction;
        pPoseStack.translate(0.5D, 0.0D, 0.5D);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - direction.toYRot()));
        pPoseStack.translate(-0.5D, 0.0D, -0.5D);

        var baseTexture = state.open ? BASE_OPEN_TEXTURE : BASE_TEXTURE;
        var baseRenderType = net.minecraft.client.renderer.rendertype.RenderTypes.entitySolid(baseTexture);
        submitNodeCollector.submitModelPart(this.neck, pPoseStack, baseRenderType, state.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, state.breakProgress);
        submitNodeCollector.submitModelPart(this.top, pPoseStack, baseRenderType, state.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, state.breakProgress);
        submitNodeCollector.submitModelPart(this.bottom, pPoseStack, baseRenderType, state.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, state.breakProgress);
        this.submitFront(this.frontSide, pPoseStack, submitNodeCollector, state.lightCoords, state.breakProgress, state.active);
        this.submitSide(this.backSide, pPoseStack, submitNodeCollector, state.lightCoords, state.breakProgress);
        this.submitSide(this.leftSide, pPoseStack, submitNodeCollector, state.lightCoords, state.breakProgress);
        this.submitSide(this.rightSide, pPoseStack, submitNodeCollector, state.lightCoords, state.breakProgress);
        pPoseStack.popPose();
    }

    private void submitSide(ModelPart pModelPart, PoseStack pPoseStack, SubmitNodeCollector submitNodeCollector, int packedLight, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        var renderType = net.minecraft.client.renderer.rendertype.RenderTypes.entitySolid(SIDE_TEXTURE);
        submitNodeCollector.submitModelPart(pModelPart, pPoseStack, renderType, packedLight, OverlayTexture.NO_OVERLAY, null, -1, breakProgress);
    }

    private void submitFront(ModelPart pModelPart, PoseStack pPoseStack, SubmitNodeCollector submitNodeCollector, int packedLight, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, boolean isActive) {
        var renderType = net.minecraft.client.renderer.rendertype.RenderTypes.entitySolid(isActive ? FRONT_ACTIVE_TEXTURE : FRONT_TEXTURE);
        submitNodeCollector.submitModelPart(pModelPart, pPoseStack, renderType, packedLight, OverlayTexture.NO_OVERLAY, null, -1, breakProgress);
    }

    public static class DigestionVatRenderState extends BlockEntityRenderState {
        public Direction direction = Direction.NORTH;
        public boolean open;
        public boolean active;
    }
}
