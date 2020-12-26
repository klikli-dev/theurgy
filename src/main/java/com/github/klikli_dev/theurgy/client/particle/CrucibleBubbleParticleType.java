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