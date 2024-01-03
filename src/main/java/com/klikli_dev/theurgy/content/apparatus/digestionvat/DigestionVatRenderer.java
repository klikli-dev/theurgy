package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;

public class DigestionVatRenderer implements BlockEntityRenderer<DigestionVatBlockEntity> {
    private static final String NECK = "neck";
    private static final String FRONT = "front";
    private static final String BACK = "back";
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String TOP = "top";
    private static final String BOTTOM = "bottom";
    private final ModelPart neck;
    private final ModelPart frontSide;
    private final ModelPart backSide;
    private final ModelPart leftSide;
    private final ModelPart rightSide;
    private final ModelPart top;
    private final ModelPart bottom;
    private final Material baseMaterial = Objects.requireNonNull(Sheets.getDecoratedPotMaterial(DecoratedPotPatterns.BASE));

    public DigestionVatRenderer(BlockEntityRendererProvider.Context pContext) {
        ModelPart modelpart = pContext.bakeLayer(ModelLayers.DECORATED_POT_BASE);
        this.neck = modelpart.getChild("neck");
        this.top = modelpart.getChild("top");
        this.bottom = modelpart.getChild("bottom");
        ModelPart modelpart1 = pContext.bakeLayer(ModelLayers.DECORATED_POT_SIDES);
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

    @Nullable
    private static Material getMaterial(Item pItem) {
        Material material = Sheets.getDecoratedPotMaterial(DecoratedPotPatterns.getResourceKey(pItem));
        if (material == null) {
            material = Sheets.getDecoratedPotMaterial(DecoratedPotPatterns.getResourceKey(Items.BRICK));
        }

        return material;
    }

    @Override
    public void render(DigestionVatBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        Direction direction = pBlockEntity.getDirection();
        pPoseStack.translate(0.5D, 0.0D, 0.5D);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - direction.toYRot()));
        pPoseStack.translate(-0.5D, 0.0D, -0.5D);
        VertexConsumer vertexconsumer = this.baseMaterial.buffer(pBuffer, RenderType::entitySolid);
        this.neck.render(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay);
        this.top.render(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay);
        this.bottom.render(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay);

        this.renderSide(this.frontSide, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, null);
        this.renderSide(this.backSide, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, null);
        this.renderSide(this.leftSide, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, null);
        this.renderSide(this.rightSide, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, null);
        pPoseStack.popPose();
    }

    private void renderSide(ModelPart pModelPart, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay, @Nullable Material pMaterial) {
        if (pMaterial == null) {
            pMaterial = getMaterial(Items.BRICK);
        }

        if (pMaterial != null) {
            pModelPart.render(pPoseStack, pMaterial.buffer(pBuffer, RenderType::entitySolid), pPackedLight, pPackedOverlay);
        }

    }
}
