/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;

public class GlowingBallParticle extends SpriteTexturedParticle {

    //region Fields
    protected final IAnimatedSprite sprite;
    protected final float initialScale;
    protected final float initialAlpha;
    //endregion Fields

    //region Initialization
    protected GlowingBallParticle(IAnimatedSprite sprite, GlowingBallParticleData data, ClientWorld world, double x,
                                  double y, double z, double motionX, double motionY,
                                  double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.sprite = sprite;

        this.setColor(data.red, data.green, data.blue);
        this.setMaxAge((int) (data.lifetime * 0.5f));
        this.particleScale = data.scale;

        this.initialScale = data.scale;
        this.motionX = motionX * 2.0f;
        this.motionY = motionY * 2.0f;
        this.motionZ = motionZ * 2.0f;
        this.initialAlpha = data.alpha;
        this.particleAngle = 2.0f * (float) Math.PI;

        this.canCollide = false;
    }
    //endregion Initialization

    //region Overrides
    @Override
    public void tick() {
        super.tick();
        if (this.rand.nextInt(6) == 0) {
            this.age++;
        }
        float coeff = (float) this.age / (float) this.maxAge;
        this.particleScale = this.initialScale - this.initialScale * coeff;
        this.particleAlpha = this.initialAlpha * (1.0f - coeff);
        this.prevParticleAngle = this.particleAngle;
        this.particleAngle += 1.0f;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return PARTICLE_SHEET_ADDITIVE;
    }

    @Override
    protected int getBrightnessForRender(float partialTick) {
        return 255;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    //endregion Overrides

    IParticleRenderType PARTICLE_SHEET_ADDITIVE = new IParticleRenderType() {
        public void beginRender(BufferBuilder bufferBuilder, TextureManager textureManager) {
            RenderSystem.depthMask(true);
            textureManager.bindTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE);
            RenderSystem.disableCull();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE
            );
            RenderSystem.alphaFunc(516, 0.003921569F);
            bufferBuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        }

        public void finishRender(Tessellator tesselator) {
            tesselator.draw();
        }

        public String toString() {
            return "PARTICLE_SHEET_TRANSLUCENT";
        }
    };
}
