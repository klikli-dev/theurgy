/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.apparatus.incubator.render;

import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorSaltVesselBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class IncubatorSaltVesselRenderer extends GeoBlockRenderer<IncubatorSaltVesselBlockEntity> {
    public IncubatorSaltVesselRenderer(BlockEntityRendererProvider.Context pContext) {
        super(new IncubatorSaltVesselModel());
    }
}
