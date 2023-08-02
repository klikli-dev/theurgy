// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT


package com.klikli_dev.theurgy.content.particle.glow;

import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.registry.ParticleRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;

public class GlowParticleProvider implements ParticleProvider<GlowParticleOptions> {
    private final SpriteSet spriteSet;

    public GlowParticleProvider(SpriteSet sprite) {
        this.spriteSet = sprite;
    }

    public static ParticleOptions createOptions(ParticleColor color) {
        return new GlowParticleOptions(ParticleRegistry.GLOW_TYPE.get(), color, false);
    }

    public static ParticleOptions createOptions(ParticleColor color, boolean disableDepthTest) {
        return new GlowParticleOptions(ParticleRegistry.GLOW_TYPE.get(), color, disableDepthTest, 0.25f, 0.75f, 36);
    }

    public static ParticleOptions createOptions(ParticleColor color, boolean disableDepthTest, float size, float alpha, int age) {
        return new GlowParticleOptions(color, disableDepthTest, size, alpha, age);
    }

    public static ParticleOptions createOptions(ParticleColor color, float size, float alpha, int age) {
        return new GlowParticleOptions(color, false, size, alpha, age);
    }

    @Override
    public Particle createParticle(GlowParticleOptions data, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new GlowParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, data.color.getRed(), data.color.getGreen(), data.color.getBlue(), data.alpha, data.size, data.age, this.spriteSet, data.disableDepthTest);
    }
}