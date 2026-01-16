// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT


package com.klikli_dev.theurgy.content.particle;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.render.RenderTypes;
import net.minecraft.client.particle.ParticleRenderType;

public class ParticleRenderTypes {
    public static final ParticleRenderType EMBER_RENDER = new ParticleRenderType(Theurgy.MODID + ":em_rend", RenderTypes.EMBER);
    public static final ParticleRenderType EMBER_RENDER_NO_MASK = new ParticleRenderType(Theurgy.MODID + ":em_rend_no_mask", RenderTypes.EMBER_NO_MASK);
}
