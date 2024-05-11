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

import java.util.Collections;
import java.util.Set;

public class WireRenderer {

    private static final WireRenderer instance = new WireRenderer();

    public Set<Wire> wires = Collections.synchronizedSet(new ObjectOpenHashSet<>());

    public static WireRenderer get() {
        return instance;
    }

    public void onRenderLevelStage(RenderLevelStageEvent event) {
        var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        var poseStack = event.getPoseStack();

        EntityRenderDispatcher erd = Minecraft.getInstance().getEntityRenderDispatcher();
        double renderPosX = erd.camera.getPosition().x();
        double renderPosY = erd.camera.getPosition().y();
        double renderPosZ = erd.camera.getPosition().z();

        poseStack.pushPose();
        poseStack.translate(-renderPosX, -renderPosY, -renderPosZ);

        //we use lines() to avoid all the wires getting connected as it would happen with linestrip
        var buffer = bufferSource.getBuffer(RenderType.lines());
        for (var wire : this.wires) {
            poseStack.pushPose();
            poseStack.translate(wire.from().getX(), wire.from().getY(), wire.from().getZ());
            this.renderWire(buffer, poseStack, wire.from().getCenter(), wire.to().getCenter());
            poseStack.popPose();
        }
        poseStack.popPose();

        //TODO: render cache?
        //  look up wire in cache
        //  render wires to cache, if not in cache.
        //  clear cache if related wire is no longer rendered (probably should be done from Wires class)
    }

    private void renderWire(VertexConsumer vertexBuilder, PoseStack poseStack, Vec3 startPos, Vec3 endPos) {
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
            Matrix4f fourMatrix = poseStack.last().pose();

            Vec3[] points = WireSlackHelper.getInterpolatedDifferences(endPos.subtract(startPos));

            poseStack.pushPose();
            for (int line = 0; line < points.length - 1; line++) {
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

        }
        poseStack.popPose();
    }
}
