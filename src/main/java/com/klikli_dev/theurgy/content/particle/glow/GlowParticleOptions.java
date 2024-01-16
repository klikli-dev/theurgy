// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.particle.glow;

import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.registry.ParticleRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;


/**
 * Simplified version of <a href="https://github.com/Sirttas/ElementalCraft/blob/b91ca42b3d139904d9754d882a595406bad1bd18/src/main/java/sirttas/elementalcraft/particle/ElementTypeParticleData.java">ElementalCraft</a>
 */

public class GlowParticleOptions implements ParticleOptions {

    public static final Codec<GlowParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(d -> d.color.getRed()),
                    Codec.FLOAT.fieldOf("g").forGetter(d -> d.color.getGreen()),
                    Codec.FLOAT.fieldOf("b").forGetter(d -> d.color.getBlue()),
                    Codec.BOOL.fieldOf("disableDepthTest").forGetter(d -> d.disableDepthTest),
                    Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
                    Codec.FLOAT.fieldOf("alpha").forGetter(d -> d.alpha),
                    Codec.INT.fieldOf("age").forGetter(d -> d.age)
            )
            .apply(instance, GlowParticleOptions::new));

    @SuppressWarnings("deprecation")
    public static final Deserializer<GlowParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public GlowParticleOptions fromCommand(ParticleType<GlowParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new GlowParticleOptions(type, ParticleColor.fromString(reader.readString()), reader.readBoolean());
        }

        @Override
        public GlowParticleOptions fromNetwork(ParticleType<GlowParticleOptions> type, FriendlyByteBuf buffer) {
            return new GlowParticleOptions(type, ParticleColor.deserialize(buffer.readNbt()), buffer.readBoolean());
        }
    };
    private final ParticleType<GlowParticleOptions> type;
    public ParticleColor color;
    public boolean disableDepthTest;
    public float size = .25f;
    public float alpha = 1.0f;
    public int age = 36;

    public GlowParticleOptions(float r, float g, float b, boolean disableDepthTest, float size, float alpha, int age) {
        this(ParticleRegistry.GLOW_TYPE.get(), new ParticleColor(r, g, b), disableDepthTest, size, alpha, age);
    }

    public GlowParticleOptions(ParticleColor color, boolean disableDepthTest, float size, float alpha, int age) {
        this(ParticleRegistry.GLOW_TYPE.get(), color, disableDepthTest, size, alpha, age);
    }

    public GlowParticleOptions(ParticleType<GlowParticleOptions> particleTypeData, ParticleColor color, boolean disableDepthTest) {
        this(particleTypeData, color, disableDepthTest, 0.25f, 1.0f, 36);
    }

    public GlowParticleOptions(ParticleType<GlowParticleOptions> particleTypeData, ParticleColor color, boolean disableDepthTest, float size, float alpha, int age) {
        this.type = particleTypeData;
        this.color = color;
        this.disableDepthTest = disableDepthTest;
        this.size = size;
        this.alpha = alpha;
        this.age = age;
    }


    @Override
    public ParticleType<GlowParticleOptions> getType() {
        return this.type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeNbt(this.color.serialize());
    }

    @Override
    public String writeToString() {
        return BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()) + " " + this.color.serialize();
    }
}
