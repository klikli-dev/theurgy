/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.renderer;

import com.klikli_dev.theurgy.content.block.incubator.IncubatorSaltVesselBlockEntity;
import com.klikli_dev.theurgy.content.block.incubator.IncubatorSulfurVesselBlockEntity;
import com.klikli_dev.theurgy.content.renderer.model.IncubatorSaltVesselModel;
import com.klikli_dev.theurgy.content.renderer.model.IncubatorSulfurVesselModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class IncubatorSaltVesselRenderer extends GeoBlockRenderer<IncubatorSaltVesselBlockEntity> {
    public IncubatorSaltVesselRenderer(BlockEntityRendererProvider.Context pContext) {
        super(new IncubatorSaltVesselModel());
    }
}
