// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.distiller.render;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class DistillerModel extends GeoModel {
    @Override
    public ResourceLocation getModelResource(GeoAnimatable animatable) {
        return Theurgy.loc("geo/distiller.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeoAnimatable animatable) {
        return Theurgy.loc("textures/block/distiller.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeoAnimatable animatable) {
        return Theurgy.loc("animations/distiller.animation.json");
    }
}
