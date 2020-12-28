package com.github.klikli_dev.theurgy.registry;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.client.particle.CrucibleBubbleParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleRegistry {
    //region Fields
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(
            ForgeRegistries.PARTICLE_TYPES, Theurgy.MODID);

    public static final RegistryObject<CrucibleBubbleParticleType> CRUCIBLE_BUBBLES = PARTICLES.register(
            "crucible_bubbles", CrucibleBubbleParticleType::new);
    //endregion Fields
}
