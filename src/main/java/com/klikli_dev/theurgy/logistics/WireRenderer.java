// SPDX-FileCopyrightText: 2020 Commoble
// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

//See upstream https://github.com/Commoble/morered/blob/HEAD/src/main/java/commoble/morered/client/WirePostRenderer.java

package com.klikli_dev.theurgy.logistics;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.Set;

public class WireRenderer {

    private static final WireRenderer instance = new WireRenderer();

    public Set<Wire> wires = new ObjectOpenHashSet<>();

    public static WireRenderer get() {
        return instance;
    }

    private static float fraction(int numerator, int denominator) {
        return (float) numerator / (float) denominator;
    }

    private static void stringVertex(float x, float y, float z, VertexConsumer consumer, PoseStack.Pose pose, float p_174124_, float p_174125_) {
        float f = x * p_174124_;
        float sag = -0.5F; // Adjust this value to increase or decrease the amount of sag
        float f1 = y * (p_174124_ * p_174124_ + p_174124_) * 0.5F + 0.25F - sag * Math.abs(p_174124_ - 0.5F);
        float f2 = z * p_174124_;
        float f3 = x * p_174125_ - f;
        float f4 = y * (p_174125_ * p_174125_ + p_174125_) * 0.5F + 0.25F - sag * Math.abs(p_174125_ - 0.5F) - f1;
        float f5 = z * p_174125_ - f2;
        float f6 = (float) Math.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
        f3 /= f6;
        f4 /= f6;
        f5 /= f6;
        consumer.vertex(pose.pose(), f, f1, f2).color(0, 0, 0, 255).normal(pose.normal(), f3, f4, f5).endVertex();
    }

    private static void stringVertex(Vec3 start, Vec3 end, VertexConsumer vertexConsumer, PoseStack.Pose pose) {
        var normal = end.subtract(start).normalize();
        vertexConsumer
                .vertex(pose.pose(), (float) start.x, (float) start.y, (float) start.z)
                .color(0, 0, 0, 255)
                .normal(pose.normal(), (float) normal.x, (float) normal.y, (float) normal.z)
                .endVertex();
    }

    public void onRenderLevelStage(RenderLevelStageEvent event) {
        var level = Minecraft.getInstance().level;
        var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
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

        //we use lines() to avoid all the wires getting connected as it would happen with linestrip
        var buffer = bufferSource.getBuffer(RenderType.lines());
        for (var wire : this.wires) {
//            this.renderWire(wire, poseStack, buffer, 0xFFFFFF);
            poseStack.pushPose();
            poseStack.translate(wire.start().getX(), wire.start().getY(), wire.start().getZ());
            this.renderWire(buffer, poseStack, wire.start().getCenter(), wire.end().getCenter());
            poseStack.popPose();
//            this.render(wire, poseStack, buffer);
        }
        poseStack.popPose();

        //TODO: render wires to cache
    }

    private void renderWire(VertexConsumer vertexBuilder, PoseStack poseStack, Vec3 startPos, Vec3 endPos) {
        poseStack.pushPose();

        boolean translateSwap = false;
        if (startPos.y() > endPos.y()) {
            Vec3 swap = startPos;
            startPos = endPos;
            endPos = swap;
            translateSwap = true;
        }

        poseStack.translate(0.5D, 0.5D, 0.5D);

        double startX = startPos.x();
        double startY = startPos.y();
        double startZ = startPos.z();

        double endX = endPos.x();
        double endY = endPos.y();
        double endZ = endPos.z();
        float dx = (float) (endX - startX);
        float dy = (float) (endY - startY);
        float dz = (float) (endZ - startZ);
        if (translateSwap) {
            poseStack.translate(-dx, -dy, -dz);
        }
        Matrix4f fourMatrix = poseStack.last().pose();

        Vec3[] points = WireSlackHelper.getInterpolatedDifferences(endPos.subtract(startPos));

        poseStack.pushPose();
        for (int line = 0; line < points.length -1; line++) {
            Vec3 firstPoint = points[line];
            Vec3 secondPoint = points[line + 1];

            Vec3 normal = secondPoint.subtract(firstPoint).normalize();
            Vec3 reverseNormal = firstPoint.subtract(secondPoint).normalize();

            vertexBuilder.vertex(fourMatrix, (float) firstPoint.x(), (float) firstPoint.y(), (float) firstPoint.z())
                    .color(0, 0, 0, 255)
                    .normal(poseStack.last().normal(), (float) normal.x(), (float) normal.y(), (float) normal.z())
                    .endVertex();

            vertexBuilder.vertex(fourMatrix, (float) secondPoint.x(), (float) secondPoint.y(), (float) secondPoint.z())
                    .color(0, 0, 0, 255)
                    .normal(poseStack.last().normal(), (float) reverseNormal.x(), (float) reverseNormal.y(), (float) reverseNormal.z())
                    .endVertex();
        }
        poseStack.popPose();

        poseStack.popPose();
    }

    //TODO: cache the vertices for each wire and look up in a cache.
    //TODO: clear cache if related wire is no longer rendered
}
