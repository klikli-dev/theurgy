// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.particle.glow;

import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.registry.ParticleRegistry;
import com.klikli_dev.theurgy.util.TheurgyExtraStreamCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;


/**
 * Simplified version of <a href="https://github.com/Sirttas/ElementalCraft/blob/b91ca42b3d139904d9754d882a595406bad1bd18/src/main/java/sirttas/elementalcraft/particle/ElementTypeParticleData.java">ElementalCraft</a>
 */

public class GlowParticleOptions implements ParticleOptions {

    public static final MapCodec<GlowParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(d -> d.color.getRed()),
                    Codec.FLOAT.fieldOf("g").forGetter(d -> d.color.getGreen()),
                    Codec.FLOAT.fieldOf("b").forGetter(d -> d.color.getBlue()),
                    Codec.BOOL.fieldOf("disableDepthTest").forGetter(d -> d.disableDepthTest),
                    Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
                    Codec.FLOAT.fieldOf("alpha").forGetter(d -> d.alpha),
                    Codec.INT.fieldOf("age").forGetter(d -> d.age)
            )
            .apply(instance, GlowParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GlowParticleOptions> STREAM_CODEC = TheurgyExtraStreamCodecs.composite(
            ByteBufCodecs.FLOAT,
            r -> r.color.getRed(),
            ByteBufCodecs.FLOAT,
            r -> r.color.getGreen(),
            ByteBufCodecs.FLOAT,
            r -> r.color.getBlue(),
            ByteBufCodecs.BOOL,
            r -> r.disableDepthTest,
            ByteBufCodecs.FLOAT,
            r -> r.size,
            ByteBufCodecs.FLOAT,
            r -> r.alpha,
            ByteBufCodecs.INT,
            r -> r.age,
            GlowParticleOptions::new
    );

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
}
