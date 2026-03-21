// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public class RenderTypes {

    protected static final RenderType DISTANCE_LINES = net.minecraft.client.renderer.rendertype.RenderTypes.lines();
    protected static final Function<Identifier, RenderType> ENTITY_TRANSLUCENT_CULL_NO_DEPTH = Util.memoize((Identifier texture) -> net.minecraft.client.renderer.rendertype.RenderTypes.entityTranslucent(texture));
    protected static final RenderType TRANSLUCENT_CULL_NO_DEPTH_BLOCK_SHEET = entityTranslucentCullNoDepth(TextureAtlas.LOCATION_BLOCKS);
    private static final Identifier BLANK_TEXTURE = Theurgy.loc("textures/misc/blank.png");
    private static final RenderType FLUID = net.minecraft.client.renderer.rendertype.RenderTypes.itemTranslucent(TextureAtlas.LOCATION_BLOCKS);
    private static final RenderType OUTLINE_SOLID = net.minecraft.client.renderer.rendertype.RenderTypes.entitySolid(BLANK_TEXTURE);
    private static final Function<Identifier, RenderType> SRC_MINUS_ONE = Util.memoize((Identifier texture) -> net.minecraft.client.renderer.rendertype.RenderTypes.entityTranslucent(texture));
    private RenderTypes() {
    }

    public static RenderType entityTranslucentCullNoDepth(Identifier pLocation) {
        return ENTITY_TRANSLUCENT_CULL_NO_DEPTH.apply(pLocation);
    }

    public static RenderType translucentCullNoDepthBlockSheet() {
        return TRANSLUCENT_CULL_NO_DEPTH_BLOCK_SHEET;
    }

    public static RenderType outlineSolid() {
        return OUTLINE_SOLID;
    }

    public static RenderType outlineTranslucent(Identifier texture, boolean cull) {
        return net.minecraft.client.renderer.rendertype.RenderTypes.entityTranslucent(texture);
    }

    public static RenderType fluid() {
        return FLUID;
    }

    public static RenderType srcMinusOne(Identifier location) {
        return SRC_MINUS_ONE.apply(location);
    }

    public static RenderType distanceLines() {
        return DISTANCE_LINES;
    }
}
