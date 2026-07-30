// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.content.item.mode.ItemModeRenderHandler;
import com.klikli_dev.theurgy.content.item.mode.TargetDirectionSetter;
import com.klikli_dev.theurgy.content.render.BlockOverlays;
import com.klikli_dev.theurgy.content.render.Color;
import com.klikli_dev.theurgy.content.render.RenderTypes;
import com.klikli_dev.theurgy.content.render.cube.CubeModel;
import com.klikli_dev.theurgy.content.render.cube.CubeModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class CycleSelectedDirectionModeRenderHandler extends ItemModeRenderHandler<CycleSelectedDirectionMode> {

    public CycleSelectedDirectionModeRenderHandler(CycleSelectedDirectionMode mode) {
        super(mode);
    }

    @Override
    public void renderBlockHighlight(BlockHitResult rayTraceResult, PoseStack ps, SubmitNodeCollector bufferSource, Camera camera) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        if (rayTraceResult.getType() != BlockHitResult.Type.BLOCK)
            return;

        var blockEntity = player.level().getBlockEntity(rayTraceResult.getBlockPos());
        if (blockEntity instanceof TargetDirectionSetter directionSettable) {
            var currentDirection = directionSettable.targetDirection();
            var newDirection = this.mode.nextDirection(currentDirection);

            var targetPos = directionSettable.targetPos();

            //TODO: port to MC 26.2 rendering API - SubmitNodeCollector doesn't have getBuffer()
            //Vec3 viewPosition = camera.position();
            //ps.pushPose();
            //ps.translate(targetPos.getX() - viewPosition.x, targetPos.getY() - viewPosition.y, targetPos.getZ() - viewPosition.z);
            //CubeModelRenderer.renderCube(
            //        CubeModel.getOverlayModel(newDirection, BlockOverlays.WHITE), ps, bufferSource.getBuffer(RenderTypes.translucentCullNoDepthBlockSheet()),
            //        Color.GREEN.getRGB(), Brightness.FULL_BRIGHT.pack(), OverlayTexture.NO_OVERLAY, CubeModelRenderer.FaceDisplay.FRONT,
            //        camera);
            //if (currentDirection != newDirection) {
            //    CubeModelRenderer.renderCube(
            //            CubeModel.getOverlayModel(currentDirection, BlockOverlays.WHITE), ps, bufferSource.getBuffer(RenderTypes.translucentCullNoDepthBlockSheet()),
            //            Color.YELLOW.getRGB(), Brightness.FULL_BRIGHT.pack(), OverlayTexture.NO_OVERLAY, CubeModelRenderer.FaceDisplay.FRONT,
            //            camera);
            //}
            //ps.popPose();
        }
    }
}
