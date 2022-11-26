/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.client.particle.ColorParticleTypeData;
import com.klikli_dev.theurgy.client.particle.GlowParticleData;
import com.klikli_dev.theurgy.client.particle.GlowParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegistry {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Theurgy.MODID);

    public static final RegistryObject<ParticleType<ColorParticleTypeData>> GLOW_TYPE = PARTICLES.register(GlowParticleData.NAME, () -> new GlowParticleType());

    public static void registerFactories(RegisterParticleProvidersEvent evt) {
        evt.register(GLOW_TYPE.get(), GlowParticleData::new);
    }

}
