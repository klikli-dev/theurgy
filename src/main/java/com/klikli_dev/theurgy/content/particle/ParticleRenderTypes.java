// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT


package com.klikli_dev.theurgy.content.particle;

import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class ParticleRenderTypes {

    public static final SingleQuadParticle.Layer EMBER_RENDER = SingleQuadParticle.Layer.TRANSLUCENT;
    //TODO: port to MC 26.2 - RenderPipeline builder API changed (withUniform/withSampler removed)
    //Use vanilla translucent for now; the no-depth-write custom pipeline needs to be rebuilt with BindGroupLayout API
    public static final SingleQuadParticle.Layer EMBER_RENDER_NO_DEPTH = SingleQuadParticle.Layer.TRANSLUCENT;
}
