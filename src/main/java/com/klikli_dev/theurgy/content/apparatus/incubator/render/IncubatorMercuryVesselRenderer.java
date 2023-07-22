/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.apparatus.incubator.render;

import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorMercuryVesselBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class IncubatorMercuryVesselRenderer extends GeoBlockRenderer<IncubatorMercuryVesselBlockEntity> {
    public IncubatorMercuryVesselRenderer(BlockEntityRendererProvider.Context pContext) {
        super(new IncubatorMercuryVesselModel());
    }
}
