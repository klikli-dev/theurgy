// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator.render;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.Identifier;

public class IncubatorSaltVesselModel<T extends GeoAnimatable> extends GeoModel<T> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Theurgy.loc("geo/incubator_vessel.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Theurgy.loc("textures/block/incubator_vessel_bronze.png");
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        return Theurgy.loc("");
    }
}
