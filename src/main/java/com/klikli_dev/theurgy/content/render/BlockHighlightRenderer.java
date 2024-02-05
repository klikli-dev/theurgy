package com.klikli_dev.theurgy.content.render;

import com.klikli_dev.theurgy.content.render.cube.CubeModel;
import com.klikli_dev.theurgy.content.render.cube.CubeModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

public class BlockHighlightRenderer {

    public final static Color GREEN = new Color(0, 255, 0, 155).setImmutable();
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
        //TODO: check for side selector multi tool
        //TODO: use scroll wheel to change side
        //TODO: somehow prevent that from being used to scroll the multi tool

        //TODO: currently it hides the catalyst, probably because it is a translucent block
        //      confirmed, does the same to stained glass
        Direction face = rayTraceResult.getDirection();
        Vec3 viewPosition = camera.getPosition();
        ps.pushPose();
        ps.translate(pos.getX() - viewPosition.x, pos.getY() - viewPosition.y, pos.getZ() - viewPosition.z);
        CubeModelRenderer.renderCube(
                CubeModel.getOverlayModel(face, BlockOverlays.WHITE), ps, bufferSource.getBuffer(Sheets.translucentCullBlockSheet()),
                new Color(0, 255, 0, 155).getRGB(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, CubeModelRenderer.FaceDisplay.FRONT,
                camera);
        ps.popPose();
    }
}
