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
        BlockPos start = wire.start();
        BlockPos end = wire.end();
        double tautness = wire.tautness();

        poseStack.pushPose();
//        poseStack.translate(start.getX() + 0.5, start.getY()+ 0.5, start.getZ()+ 0.5); //this moved the line  almost into position
        //TODO: this is only needed if we don't get the info from the wire
        float dx = end.getX() - start.getX();
        float dy = end.getY() - start.getY() - (float)tautness; //this is crap :D
        float dz = end.getZ() - start.getZ();

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lineStrip());
        PoseStack.Pose pose = poseStack.last();

        for(int i = 0; i <= 16; ++i) {
            //this version looks good, but renders to the bottom left corner.
            //tautness needs to be negative currently, otherwise wire arches up
            var tStart = wire.getPointAt((float)i / 16.0F);
            var tEnd = wire.getPointAt((float)(i + 1) / 16.0F);
            stringVertex(tStart, tEnd, vertexConsumer, pose);

//            float tStart = (float)i / 16.0F;
//            float tEnd = (float)(i + 1) / 16.0F;
//            stringVertex(dx, dy, dz, vertexConsumer, pose, tStart, tEnd);
        }

        poseStack.popPose();
    }

    private static void stringVertex(Vec3 start, Vec3 end, VertexConsumer vertexConsumer, PoseStack.Pose pose) {
        var normal = end.subtract(start).normalize();
        vertexConsumer.vertex(pose.pose(), (float)start.x, (float)start.y, (float)start.z).color(0, 0, 0, 255).normal(pose.normal(), (float)normal.x, (float)normal.y, (float)normal.z).endVertex();
    }

    private static void stringVertex(float dx, float dy, float dz, VertexConsumer vertexConsumer, PoseStack.Pose pose, float tStart, float tEnd) {
        float x = dx * tStart;
        float y = dy * (tStart * tStart + tStart) * 0.5F + 0.25F;
        float z = dz * tStart;
        float dx1 = dx * tEnd - x;
        float dy1 = dy * (tEnd * tEnd + tEnd) * 0.5F + 0.25F - y;
        float dz1 = dz * tEnd - z;
        float length = Mth.sqrt(dx1 * dx1 + dy1 * dy1 + dz1 * dz1);
        dx1 /= length;
        dy1 /= length;
        dz1 /= length;
        vertexConsumer.vertex(pose.pose(), x, y, z).color(0, 0, 0, 255).normal(pose.normal(), dx1, dy1, dz1).endVertex();
    }
}
