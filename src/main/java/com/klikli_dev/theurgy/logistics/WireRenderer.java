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
                poseStack.pushPose();
                poseStack.translate(wire.from().getX(), wire.from().getY(), wire.from().getZ());
                this.renderWire(consumer, poseStack, Vec3.atCenterOf(wire.from()), Vec3.atCenterOf(wire.to()), lineWidth);
                poseStack.popPose();
            }
        });

        poseStack.popPose();
    }

    private void renderWire(VertexConsumer vertexBuilder, PoseStack poseStack, Vec3 startPos, Vec3 endPos, float lineWidth) {
        poseStack.pushPose();
        {

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
            var pose = poseStack.last();

            Vec3[] points = WireSlackHelper.getInterpolatedDifferences(endPos.subtract(startPos));

            poseStack.pushPose();
            for (int line = 0; line < points.length - 1; line++) {
                Vec3 firstPoint = points[line];
                Vec3 secondPoint = points[line + 1];

                Vec3 normal = secondPoint.subtract(firstPoint).normalize();
                Vec3 reverseNormal = firstPoint.subtract(secondPoint).normalize();

                vertexBuilder.addVertex(pose, (float) firstPoint.x(), (float) firstPoint.y(), (float) firstPoint.z())
                        .setColor(WIRE_COLOR)
                        .setNormal(pose, (float) normal.x(), (float) normal.y(), (float) normal.z())
                        .setLineWidth(lineWidth);

                vertexBuilder.addVertex(pose, (float) secondPoint.x(), (float) secondPoint.y(), (float) secondPoint.z())
                        .setColor(WIRE_COLOR)
                        .setNormal(pose, (float) reverseNormal.x(), (float) reverseNormal.y(), (float) reverseNormal.z())
                        .setLineWidth(lineWidth);
            }
            poseStack.popPose();

        }
        poseStack.popPose();
    }
}
