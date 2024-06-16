// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.render;

import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlockEntity;
import com.klikli_dev.theurgy.content.render.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;


public class LiquefactionCauldronRenderer implements BlockEntityRenderer<LiquefactionCauldronBlockEntity> {

    public LiquefactionCauldronRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    private static void putVertex(VertexConsumer builder, PoseStack ms, float x, float y, float z, int color, float u, float v, Direction face, int light) {

        Vec3i normal = face.getNormal();
        PoseStack.Pose peek = ms.last();
        int a = color >> 24 & 0xff;
        int r = color >> 16 & 0xff;
        int g = color >> 8 & 0xff;
        int b = color & 0xff;

        builder.addVertex(peek.pose(), x, y, z)
                .setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(peek, normal.getX(), normal.getY(), normal.getZ());
    }

    /**
     * Based on com.simibubi.create.content.contraptions.fluids.tank.FluidTankR
     */
    @Override
    public void render(LiquefactionCauldronBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (pBlockEntity.storageBehaviour.solventTank.isEmpty())
            return;

        var fluidStack = pBlockEntity.storageBehaviour.solventTank.getFluid();
        var fluid = fluidStack.getFluid();
        var fluidType = fluid.getFluidType();
        var fluidClientExtension = IClientFluidTypeExtensions.of(fluid);

        TextureAtlasSprite fluidTexture = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(fluidClientExtension.getStillTexture(fluidStack));

        int color = fluidClientExtension.getTintColor(fluidStack);
        int blockLightIn = (pPackedLight >> 4) & 0xF;
        int luminosity = Math.max(blockLightIn, fluidType.getLightLevel(fluidStack));
        var fluidLight = (pPackedLight & 0xF00000) | luminosity << 4;

        var fluidHeight = fluidStack.getAmount() / (float) pBlockEntity.storageBehaviour.solventTank.getCapacity();

        fluidHeight *= 0.875f;
        fluidHeight += 0.25f;

        pPoseStack.pushPose();

        var min = 0.25f;
        var max = 1 - min;

        var builder = pBufferSource.getBuffer(RenderTypes.fluid());

        //Hack: the UV calculation doesn't work on 1.20.4, so we just use the full texture for now
        //TODO: fix this for 1.20.4
        var u1 = fluidTexture.getU0();
        var u2 = fluidTexture.getU1();
        var v1 = fluidTexture.getV0();
        var v2 = fluidTexture.getV1();


        putVertex(builder, pPoseStack, min, fluidHeight, min, color,
                u1, v1, Direction.UP, fluidLight);

        putVertex(builder, pPoseStack, min, fluidHeight, max, color,
                u1, v2, Direction.UP, fluidLight);

        putVertex(builder, pPoseStack, max, fluidHeight, max, color,
                u2, v2, Direction.UP, fluidLight);

        putVertex(builder, pPoseStack, max, fluidHeight, min, color,
                u2, v1, Direction.UP, fluidLight);

        pPoseStack.popPose();
    }
}
