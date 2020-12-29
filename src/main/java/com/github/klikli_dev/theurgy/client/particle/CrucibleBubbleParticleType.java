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

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleType;

import javax.annotation.Nullable;

public class CrucibleBubbleParticleType extends ParticleType<CrucibleBubbleParticleData> {
    //region Initialization
    public CrucibleBubbleParticleType() {
        super(false, CrucibleBubbleParticleData.DESERIALIZER);
    }
    //endregion Initialization

    //region Overrides
    @Override
    public Codec<CrucibleBubbleParticleData> func_230522_e_() {
        return CrucibleBubbleParticleData.codecFor(this);
    }
    //endregion Overrides

    public static class Factory implements IParticleFactory<CrucibleBubbleParticleData> {
        //region Fields
        private final IAnimatedSprite spriteSet;
        //endregion Fields

        //region Initialization
        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }
        //endregion Initialization

        //region Overrides
        @Nullable
        @Override
        public Particle makeParticle(CrucibleBubbleParticleData data, ClientWorld worldIn, double x, double y, double z,
                                     double xSpeed, double ySpeed, double zSpeed) {
            CrucibleBubbleParticle particle =
                    new CrucibleBubbleParticle(this.spriteSet, data, worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.selectSpriteWithAge(this.spriteSet);
            return particle;
        }
        //endregion Overrides
    }
}