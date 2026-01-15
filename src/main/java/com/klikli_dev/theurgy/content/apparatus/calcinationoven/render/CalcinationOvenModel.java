// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.calcinationoven.render;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class CalcinationOvenModel extends GeoModel {

    @Override
    public ResourceLocation getModelResource(GeoAnimatable animatable, @Nullable GeoRenderer renderer) {
        return Theurgy.loc("geo/calcination_oven.geo.json");

    }

    @Override
    public ResourceLocation getTextureResource(GeoAnimatable animatable, @Nullable GeoRenderer renderer) {
        return Theurgy.loc("textures/block/calcination_oven.png");

    }

    @Override
    public ResourceLocation getAnimationResource(GeoAnimatable animatable) {
        return Theurgy.loc("animations/calcination_oven.animation.json");
    }
}
