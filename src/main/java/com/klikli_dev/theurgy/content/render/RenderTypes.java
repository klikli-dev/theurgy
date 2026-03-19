// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class RenderTypes {

    private static final ResourceLocation BLANK_TEXTURE = Theurgy.loc("textures/misc/blank.png");

    protected static final RenderType DISTANCE_LINES = RenderType.lines();

    protected static final Function<ResourceLocation, RenderType> ENTITY_TRANSLUCENT_CULL_NO_DEPTH = Util.memoize(RenderType::entityTranslucentCull);

    public static RenderType entityTranslucentCullNoDepth(ResourceLocation pLocation) {
        return ENTITY_TRANSLUCENT_CULL_NO_DEPTH.apply(pLocation);
    }

    protected static final RenderType TRANSLUCENT_CULL_NO_DEPTH_BLOCK_SHEET = entityTranslucentCullNoDepth(TextureAtlas.LOCATION_BLOCKS);

    public static RenderType translucentCullNoDepthBlockSheet() {
        return TRANSLUCENT_CULL_NO_DEPTH_BLOCK_SHEET;
    }

    private static final RenderType FLUID = RenderType.itemEntityTranslucentCull(TextureAtlas.LOCATION_BLOCKS);
    private static final RenderType OUTLINE_SOLID = RenderType.entitySolid(BLANK_TEXTURE);
    private static final Function<ResourceLocation, RenderType> SRC_MINUS_ONE = Util.memoize(RenderType::entityTranslucent);

    private RenderTypes() {
    }

    public static RenderType outlineSolid() {
        return OUTLINE_SOLID;
    }

    public static RenderType outlineTranslucent(ResourceLocation texture, boolean cull) {
        return cull ? RenderType.entityTranslucentCull(texture) : RenderType.entityTranslucent(texture);
    }

    public static RenderType fluid() {
        return FLUID;
    }

    public static RenderType srcMinusOne(ResourceLocation location) {
        return SRC_MINUS_ONE.apply(location);
    }

    public static RenderType distanceLines() {
        return DISTANCE_LINES;
    }
}
