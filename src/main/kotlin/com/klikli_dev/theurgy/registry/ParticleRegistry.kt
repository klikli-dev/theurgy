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

package com.klikli_dev.theurgy.registry

import com.klikli_dev.theurgy.Theurgy
import com.klikli_dev.theurgy.client.particle.EssentiaTypeParticleData
import com.klikli_dev.theurgy.client.particle.ParticleDivinationRodIndicator
import net.minecraft.client.Minecraft
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister

@EventBusSubscriber(modid = Theurgy.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ParticleRegistry {
    val particles = KDeferredRegister(ForgeRegistries.PARTICLE_TYPES, Theurgy.MOD_ID)

    val divinationRodIndicator by particles.registerObject("divination_rod_indicator") {
        EssentiaTypeParticleData.createParticleType(true)
    }

    @SubscribeEvent
    fun onRegisterParticleFactories(event: ParticleFactoryRegisterEvent?) {
        val manager = Minecraft.getInstance().particles
        manager.registerFactory(divinationRodIndicator, ParticleDivinationRodIndicator::Factory)
        Theurgy.logger.info("Registered Particle Factories")
    }
}