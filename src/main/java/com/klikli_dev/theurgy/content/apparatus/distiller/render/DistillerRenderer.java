// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.distiller.render;

import com.klikli_dev.theurgy.content.apparatus.distiller.DistillerBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import com.geckolib.renderer.GeoBlockRenderer;

public class DistillerRenderer extends GeoBlockRenderer<DistillerBlockEntity, BlockEntityRenderState> {
    public DistillerRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext, new DistillerModel());
    }
}
