// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator.render;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class IncubatorSulfurVesselModel<T extends GeoAnimatable> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(GeoRenderState renderState) {
        return Theurgy.loc("geo/incubator_vessel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState renderState) {
        return Theurgy.loc("textures/block/incubator_vessel_iron.png");
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return Theurgy.loc("");
    }
}
