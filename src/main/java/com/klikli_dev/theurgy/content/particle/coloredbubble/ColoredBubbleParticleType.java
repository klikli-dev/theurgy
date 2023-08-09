// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.particle.coloredbubble;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class ColoredBubbleParticleType extends ParticleType<ColoredBubbleParticleOptions> {
    public ColoredBubbleParticleType() {
        super(false, ColoredBubbleParticleOptions.DESERIALIZER);
    }

    @Override
    public Codec<ColoredBubbleParticleOptions> codec() {
        return ColoredBubbleParticleOptions.CODEC;
    }
}
