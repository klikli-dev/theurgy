// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.calcinationoven.render;

import com.geckolib.renderer.GeoBlockRenderer;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class CalcinationOvenRenderer extends GeoBlockRenderer<CalcinationOvenBlockEntity, BlockEntityRenderState> {
    public CalcinationOvenRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext, new CalcinationOvenModel());
    }
}
