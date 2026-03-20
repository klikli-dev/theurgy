// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.render;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.Identifier;
import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;

public class SalAmmoniacAccumulatorModel<T extends GeoAnimatable> extends GeoModel<T> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Theurgy.loc("geo/sal_ammoniac_accumulator.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Theurgy.loc("textures/block/sal_ammoniac_accumulator.png");
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        return Theurgy.loc("");
    }
}
