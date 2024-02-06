package com.klikli_dev.theurgy.content.render;

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

        //Note: for now we simply do not use transparency here because it does not work nicely with translucent blocks
        //      specifically, it stops them from rendering, os the transparent highlight renders the world behind the bock.
        //      If we want to play with it again, use     public final static Color GREEN = new Color(0, 255, 0, 155).setImmutable();

        Direction face = rayTraceResult.getDirection().getOpposite(); //opposite to test rendering behind. Will instead be drawn from the config tool scroll selection
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
