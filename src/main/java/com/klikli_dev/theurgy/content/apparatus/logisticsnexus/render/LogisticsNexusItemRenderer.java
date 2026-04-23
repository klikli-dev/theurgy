// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsnexus.render;

import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.GeoRenderLayer;
import com.klikli_dev.theurgy.content.apparatus.logisticsnexus.LogisticsNexusBlockItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import java.util.function.BiConsumer;

import com.geckolib.cache.model.GeoBone;
import com.geckolib.renderer.base.PerBoneRender;

public class LogisticsNexusItemRenderer extends GeoItemRenderer<LogisticsNexusBlockItem> {
    public LogisticsNexusItemRenderer() {
        super(new LogisticsNexusModel<>());
        this.withRenderLayer(new PortalLayer(this));
    }

    @Override
    public void scaleModelForRender(RenderPassInfo<GeoRenderState> renderPassInfo, float widthScale, float heightScale) {
        super.scaleModelForRender(renderPassInfo, widthScale * 2.0f, heightScale * 2.0f);
    }

    @Override
    public void adjustRenderPose(RenderPassInfo<GeoRenderState> renderPassInfo) {
        PoseStack poseStack = renderPassInfo.poseStack();

        poseStack.translate(0.25f, -4/16f, 0.25f);
    }

    @Override
    public void adjustModelBonesForRender(RenderPassInfo<GeoRenderState> renderPassInfo, BoneSnapshots snapshots) {
        snapshots.ifPresent("Portal", snapshot -> snapshot.skipRender(true).skipChildrenRender(true));
        snapshots.ifPresent("Connectors", snapshot -> snapshot.skipRender(true).skipChildrenRender(true));
    }

    private static class PortalLayer extends GeoRenderLayer<LogisticsNexusBlockItem, GeoItemRenderer.RenderData, GeoRenderState> {
        private PortalLayer(GeoItemRenderer<LogisticsNexusBlockItem> renderer) {
            super(renderer);
        }

        @Override
        public void addPerBoneRender(RenderPassInfo<GeoRenderState> renderPassInfo, BiConsumer<GeoBone, PerBoneRender<GeoRenderState>> consumer) {
            renderPassInfo.model().getBone("Portal").ifPresent(bone -> consumer.accept(bone, this::renderPortal));
        }

        private void renderPortal(RenderPassInfo<GeoRenderState> renderPassInfo, GeoBone bone, SubmitNodeCollector renderTasks) {
            var poseStack = renderPassInfo.poseStack();

            poseStack.translate(-2 / 16f, -2 / 16f, -2 / 16f);
            poseStack.scale(4 / 16f, 4 / 16f, 4 / 16f);

            AbstractEndPortalRenderer.submitSpecial(RenderTypes.endPortal(), poseStack, renderTasks);
        }
    }
}
