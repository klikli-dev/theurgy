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

package com.github.klikli_dev.theurgy.common.handlers;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.client.particle.CrucibleBubbleParticleType;
import com.github.klikli_dev.theurgy.client.particle.GlowingBallParticleType;
import com.github.klikli_dev.theurgy.client.particle.SparkleParticleType;
import com.github.klikli_dev.theurgy.registry.ParticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Theurgy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistryEventHandler {
    //region Static Methods
    @SubscribeEvent()
    public static void onRegisterParticleFactories(ParticleFactoryRegisterEvent event) {
        ParticleManager manager = Minecraft.getInstance().particles;

        manager.registerFactory(ParticleRegistry.CRUCIBLE_BUBBLES.get(), CrucibleBubbleParticleType.Factory::new);
        manager.registerFactory(ParticleRegistry.GLOWING_BALL.get(), GlowingBallParticleType.Factory::new);
        manager.registerFactory(ParticleRegistry.SPARKLE.get(), SparkleParticleType.Factory::new);

        Theurgy.LOGGER.info("Registered Particle Factories");
    }
    //endregion Static Methods
}