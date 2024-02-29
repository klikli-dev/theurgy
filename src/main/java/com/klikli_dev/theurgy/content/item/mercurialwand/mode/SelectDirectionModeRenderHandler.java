package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.content.item.mode.ItemModeRenderHandler;
import com.klikli_dev.theurgy.content.render.BlockOverlays;
import com.klikli_dev.theurgy.content.render.Color;
import com.klikli_dev.theurgy.content.render.RenderTypes;
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

public class SelectDirectionModeRenderHandler extends ItemModeRenderHandler<SelectDirectionMode> {

    public SelectDirectionModeRenderHandler(SelectDirectionMode mode) {
        super(mode);
    }

    @Override
    public void renderBlockHighlight(RenderHighlightEvent.Block event) {
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

        Direction face = this.mode.getDirection(stack);

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
