/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.renderer.model;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.block.distiller.DistillerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DistillerModel extends GeoModel<DistillerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(DistillerBlockEntity animatable) {
        return Theurgy.loc("geo/distiller.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DistillerBlockEntity animatable) {
        return Theurgy.loc("textures/block/distiller.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DistillerBlockEntity animatable) {
        return Theurgy.loc("animations/distiller.animation.json");
    }
}
