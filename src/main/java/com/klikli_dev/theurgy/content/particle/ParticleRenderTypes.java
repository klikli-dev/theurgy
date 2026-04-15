// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT


package com.klikli_dev.theurgy.content.particle;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.particle.SingleQuadParticle;

public class ParticleRenderTypes {

    /**
     * Creates a custom particle layer with depth writing disabled.
     * This prevents z-fighting when multiple translucent particles spawn at the same position.
     */
    private static SingleQuadParticle.Layer createNoDepthWriteLayer(boolean translucent) {
        RenderPipeline noDepthPipeline = RenderPipeline.builder()
                .withLocation("theurgy:particle_" + (translucent ? "translucent" : "opaque") + "_no_depth")
                .withVertexShader("core/particle")
                .withFragmentShader("core/particle")
                .withSampler("Sampler0")
                .withSampler("Sampler2")
                .withVertexFormat(DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS)
                .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
                .build();

        return new SingleQuadParticle.Layer(translucent, TextureAtlas.LOCATION_PARTICLES, noDepthPipeline);
    }

    public static final SingleQuadParticle.Layer EMBER_RENDER = SingleQuadParticle.Layer.TRANSLUCENT;
    public static final SingleQuadParticle.Layer EMBER_RENDER_NO_DEPTH = createNoDepthWriteLayer(true);
}
