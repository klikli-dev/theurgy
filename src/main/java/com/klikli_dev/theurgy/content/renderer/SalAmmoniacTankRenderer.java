/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.renderer;

import com.klikli_dev.theurgy.content.block.incubator.IncubatorMercuryVesselBlockEntity;
import com.klikli_dev.theurgy.content.block.salammoniactank.SalAmmoniacTankBlockEntity;
import com.klikli_dev.theurgy.content.renderer.model.IncubatorMercuryVesselModel;
import com.klikli_dev.theurgy.content.renderer.model.SalAmmoniacTankModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SalAmmoniacTankRenderer extends GeoBlockRenderer<SalAmmoniacTankBlockEntity> {
    public SalAmmoniacTankRenderer(BlockEntityRendererProvider.Context pContext) {
        super(new SalAmmoniacTankModel());
    }
}
