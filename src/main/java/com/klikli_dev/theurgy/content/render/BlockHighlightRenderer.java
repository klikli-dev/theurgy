/*
 * SPDX-FileCopyrightText: 2023 Aidan C. Brady
 * SPDX-FileCopyrightText: 2024 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.render;

import com.klikli_dev.theurgy.content.item.mercurialwand.mode.MercurialWandItemMode;
import com.klikli_dev.theurgy.content.item.mercurialwand.mode.SelectDirectionMode;
import com.klikli_dev.theurgy.content.render.cube.CubeModel;
import com.klikli_dev.theurgy.content.render.cube.CubeModelRenderer;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

/**
 * Based on Mekanism RenderTickhandler#onBlockHover https://github.com/mekanism/Mekanism/blob/d22f6e2028009ed043f8b40c4ea1f7912be3002c/src/main/java/mekanism/client/render/RenderTickHandler.java#L347-L347
 */
public class BlockHighlightRenderer {

    public static void onRenderBlockHighlight(RenderHighlightEvent.Block event) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        BlockHitResult rayTraceResult = event.getTarget();
        if (rayTraceResult.getType() == BlockHitResult.Type.MISS)
            return;

        var pos = rayTraceResult.getBlockPos();
        var ps = event.getPoseStack();
        var camera = event.getCamera();
        var bufferSource = event.getMultiBufferSource();

        var stack = player.getMainHandItem();
        if(!stack.is(ItemRegistry.MERCURIAL_WAND.get()))
            return;
        //TODO: check for side selector multi tool
        //TODO: use scroll wheel to change side
        //TODO: somehow prevent that from being used to scroll the multi tool

        var mode = MercurialWandItemMode.getMode(stack);
        if (!(mode instanceof SelectDirectionMode selectDirectionMode))
            return;

        Direction face = selectDirectionMode.getDirection(stack);

        //Note: for now we simply do not use transparency here because it does not work nicely with translucent blocks
        //      specifically, it stops them from rendering, os the transparent highlight renders the world behind the bock.
        //      If we want to play with it again, use     public final static Color GREEN = new Color(0, 255, 0, 155).setImmutable();

        Vec3 viewPosition = camera.getPosition();
        ps.pushPose();
        ps.translate(pos.getX() - viewPosition.x, pos.getY() - viewPosition.y, pos.getZ() - viewPosition.z);
        CubeModelRenderer.renderCube(
                CubeModel.getOverlayModel(face, BlockOverlays.WHITE), ps, bufferSource.getBuffer(RenderTypes.translucentCullNoDepthBlockSheet()),
                Color.GREEN.getRGB(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, CubeModelRenderer.FaceDisplay.FRONT,
                camera);
        ps.popPose();
    }
}
