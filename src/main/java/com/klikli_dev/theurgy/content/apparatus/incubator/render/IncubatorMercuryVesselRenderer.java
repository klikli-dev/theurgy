// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator.render;

import com.geckolib.renderer.GeoBlockRenderer;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorMercuryVesselBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class IncubatorMercuryVesselRenderer extends GeoBlockRenderer<IncubatorMercuryVesselBlockEntity, BlockEntityRenderState> {
    public IncubatorMercuryVesselRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext, new IncubatorMercuryVesselModel());
    }
}
