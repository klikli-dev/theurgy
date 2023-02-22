/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.particle;

import com.klikli_dev.theurgy.registry.ParticleRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;


/**
 * Simplified version of <a href="https://github.com/Sirttas/ElementalCraft/blob/b91ca42b3d139904d9754d882a595406bad1bd18/src/main/java/sirttas/elementalcraft/particle/ElementTypeParticleData.java">ElementalCraft</a>
 */

public class ColorParticleTypeData implements ParticleOptions {

    public static final Codec<ColorParticleTypeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(d -> d.color.getRed()),
                    Codec.FLOAT.fieldOf("g").forGetter(d -> d.color.getGreen()),
                    Codec.FLOAT.fieldOf("b").forGetter(d -> d.color.getBlue()),
                    Codec.BOOL.fieldOf("disableDepthTest").forGetter(d -> d.disableDepthTest),
                    Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
                    Codec.FLOAT.fieldOf("alpha").forGetter(d -> d.alpha),
                    Codec.INT.fieldOf("age").forGetter(d -> d.age)
            )
            .apply(instance, ColorParticleTypeData::new));
    static final Deserializer<ColorParticleTypeData> DESERIALIZER = new Deserializer<>() {
        @Override
        public ColorParticleTypeData fromCommand(ParticleType<ColorParticleTypeData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new ColorParticleTypeData(type, ParticleColor.fromString(reader.readString()), reader.readBoolean());
        }

        @Override
        public ColorParticleTypeData fromNetwork(ParticleType<ColorParticleTypeData> type, FriendlyByteBuf buffer) {
            return new ColorParticleTypeData(type, ParticleColor.deserialize(buffer.readNbt()), buffer.readBoolean());
        }
    };
    private final ParticleType<ColorParticleTypeData> type;
    public ParticleColor color;
    public boolean disableDepthTest;
    public float size = .25f;
    public float alpha = 1.0f;
    public int age = 36;

    public ColorParticleTypeData(float r, float g, float b, boolean disableDepthTest, float size, float alpha, int age) {
        this(ParticleRegistry.GLOW_TYPE.get(), new ParticleColor(r, g, b), disableDepthTest, size, alpha, age);
    }

    public ColorParticleTypeData(ParticleColor color, boolean disableDepthTest, float size, float alpha, int age) {
        this(ParticleRegistry.GLOW_TYPE.get(), color, disableDepthTest, size, alpha, age);
    }

    public ColorParticleTypeData(ParticleType<ColorParticleTypeData> particleTypeData, ParticleColor color, boolean disableDepthTest) {
        this(particleTypeData, color, disableDepthTest, 0.25f, 1.0f, 36);
    }

    public ColorParticleTypeData(ParticleType<ColorParticleTypeData> particleTypeData, ParticleColor color, boolean disableDepthTest, float size, float alpha, int age) {
        this.type = particleTypeData;
        this.color = color;
        this.disableDepthTest = disableDepthTest;
        this.size = size;
        this.alpha = alpha;
        this.age = age;
    }


    @Override
    public ParticleType<ColorParticleTypeData> getType() {
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
