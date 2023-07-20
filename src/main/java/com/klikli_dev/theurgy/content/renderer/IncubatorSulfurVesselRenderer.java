/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.renderer;

import com.klikli_dev.theurgy.content.block.incubator.IncubatorSulfurVesselBlockEntity;
import com.klikli_dev.theurgy.content.renderer.model.DistillerModel;
import com.klikli_dev.theurgy.content.renderer.model.IncubatorSulfurVesselModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class IncubatorSulfurVesselRenderer extends GeoBlockRenderer<IncubatorSulfurVesselBlockEntity> {
    public IncubatorSulfurVesselRenderer(BlockEntityRendererProvider.Context pContext) {
        super(new IncubatorSulfurVesselModel());
    }
}
