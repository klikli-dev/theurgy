// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render;

import com.klikli_dev.theurgy.Theurgy;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderProgram;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;

import java.util.OptionalDouble;
import java.util.function.Function;

public class RenderTypes extends RenderStateShard {

    //public static ShaderProgram rendertypeDistanceLines;
    //protected static final ShaderStateShard RENDERTYPE_DISTANCE_LINES_SHADER = new ShaderStateShard(() -> rendertypeDistanceLines);

    protected static final RenderType DISTANCE_LINES = RenderType.create(
           "distance_lines",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.LINES,
            1536,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_LINES_SHADER)
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setCullState(NO_CULL)
                    .createCompositeState(false)
    );

    protected static final Function<ResourceLocation, RenderType> ENTITY_TRANSLUCENT_CULL_NO_DEPTH = Util.memoize(
            p_286165_ -> {
                RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setDepthTestState(DepthTestStateShard.NO_DEPTH_TEST)
                        .setTextureState(new RenderStateShard.TextureStateShard(p_286165_, TriState.FALSE, false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(true);
                return RenderType.create("entity_translucent_cull_no_depth", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, true, true, rendertype$compositestate);
            }
    );

    public static RenderType entityTranslucentCullNoDepth(ResourceLocation pLocation) {
        return ENTITY_TRANSLUCENT_CULL_NO_DEPTH.apply(pLocation);
    }

    protected static final RenderType TRANSLUCENT_CULL_NO_DEPTH_BLOCK_SHEET = entityTranslucentCullNoDepth(TextureAtlas.LOCATION_BLOCKS);

    public static RenderType translucentCullNoDepthBlockSheet() {
        return TRANSLUCENT_CULL_NO_DEPTH_BLOCK_SHEET;
    }

    protected static final TransparencyStateShard SRC_MINUS_ONE_TRANSPARENCY = new TransparencyStateShard(Theurgy.loc("src_minus_one").toString(),
            () -> {
                RenderSystem.enableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(true);
    });
    private static final RenderType FLUID = RenderType.create(Theurgy.loc("fluid").toString(),
            DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .createCompositeState(true));
    private static final RenderType OUTLINE_SOLID =
            RenderType.create(Theurgy.loc("outline_solid").toString(), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false,
                    false, RenderType.CompositeState.builder()
                            .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)
                            .setTextureState(new TextureStateShard(Theurgy.loc("textures/misc/blank.png"), TriState.FALSE, false))
                            .setCullState(CULL)
                            .setLightmapState(LIGHTMAP)
                            .setOverlayState(OVERLAY)
                            .createCompositeState(false));
    private static final Function<ResourceLocation, RenderType> SRC_MINUS_ONE = Util.memoize(location -> {
        var rendertype = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                .setTextureState(new TextureStateShard(location, TriState.FALSE, false))
                .setTransparencyState(SRC_MINUS_ONE_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(false);

        return RenderType.create(Theurgy.loc("src_minus_one").toString(), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype);
    });

    //unneeded
    private RenderTypes(String pName, Runnable pSetupState, Runnable pClearState) {
        super(pName, pSetupState, pClearState);
    }

    public static RenderType outlineSolid() {
        return OUTLINE_SOLID;
    }

    public static RenderType outlineTranslucent(ResourceLocation texture, boolean cull) {
        return RenderType.create(Theurgy.loc("outline_translucent" + (cull ? "_cull" : "")).toString(),
                DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new TextureStateShard(texture, TriState.FALSE, false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(cull ? CULL : NO_CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .setWriteMaskState(COLOR_WRITE)
                        .createCompositeState(false));
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
