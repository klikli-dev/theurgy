// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.particle.coloredbubble;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ColoredBubbleParticleType extends ParticleType<ColoredBubbleParticleOptions> {
    public ColoredBubbleParticleType() {
        super(false);
    }

    @Override
    public MapCodec<ColoredBubbleParticleOptions> codec() {
        return ColoredBubbleParticleOptions.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ColoredBubbleParticleOptions> streamCodec() {
        return null;
    }
}
