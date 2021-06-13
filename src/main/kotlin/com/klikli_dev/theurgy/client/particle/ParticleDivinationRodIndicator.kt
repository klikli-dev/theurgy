/*
 * MIT License
 *
 * Copyright 2021 klikli-dev, sirttas
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

package com.klikli_dev.theurgy.client.particle

import com.klikli_dev.theurgy.api.essentia.EssentiaType
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.*
import net.minecraft.client.settings.GraphicsFanciness
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.vector.Vector3d

/**
 * https://github.com/Sirttas/ElementalCraft
 */
class ParticleDivinationRodIndicator(world: ClientWorld, x: Double, y: Double, z: Double, motionX: Double, motionY: Double, motionZ: Double, type: EssentiaType) :
    SpriteTexturedParticle(world, x, y, z, motionX, motionY, motionZ) {

    private val originX: Double = x
    private val originY: Double = y
    private val originZ: Double = z

    init{
        this.prevPosX = this.originX * this.motionX;
        this.prevPosY = this.originY * this.motionY;
        this.prevPosZ = this.originZ * this.motionZ;
        this.posX = this.prevPosX;
        this.posY = this.prevPosY;
        this.posZ = this.prevPosZ;
        particleScale = 0.1f * (rand.nextFloat() * 0.5f + 0.2f)
        val f = rand.nextFloat() * 0.4f + 0.6f
        particleRed = f * type.getRed()
        particleGreen = f * type.getGreen()
        particleBlue = f * type.getBlue()
        canCollide = false
        maxAge = ((rand.nextInt(10) + 30) * Vector3d(motionX, motionY, motionZ).length()).toInt()
    }

    override fun getRenderType(): IParticleRenderType {
        return if (Minecraft.getInstance().gameSettings.graphicFanciness == GraphicsFanciness.FAST)
            IParticleRenderType.PARTICLE_SHEET_OPAQUE
        else
            TheurgyParticleRenderTypes.particleSheetTranslucentNoDepth
    }

    override fun move(x: Double, y: Double, z: Double) {
        boundingBox = boundingBox.offset(x, y, z)
        resetPositionToBB()
    }

    override fun tick() {
        prevPosX = posX
        prevPosY = posY
        prevPosZ = posZ
        if (age++ >= maxAge) {
            setExpired()
        } else {
            var f = age.toFloat() / maxAge.toFloat()
            f = 1.0f - f
            posX = this.originX + motionX * f
            posY = this.originY + motionY * f
            posZ = this.originZ + motionZ * f
        }
    }

    override fun getBrightnessForRender(partialTick: Float): Int {
        val i = super.getBrightnessForRender(partialTick)
        var f = age.toFloat() / maxAge.toFloat()
        f *= f
        f *= f
        val j = i and 255
        var k = i shr 16 and 255
        k += (f * 15.0f * 16.0f).toInt()
        if (k > 240) {
            k = 240
        }
        return j or k shl 16
    }

    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<EssentiaTypeParticleData> {
        override fun makeParticle(
            data: EssentiaTypeParticleData,
            worldIn: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            val particle = ParticleDivinationRodIndicator(
                worldIn, x, y, z, xSpeed, ySpeed, zSpeed, data.essentiaType
            )
            particle.selectSpriteRandomly(this.spriteSet)
            return particle
        }
    }
}