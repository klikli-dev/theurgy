// SPDX-FileCopyrightText: 2020 Commoble
// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

//See upstream https://github.com/Commoble/morered/blob/HEAD/src/main/java/commoble/morered/client/WirePostRenderer.java

package com.klikli_dev.theurgy.logistics;

import com.klikli_dev.theurgy.config.ClientConfig;
import com.klikli_dev.theurgy.content.render.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

import java.util.Collections;
import java.util.Set;

public class WireRenderer {

    private static final WireRenderer instance = new WireRenderer();
    private static final int WIRE_COLOR = 0xFFB87333;

    public Set<Wire> wires = Collections.synchronizedSet(new ObjectOpenHashSet<>());

    public static WireRenderer get() {
        return instance;
    }

    public void onSubmitCustomGeometry(SubmitCustomGeometryEvent event) {
        var minecraft = Minecraft.getInstance();
        var collector = event.getSubmitNodeCollector();
        var poseStack = event.getPoseStack();
        float lineWidth = minecraft.getWindow().getAppropriateLineWidth() * ClientConfig.get().rendering.wireLineWidth.get();

        double renderPosX = minecraft.gameRenderer.mainCamera().position().x();
        double renderPosY = minecraft.gameRenderer.mainCamera().position().y();
        double renderPosZ = minecraft.gameRenderer.mainCamera().position().z();

        poseStack.pushPose();
        poseStack.translate(-renderPosX, -renderPosY, -renderPosZ);

        var renderType = ClientConfig.get().rendering.useSimpleWireRenderer.get()
                ? net.minecraft.client.renderer.rendertype.RenderTypes.lines()
                : RenderTypes.distanceLines();

        collector.submitCustomGeometry(poseStack, renderType, (pose, consumer) -> {
            for (var wire : this.wires) {
                Vec3 startPos = Vec3.atCenterOf(wire.from()).subtract(renderPosX, renderPosY, renderPosZ);
                Vec3 endPos = Vec3.atCenterOf(wire.to()).subtract(renderPosX, renderPosY, renderPosZ);
                this.renderWire(pose, consumer, startPos, endPos, lineWidth);
            }
        });

        poseStack.popPose();
    }

    private void renderWire(PoseStack.Pose pose, VertexConsumer vertexBuilder, Vec3 startPos, Vec3 endPos, float lineWidth) {
        boolean translateSwap = false;
        if (startPos.y() > endPos.y()) {
            Vec3 swap = startPos;
            startPos = endPos;
            endPos = swap;
            translateSwap = true;
        }

        double startX = startPos.x();
        double startY = startPos.y();
        double startZ = startPos.z();

        double endX = endPos.x();
        double endY = endPos.y();
        double endZ = endPos.z();
        float dx = (float) (endX - startX);
        float dy = (float) (endY - startY);
        float dz = (float) (endZ - startZ);

        Vec3 offset = new Vec3(0.5D, 0.5D, 0.5D);
        if (translateSwap) {
            offset = offset.subtract(dx, dy, dz);
        }

        Vec3[] points = WireSlackHelper.getInterpolatedDifferences(endPos.subtract(startPos));

        org.joml.Vector4f posTransformTemp = new org.joml.Vector4f();
        org.joml.Vector3f normalTransformTemp = new org.joml.Vector3f();
        org.joml.Matrix4f posMatrix = pose.pose();
        org.joml.Matrix3f normalMatrix = pose.normal();

        float r = ((WIRE_COLOR >> 16) & 0xFF) / 255.0f;
        float g = ((WIRE_COLOR >> 8) & 0xFF) / 255.0f;
        float b = (WIRE_COLOR & 0xFF) / 255.0f;
        float a = ((WIRE_COLOR >> 24) & 0xFF) / 255.0f;

        for (int line = 0; line < points.length - 1; line++) {
            Vec3 firstPoint = points[line].add(offset);
            Vec3 secondPoint = points[line + 1].add(offset);

            Vec3 normal = secondPoint.subtract(firstPoint).normalize();
            Vec3 reverseNormal = firstPoint.subtract(secondPoint).normalize();

            posTransformTemp.set((float) firstPoint.x(), (float) firstPoint.y(), (float) firstPoint.z(), 1);
            posTransformTemp.mul(posMatrix);
            float x0 = posTransformTemp.x();
            float y0 = posTransformTemp.y();
            float z0 = posTransformTemp.z();

            posTransformTemp.set((float) secondPoint.x(), (float) secondPoint.y(), (float) secondPoint.z(), 1);
            posTransformTemp.mul(posMatrix);
            float x1 = posTransformTemp.x();
            float y1 = posTransformTemp.y();
            float z1 = posTransformTemp.z();

            normalTransformTemp.set((float) normal.x(), (float) normal.y(), (float) normal.z());
            normalTransformTemp.mul(normalMatrix);
            float nx0 = normalTransformTemp.x();
            float ny0 = normalTransformTemp.y();
            float nz0 = normalTransformTemp.z();

            normalTransformTemp.set((float) reverseNormal.x(), (float) reverseNormal.y(), (float) reverseNormal.z());
            normalTransformTemp.mul(normalMatrix);
            float nx1 = normalTransformTemp.x();
            float ny1 = normalTransformTemp.y();
            float nz1 = normalTransformTemp.z();

            vertexBuilder.addVertex(x0, y0, z0)
                    .setColor(r, g, b, a)
                    .setNormal(nx0, ny0, nz0)
                    .setLineWidth(lineWidth);

            vertexBuilder.addVertex(x1, y1, z1)
                    .setColor(r, g, b, a)
                    .setNormal(nx1, ny1, nz1)
                    .setLineWidth(lineWidth);
        }
    }
}
