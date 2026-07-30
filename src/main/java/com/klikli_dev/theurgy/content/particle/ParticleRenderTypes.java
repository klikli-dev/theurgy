// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT


package com.klikli_dev.theurgy.content.particle;

import com.klikli_dev.theurgy.Theurgy;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;

public class ParticleRenderTypes {

    public static final SingleQuadParticle.Layer EMBER_RENDER = SingleQuadParticle.Layer.TRANSLUCENT;
    public static final SingleQuadParticle.Layer EMBER_RENDER_NO_DEPTH = createNoDepthWriteLayer();

    private static final RenderPipeline NO_DEPTH_PIPELINE = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
            .withLocation(Theurgy.loc("pipeline/particle_translucent_no_depth"))
            .withVertexShader(Identifier.withDefaultNamespace("core/particle"))
            .withFragmentShader(Identifier.withDefaultNamespace("core/particle"))
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER2)
            .withVertexBinding(0, DefaultVertexFormat.PARTICLE)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .build();

    private static SingleQuadParticle.Layer createNoDepthWriteLayer() {
        return new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, NO_DEPTH_PIPELINE);
    }
}
