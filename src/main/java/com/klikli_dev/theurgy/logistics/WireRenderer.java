package com.klikli_dev.theurgy.logistics;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.Set;

public class WireRenderer {

    private static final WireRenderer instance = new WireRenderer();

    public Set<Wire> wires = new ObjectOpenHashSet<>();

    public static WireRenderer get() {
        return instance;
    }

    public void onRenderLevelStage(RenderLevelStageEvent event) {
        var level = Minecraft.getInstance().level;
        var buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        var poseStack = event.getPoseStack();

        //TODO: the Wires class takes care of which wires visible client side, so we just render all
        //      we keep sections so we can remove on chunk unload
        //TODO: likely we can switch that just to a set or even a list as we can remove by conneciton hash

        EntityRenderDispatcher erd = Minecraft.getInstance().getEntityRenderDispatcher();
        double renderPosX = erd.camera.getPosition().x();
        double renderPosY = erd.camera.getPosition().y();
        double renderPosZ = erd.camera.getPosition().z();

        poseStack.pushPose();
        poseStack.translate(-renderPosX, -renderPosY, -renderPosZ);
        //this caused the wire to stabilize - render in the same spto always - but in the wrong spot (in the sky)

        for(var wire : this.wires) {
            this.renderWire(wire, poseStack, buffer, 0xFFFFFF);
        }

        poseStack.popPose();
    }

    public void renderWire(Wire wire, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lineStrip());
        PoseStack.Pose pose = poseStack.last();

        for(int i = 0; i <= 16; ++i) {
            var tStart = wire.getPointAt((float)i / 16.0F);
            var tEnd = wire.getPointAt((float)(i + 1) / 16.0F);
            tStart = tStart.add(0.5, 0.5, 0.5); //move to center of block
            tEnd = tEnd.add(0.5, 0.5, 0.5);
            stringVertex(tStart, tEnd, vertexConsumer, pose);
        }

        poseStack.popPose();
    }

    private static void stringVertex(Vec3 start, Vec3 end, VertexConsumer vertexConsumer, PoseStack.Pose pose) {
        var normal = end.subtract(start).normalize();
        vertexConsumer
                .vertex(pose.pose(), (float)start.x, (float)start.y, (float)start.z)
                .color(0, 0, 0, 255)
                .normal(pose.normal(), (float)normal.x, (float)normal.y, (float)normal.z)
                .endVertex();
    }
}
