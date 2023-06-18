/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.particle.glow;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class GlowParticleType extends ParticleType<GlowParticleOptions> {
    public GlowParticleType() {
        super(false, GlowParticleOptions.DESERIALIZER);
    }

    @Override
    public Codec<GlowParticleOptions> codec() {
        return GlowParticleOptions.CODEC;
    }
}
