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

        Vec3 camera = event.getLevelRenderState().cameraRenderState.pos;

        var renderType = ClientConfig.get().rendering.useSimpleWireRenderer.get()
                ? net.minecraft.client.renderer.rendertype.RenderTypes.lines()
                : RenderTypes.distanceLines();

        for (var wire : this.wires) {
            Vec3 fromCenter = Vec3.atCenterOf(wire.from());
            Vec3 toCenter = Vec3.atCenterOf(wire.to());
            Vec3 offset = fromCenter.subtract(camera);

            poseStack.pushPose();
            poseStack.translate(offset.x, offset.y, offset.z);
            collector.submitCustomGeometry(poseStack, renderType, (pose, consumer) -> {
                Vec3 localEnd = toCenter.subtract(fromCenter);
                this.renderWire(pose, consumer, Vec3.ZERO, localEnd, lineWidth);
            });
            poseStack.popPose();
        }
    }

    private void renderWire(PoseStack.Pose pose, VertexConsumer vertexBuilder, Vec3 startPos, Vec3 endPos, float lineWidth) {
        Vec3[] points = WireSlackHelper.getInterpolatedDifferences(endPos.subtract(startPos));

        float r = ((WIRE_COLOR >> 16) & 0xFF) / 255.0f;
        float g = ((WIRE_COLOR >> 8) & 0xFF) / 255.0f;
        float b = (WIRE_COLOR & 0xFF) / 255.0f;
        float a = ((WIRE_COLOR >> 24) & 0xFF) / 255.0f;

        for (int line = 0; line < points.length - 1; line++) {
            Vec3 firstPoint = points[line];
            Vec3 secondPoint = points[line + 1];

            Vec3 normal = secondPoint.subtract(firstPoint).normalize();
            Vec3 reverseNormal = firstPoint.subtract(secondPoint).normalize();

            vertexBuilder.addVertex(pose, (float) firstPoint.x(), (float) firstPoint.y(), (float) firstPoint.z())
                    .setColor(r, g, b, a)
                    .setNormal(pose, (float) normal.x(), (float) normal.y(), (float) normal.z())
                    .setLineWidth(lineWidth);

            vertexBuilder.addVertex(pose, (float) secondPoint.x(), (float) secondPoint.y(), (float) secondPoint.z())
                    .setColor(r, g, b, a)
                    .setNormal(pose, (float) reverseNormal.x(), (float) reverseNormal.y(), (float) reverseNormal.z())
                    .setLineWidth(lineWidth);
        }
    }
}
