// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.particle.coloredbubble;

import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.registry.ParticleRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.registries.ForgeRegistries;

public class ColoredBubbleParticleOptions implements ParticleOptions {

    public static final Codec<ColoredBubbleParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(d -> d.color.getRed()),
                    Codec.FLOAT.fieldOf("g").forGetter(d -> d.color.getGreen()),
                    Codec.FLOAT.fieldOf("b").forGetter(d -> d.color.getBlue())
            )
            .apply(instance, ColoredBubbleParticleOptions::new));

    @SuppressWarnings("deprecation")
    public static final Deserializer<ColoredBubbleParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public ColoredBubbleParticleOptions fromCommand(ParticleType<ColoredBubbleParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new ColoredBubbleParticleOptions(type, ParticleColor.fromString(reader.readString()));
        }

        @Override
        public ColoredBubbleParticleOptions fromNetwork(ParticleType<ColoredBubbleParticleOptions> type, FriendlyByteBuf buffer) {
            return new ColoredBubbleParticleOptions(type, ParticleColor.deserialize(buffer.readNbt()));
        }
    };
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

    @Override
    public void writeToNetwork(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeNbt(this.color.serialize());
    }

    @Override
    public String writeToString() {
        return ForgeRegistries.PARTICLE_TYPES.getKey(this.type) + " " + this.color.serialize();
    }
}