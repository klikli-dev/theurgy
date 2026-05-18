// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.content.particle.coloredbubble.ColoredBubbleParticleOptions;
import com.klikli_dev.theurgy.content.particle.coloredbubble.ColoredBubbleParticleProvider;
import com.klikli_dev.theurgy.content.particle.coloredbubble.ColoredBubbleParticleType;
import com.klikli_dev.magicparticleslib.premade.particle.glow.GlowParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ARGB;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ParticleRegistry {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Theurgy.MODID);

    public static final Supplier<ParticleType<ColoredBubbleParticleOptions>> COLORED_BUBBLE_TYPE = PARTICLES.register("colored_bubble", ColoredBubbleParticleType::new);

    public static void registerFactories(RegisterParticleProvidersEvent evt) {
        evt.registerSpriteSet(COLORED_BUBBLE_TYPE.get(), ColoredBubbleParticleProvider::new);
    }
}
