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

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class SparkleParticle extends SpriteTexturedParticle {
    //region Fields
    protected final IAnimatedSprite sprite;

    protected final double targetX;
    protected final double targetY;
    protected final double targetZ;
    //endregion Fields

    //region Initialization
    protected SparkleParticle(IAnimatedSprite sprite, SparkleParticleData data, ClientWorld world, double x,
                              double y, double z) {
        super(world, x, y, z, 0, 0, 0);
        this.sprite = sprite;

        this.targetX = data.targetX;
        this.targetY = data.targetY;
        this.targetZ = data.targetZ;

        this.setColor(data.red, data.green, data.blue);
        this.particleScale = this.rand.nextFloat() * 0.3F; // original: rand * 0.5 + 0.5F

        double dx = this.targetX - this.posX;
        double dy = this.targetY - this.posY;
        double dz = this.targetZ - this.posZ;

        int base = (int) (MathHelper.sqrt(dx * dx + dy * dy + dz * dz) * 10.0F);
        if (base < 1)
            base = 1;
        this.setMaxAge(base / 2 + this.rand.nextInt(base));

        float motionScale = 0.01F;
        this.motionX = ((float) this.rand.nextGaussian() * motionScale);
        this.motionY = ((float) this.rand.nextGaussian() * motionScale);
        this.motionZ = ((float) this.rand.nextGaussian() * motionScale);

        this.particleGravity = 0.2F;
        this.particleAlpha = 1.0f;

        this.canCollide = false;
    }
    //endregion Initialization

    //region Overrides
    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {


        float bob = MathHelper.sin(this.age / 3.0F) * 0.5F + 1.0F;
        float bobOffset = 0.1F * this.particleScale * bob;

        //copied from super.renderParticle, but applies bob offset
        Vector3d vector3d = renderInfo.getProjectedView();
        float currentX = (float) (MathHelper.lerp(partialTicks, this.prevPosX, this.posX) - vector3d.getX());
        float currentY = (float) (MathHelper.lerp(partialTicks, this.prevPosY, this.posY) - vector3d.getY());
        float currentZ = (float) (MathHelper.lerp(partialTicks, this.prevPosZ, this.posZ) - vector3d.getZ());

        currentX -= bobOffset;
        currentY -= bobOffset;
        currentZ -= bobOffset;

        Quaternion quaternion;
        if (this.particleAngle == 0.0F) {
            quaternion = renderInfo.getRotation();
        }
        else {
            quaternion = new Quaternion(renderInfo.getRotation());
            float f3 = MathHelper.lerp(partialTicks, this.prevParticleAngle, this.particleAngle);
            quaternion.multiply(Vector3f.ZP.rotation(f3));
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.transform(quaternion);
        Vector3f[] avector3f =
                new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F,
                        1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.getScale(partialTicks);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.transform(quaternion);
            vector3f.mul(f4);
            vector3f.add(currentX, currentY, currentZ);
        }

        float f7 = this.getMinU();
        float f8 = this.getMaxU();
        float f5 = this.getMinV();
        float f6 = this.getMaxV();
        int j = this.getBrightnessForRender(partialTicks);
        buffer.pos(avector3f[0].getX(), avector3f[0].getY(), avector3f[0].getZ())
                .tex(f8, f6)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j)
                .endVertex();
        buffer.pos(avector3f[1].getX(), avector3f[1].getY(), avector3f[1].getZ())
                .tex(f8, f5)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j)
                .endVertex();
        buffer.pos(avector3f[2].getX(), avector3f[2].getY(), avector3f[2].getZ())
                .tex(f7, f5)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j)
                .endVertex();
        buffer.pos(avector3f[3].getX(), avector3f[3].getY(), avector3f[3].getZ())
                .tex(f7, f6)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j)
                .endVertex();

    }

    @Override
    public void tick() {
        //no call to super, we're calculating movement on our own
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        //expire particle if too old, or if we are on same block pos as target
        if (this.age++ >= this.maxAge ||
            (MathHelper.floor(this.posX) == MathHelper.floor(this.targetX) &&
             MathHelper.floor(this.posY) == MathHelper.floor(this.targetY) &&
             MathHelper.floor(this.posZ) == MathHelper.floor(this.targetZ))) {
            this.setExpired();
            return;
        }

        //move particle
        this.move(this.motionX, this.motionY, this.motionZ);

        //then recalculate motion
        this.motionX *= 0.985d;
        this.motionY *= 0.95d;
        this.motionZ *= 0.985d;
        double dx = this.targetX - this.posX;
        double dy = this.targetY - this.posY;
        double dz = this.targetZ - this.posZ;
        double distance = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
        double clamp = Math.min(0.25d, distance / 15.0d);
        if (distance < 2.0d)
            this.particleScale *= 0.9F;
        dx /= distance;
        dy /= distance;
        dz /= distance;
        this.motionX += dx * clamp;
        this.motionY += dy * clamp;
        this.motionZ += dz * clamp;
        this.motionX = MathHelper.clamp((float) this.motionX, -clamp, clamp);
        this.motionY = MathHelper.clamp((float) this.motionY, -clamp, clamp);
        this.motionZ = MathHelper.clamp((float) this.motionZ, -clamp, clamp);
        this.motionX += this.rand.nextGaussian() * 0.01d;
        this.motionY += this.rand.nextGaussian() * 0.01d;
        this.motionZ += this.rand.nextGaussian() * 0.01d;

        this.selectSpriteWithAge(this.sprite);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return TheurgyParticleRenderTypes.PARTICLE_SHEET_ADDITIVE;
    }
    //endregion Overrides
}
