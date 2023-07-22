/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */


package com.klikli_dev.theurgy.content.particle.coloredbubble;

import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.registry.ParticleRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;

public class ColoredBubbleParticleProvider implements ParticleProvider<ColoredBubbleParticleOptions> {
    private final SpriteSet spriteSet;

    public ColoredBubbleParticleProvider(SpriteSet sprite) {
        this.spriteSet = sprite;
    }

    public static ParticleOptions createOptions(ParticleColor color) {
        return new ColoredBubbleParticleOptions(ParticleRegistry.COLORED_BUBBLE_TYPE.get(), color);
    }

    @Override
    public Particle createParticle(ColoredBubbleParticleOptions data, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new ColoredBubbleParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, data.color.getRed(), data.color.getGreen(), data.color.getBlue(), this.spriteSet);
    }
}