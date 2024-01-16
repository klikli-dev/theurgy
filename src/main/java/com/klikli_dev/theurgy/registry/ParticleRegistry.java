// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.content.particle.coloredbubble.ColoredBubbleParticleOptions;
import com.klikli_dev.theurgy.content.particle.coloredbubble.ColoredBubbleParticleProvider;
import com.klikli_dev.theurgy.content.particle.coloredbubble.ColoredBubbleParticleType;
import com.klikli_dev.theurgy.content.particle.glow.GlowParticleOptions;
import com.klikli_dev.theurgy.content.particle.glow.GlowParticleProvider;
import com.klikli_dev.theurgy.content.particle.glow.GlowParticleType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public class ParticleRegistry {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Theurgy.MODID);

    public static final RegistryObject<ParticleType<GlowParticleOptions>> GLOW_TYPE = PARTICLES.register("glow", GlowParticleType::new);

    public static final RegistryObject<ParticleType<ColoredBubbleParticleOptions>> COLORED_BUBBLE_TYPE = PARTICLES.register("colored_bubble", ColoredBubbleParticleType::new);

    public static void registerFactories(RegisterParticleProvidersEvent evt) {
        evt.registerSpriteSet(GLOW_TYPE.get(), GlowParticleProvider::new);
        evt.registerSpriteSet(COLORED_BUBBLE_TYPE.get(), ColoredBubbleParticleProvider::new);
    }

    public static void spawnTouch(ClientLevel world, BlockPos loc, ParticleColor particleColor) {
        for (int i = 0; i < 10; i++) {
            double d0 = loc.getX() + 0.5;
            double d1 = loc.getY() + 1.0;
            double d2 = loc.getZ() + .5;
            world.addParticle(GlowParticleProvider.createOptions(particleColor), d0, d1, d2, (world.random.nextFloat() * 1 - 0.5) / 5, (world.random.nextFloat() * 1 - 0.5) / 5, (world.random.nextFloat() * 1 - 0.5) / 5);
        }
    }


}
