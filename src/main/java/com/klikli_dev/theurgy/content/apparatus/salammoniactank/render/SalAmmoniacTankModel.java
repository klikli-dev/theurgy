// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniactank.render;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class SalAmmoniacTankModel<T extends GeoAnimatable> extends GeoModel<T> {
    @Override
    public @NonNull Identifier getModelResource(@NonNull GeoRenderState renderState) {
        return Theurgy.loc("block/sal_ammoniac_tank");
    }

    @Override
    public @NonNull Identifier getTextureResource(@NonNull GeoRenderState renderState) {
        return Theurgy.loc("textures/block/sal_ammoniac_tank.png");
    }

    @Override
    public @NonNull Identifier getAnimationResource(T animatable) {
        return Theurgy.loc("");
    }
}
