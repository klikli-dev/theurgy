// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.render;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class SalAmmoniacAccumulatorModel extends GeoModel {
    @Override
    public ResourceLocation getModelResource(GeoAnimatable animatable) {
        return Theurgy.loc("geo/sal_ammoniac_accumulator.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeoAnimatable animatable) {
        return Theurgy.loc("textures/block/sal_ammoniac_accumulator.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeoAnimatable animatable) {
        return Theurgy.loc("");
    }
}
