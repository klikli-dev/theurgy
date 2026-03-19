// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.render;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class SalAmmoniacAccumulatorModel<T extends GeoAnimatable> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(GeoRenderState renderState) {
        return Theurgy.loc("geo/sal_ammoniac_accumulator.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState renderState) {
        return Theurgy.loc("textures/block/sal_ammoniac_accumulator.png");
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return Theurgy.loc("");
    }
}
