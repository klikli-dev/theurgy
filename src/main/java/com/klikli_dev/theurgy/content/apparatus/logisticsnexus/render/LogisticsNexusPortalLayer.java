// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsnexus.render;

import com.geckolib.cache.model.GeoBone;
import com.geckolib.renderer.GeoBlockRenderer;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.base.PerBoneRender;
import com.geckolib.renderer.layer.GeoRenderLayer;
import com.klikli_dev.theurgy.content.apparatus.logisticsnexus.LogisticsNexusBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import java.util.function.BiConsumer;

public class LogisticsNexusPortalLayer extends GeoRenderLayer<LogisticsNexusBlockEntity, Void, LogisticsNexusRenderer.RenderState> {
    public LogisticsNexusPortalLayer(GeoBlockRenderer<LogisticsNexusBlockEntity, LogisticsNexusRenderer.RenderState> renderer) {
        super(renderer);
    }

    @Override
    public void addPerBoneRender(RenderPassInfo<LogisticsNexusRenderer.RenderState> renderPassInfo, BiConsumer<GeoBone, PerBoneRender<LogisticsNexusRenderer.RenderState>> consumer) {
        renderPassInfo.model().getBone("Portal").ifPresent(bone -> consumer.accept(bone, this::renderPortal));
    }

    private void renderPortal(RenderPassInfo<LogisticsNexusRenderer.RenderState> renderPassInfo, GeoBone bone, SubmitNodeCollector renderTasks) {
        var poseStack = renderPassInfo.poseStack();

        poseStack.translate(-2 / 16f, -2 / 16f, -2 / 16f);
        poseStack.scale(4 / 16f, 4 / 16f, 4 / 16f);

        AbstractEndPortalRenderer.submitSpecial(RenderTypes.endPortal(), poseStack, renderTasks);
    }
}
