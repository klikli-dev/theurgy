// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator.render;

import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSulfurVesselBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class IncubatorSulfurVesselRenderer extends GeoBlockRenderer<IncubatorSulfurVesselBlockEntity> {
    public IncubatorSulfurVesselRenderer(BlockEntityRendererProvider.Context pContext) {
        super(new IncubatorSulfurVesselModel());
    }
}
