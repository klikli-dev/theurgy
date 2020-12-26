package com.github.klikli_dev.theurgy.client.particle;

import net.minecraft.client.particle.*;
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
    public CrucibleBubbleParticle(IAnimatedSprite spriteSet, CrucibleBubbleParticleData data, ClientWorld world, double x, double y, double z,
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
        float progress = (float)this.age / this.maxAge;
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
