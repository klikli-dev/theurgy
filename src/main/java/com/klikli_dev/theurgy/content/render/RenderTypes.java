// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render;

import com.klikli_dev.theurgy.Theurgy;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public class RenderTypes {

    /**
     * Custom render pipeline for distance-scaled line rendering.
     * Mirrors the vanilla LINES pipeline but uses custom shaders that scale
     * line width based on camera distance so wires shrink with distance.
     */
    public static final RenderPipeline DISTANCE_LINES_PIPELINE = RenderPipelines.LINES.toBuilder()
            .withLocation(Theurgy.loc("pipeline/distance_lines"))
            .withVertexShader(Theurgy.loc("rendertype_distance_lines"))
            .withFragmentShader(Theurgy.loc("rendertype_distance_lines"))
            .build();
    protected static final Function<Identifier, RenderType> ENTITY_TRANSLUCENT_CULL_NO_DEPTH = Util.memoize((Identifier texture) -> net.minecraft.client.renderer.rendertype.RenderTypes.entityTranslucent(texture));
    protected static final Function<Identifier, RenderType> PARTICLE_TRANSLUCENT = Util.memoize((Identifier texture) -> net.minecraft.client.renderer.rendertype.RenderTypes.entityTranslucent(texture, true));
    protected static final RenderType TRANSLUCENT_CULL_NO_DEPTH_BLOCK_SHEET = entityTranslucentCullNoDepth(TextureAtlas.LOCATION_BLOCKS);
    private static final RenderType DISTANCE_LINES = RenderType.create(
            "theurgy_distance_lines",
            RenderSetup.builder(DISTANCE_LINES_PIPELINE)
                    .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                    .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                    .createRenderSetup());
    private static final Identifier BLANK_TEXTURE = Theurgy.loc("textures/misc/blank.png");
    private static final RenderType FLUID = net.minecraft.client.renderer.rendertype.RenderTypes.itemTranslucent(TextureAtlas.LOCATION_BLOCKS);
    private static final RenderType OUTLINE_SOLID = net.minecraft.client.renderer.rendertype.RenderTypes.entitySolid(BLANK_TEXTURE);
    private static final Function<Identifier, RenderType> SRC_MINUS_ONE = Util.memoize((Identifier texture) -> net.minecraft.client.renderer.rendertype.RenderTypes.entityTranslucent(texture));

    private RenderTypes() {
    }

    public static RenderType entityTranslucentCullNoDepth(Identifier pLocation) {
        return ENTITY_TRANSLUCENT_CULL_NO_DEPTH.apply(pLocation);
    }

    public static RenderType particleTranslucent(Identifier pLocation) {
        return PARTICLE_TRANSLUCENT.apply(pLocation);
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
