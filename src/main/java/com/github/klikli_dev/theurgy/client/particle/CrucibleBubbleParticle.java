/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
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

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CrucibleBubbleParticle extends SpriteTexturedParticle {

    //region Fields
    private final IAnimatedSprite spriteSet;
    //endregion Fields

    //region Initialization
    public CrucibleBubbleParticle(IAnimatedSprite spriteSet, CrucibleBubbleParticleData data, ClientWorld world,
                                  double x, double y, double z,
                                  double motionX, double motionY, double motionZ) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.particleScale = 0.05f;
        this.maxAge = 10;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;

        this.setColor(data.red, data.green, data.blue);
    }
    //endregion Initialization

    //region Overrides
    @Override
    public void tick() {
        super.tick();
        float progress = (float) this.age / this.maxAge;
        //change particle based on age progress
        this.setAlphaF(MathHelper.lerp(progress, 1.0f, 0.75f));

        //slow down y motion over time
        this.motionY *= 0.75f;
        //select new visual based on updated age
        this.selectSpriteWithAge(this.spriteSet);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    //endregion Overrides
}
