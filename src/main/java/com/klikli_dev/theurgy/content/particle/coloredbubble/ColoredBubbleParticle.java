/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */


package com.klikli_dev.theurgy.content.particle.coloredbubble;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Math;

@OnlyIn(Dist.CLIENT)
public class ColoredBubbleParticle extends TextureSheetParticle {

    private final SpriteSet sprite;
    public float colorR;
    public float colorG;
    public float colorB;


    public ColoredBubbleParticle(ClientLevel worldIn, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, SpriteSet sprite) {
        super(worldIn, x, y, z, 0, 0, 0);
        this.hasPhysics = false;

        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
        if (this.colorR > 1.0) {
            this.colorR = this.colorR / 255.0f;
        }
        if (this.colorG > 1.0) {
            this.colorG = this.colorG / 255.0f;
        }
        if (this.colorB > 1.0) {
            this.colorB = this.colorB / 255.0f;
        }
        this.setColor(this.colorR, this.colorG, this.colorB);

        this.lifetime = 10;

        this.quadSize = 0.05f;

        this.xd = vx;
        this.yd = vy;
        this.zd = vz;

        this.sprite = sprite;
        this.pickSprite(this.sprite);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }


    @Override
    public void tick() {
        super.tick();
        float progress = (float) this.age / this.lifetime;
        //change particle based on age progress
        this.setAlpha(Math.lerp(progress, 1.0f, 0.75f));

        //slow down y motion over time
        this.yd *= 0.75f;

        //select new visual based on updated age
        this.setSpriteFromAge(this.sprite);
    }

    @Override
    public boolean isAlive() {
        return this.age < this.lifetime;
    }
}