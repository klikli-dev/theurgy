// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT


package com.klikli_dev.theurgy.content.particle;

import com.klikli_dev.theurgy.Theurgy;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;

public class ParticleRenderTypes {

    public static final SingleQuadParticle.Layer EMBER_RENDER = SingleQuadParticle.Layer.TRANSLUCENT;
    public static final SingleQuadParticle.Layer EMBER_RENDER_NO_DEPTH = createNoDepthWriteLayer();

    /**
     * Creates a custom particle layer with depth writing disabled.
     * This prevents z-fighting when multiple translucent particles spawn at the same position.
     * Based on TRANSLUCENT_PARTICLE but with depth writes disabled.
     */
    private static SingleQuadParticle.Layer createNoDepthWriteLayer() {
        // Replicate MATRICES_FOG_SNIPPET which is private in RenderPipelines
        RenderPipeline.Snippet matricesFogSnippet = RenderPipeline.builder()
                .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
                .withUniform("Projection", UniformType.UNIFORM_BUFFER)
                .withUniform("Fog", UniformType.UNIFORM_BUFFER)
                .buildSnippet();

        RenderPipeline noDepthPipeline = RenderPipeline.builder(matricesFogSnippet)
                .withLocation(Theurgy.loc("particle_translucent_no_depth"))
                .withVertexShader("core/particle")
                .withFragmentShader("core/particle")
                .withSampler("Sampler0")
                .withSampler("Sampler2")
                .withVertexFormat(DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS)
                .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
                .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                .build();

        return new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, noDepthPipeline);
    }
}
