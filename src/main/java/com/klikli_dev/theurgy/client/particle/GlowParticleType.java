/*
 * SPDX-FileCopyrightText: 2022 the original author or authors of Ars Noveau https://github.com/baileyholl/Ars-Nouveau
 *
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.klikli_dev.theurgy.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class GlowParticleType extends ParticleType<ColorParticleTypeData> {
    public GlowParticleType() {
        super(false, ColorParticleTypeData.DESERIALIZER);
    }

    @Override
    public Codec<ColorParticleTypeData> codec() {
        return ColorParticleTypeData.CODEC;
    }
}
