package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.content.item.mode.ItemModeRenderHandler;
import com.klikli_dev.theurgy.content.item.mode.TargetDirectionSetter;
import com.klikli_dev.theurgy.content.render.BlockOverlays;
import com.klikli_dev.theurgy.content.render.Color;
import com.klikli_dev.theurgy.content.render.RenderTypes;
import com.klikli_dev.theurgy.content.render.cube.CubeModel;
import com.klikli_dev.theurgy.content.render.cube.CubeModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

public class SetSelectedDirectionModeRenderHandler extends ItemModeRenderHandler<SetSelectedDirectionMode> {

    public SetSelectedDirectionModeRenderHandler(SetSelectedDirectionMode mode) {
        super(mode);
    }

    @Override
    public void renderBlockHighlight(RenderHighlightEvent.Block event) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        BlockHitResult rayTraceResult = event.getTarget();
        if (rayTraceResult.getType() != BlockHitResult.Type.BLOCK)
            return;

        var blockEntity = player.level().getBlockEntity(rayTraceResult.getBlockPos());
        if (blockEntity instanceof TargetDirectionSetter directionSettable) {
            var currentDirection = directionSettable.targetDirection();

            var stack = player.getMainHandItem();
            var newDirection = this.mode.getDirection(stack);
            var targetPos = directionSettable.targetPos();

            var ps = event.getPoseStack();
            var camera = event.getCamera();
            var bufferSource = event.getMultiBufferSource();

            //Note: for now we simply do not use transparency here because it does not work nicely with translucent blocks
            //      specifically, it stops them from rendering, os the transparent highlight renders the world behind the bock.
            //      If we want to play with it again, use     public final static Color GREEN = new Color(0, 255, 0, 155).setImmutable();

            Vec3 viewPosition = camera.getPosition();
            ps.pushPose();
            ps.translate(targetPos.getX() - viewPosition.x, targetPos.getY() - viewPosition.y, targetPos.getZ() - viewPosition.z);
            CubeModelRenderer.renderCube(
                    CubeModel.getOverlayModel(newDirection, BlockOverlays.WHITE), ps, bufferSource.getBuffer(RenderTypes.translucentCullNoDepthBlockSheet()),
                    Color.GREEN.getRGB(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, CubeModelRenderer.FaceDisplay.FRONT,
                    camera);
            if (currentDirection != newDirection) {
                CubeModelRenderer.renderCube(
                        CubeModel.getOverlayModel(currentDirection, BlockOverlays.WHITE), ps, bufferSource.getBuffer(RenderTypes.translucentCullNoDepthBlockSheet()),
                        Color.YELLOW.getRGB(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, CubeModelRenderer.FaceDisplay.FRONT,
                        camera);
            }
            ps.popPose();
        }
    }
}
