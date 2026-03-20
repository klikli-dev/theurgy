// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator.render;

import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSaltVesselBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import com.geckolib.renderer.GeoBlockRenderer;

public class IncubatorSaltVesselRenderer extends GeoBlockRenderer<IncubatorSaltVesselBlockEntity, BlockEntityRenderState> {
    public IncubatorSaltVesselRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext, new IncubatorSaltVesselModel());
    }
}
