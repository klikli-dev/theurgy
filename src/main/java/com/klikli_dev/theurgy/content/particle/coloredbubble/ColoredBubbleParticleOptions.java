// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.particle.coloredbubble;

import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.registry.ParticleRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class ColoredBubbleParticleOptions implements ParticleOptions {

    public static final MapCodec<ColoredBubbleParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(d -> d.color.getRed()),
                    Codec.FLOAT.fieldOf("g").forGetter(d -> d.color.getGreen()),
                    Codec.FLOAT.fieldOf("b").forGetter(d -> d.color.getBlue())
            )
            .apply(instance, ColoredBubbleParticleOptions::new));


    public static final StreamCodec<RegistryFriendlyByteBuf, ColoredBubbleParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            r -> r.color.getRed(),
            ByteBufCodecs.FLOAT,
            r -> r.color.getGreen(),
            ByteBufCodecs.FLOAT,
            r -> r.color.getBlue(),
            ColoredBubbleParticleOptions::new
    );


    private final ParticleType<ColoredBubbleParticleOptions> type;
    public ParticleColor color;

    public ColoredBubbleParticleOptions(float r, float g, float b) {
        this(ParticleRegistry.COLORED_BUBBLE_TYPE.get(), new ParticleColor(r, g, b));
    }

    public ColoredBubbleParticleOptions(ParticleType<ColoredBubbleParticleOptions> particleTypeData, ParticleColor color) {
        this.type = particleTypeData;
        this.color = color;
    }

    @Override
    public ParticleType<ColoredBubbleParticleOptions> getType() {
        return this.type;
    }
}