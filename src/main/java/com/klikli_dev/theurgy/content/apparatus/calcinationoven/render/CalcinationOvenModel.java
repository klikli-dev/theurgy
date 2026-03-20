// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.calcinationoven.render;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class CalcinationOvenModel<T extends GeoAnimatable> extends GeoModel<T> {

    @Override
    public ResourceLocation getModelResource(GeoRenderState renderState) {
        return Theurgy.loc("geo/calcination_oven.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState renderState) {
        return Theurgy.loc("textures/block/calcination_oven.png");
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return Theurgy.loc("animations/calcination_oven.animation.json");
    }
}
