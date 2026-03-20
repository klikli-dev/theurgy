// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator.render;

import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSulfurVesselBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import com.geckolib.renderer.GeoBlockRenderer;

public class IncubatorSulfurVesselRenderer extends GeoBlockRenderer<IncubatorSulfurVesselBlockEntity, BlockEntityRenderState> {
    public IncubatorSulfurVesselRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext, new IncubatorSulfurVesselModel());
    }
}
