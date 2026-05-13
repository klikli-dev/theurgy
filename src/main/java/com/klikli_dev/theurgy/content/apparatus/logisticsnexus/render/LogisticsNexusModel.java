// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsnexus.render;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class LogisticsNexusModel<T extends GeoAnimatable> extends GeoModel<T> {
    @Override
    public @NonNull Identifier getModelResource(@NonNull GeoRenderState renderState) {
        return Theurgy.loc("block/logistics_nexus");
    }

    @Override
    public @NonNull Identifier getTextureResource(@NonNull GeoRenderState renderState) {
        return Theurgy.loc("textures/block/logistics_nexus.png");
    }

    @Override
    public @NonNull Identifier getAnimationResource(T animatable) {
        return Theurgy.loc("block/logistics_nexus");
    }
}
