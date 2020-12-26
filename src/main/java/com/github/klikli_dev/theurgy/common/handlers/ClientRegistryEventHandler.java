package com.github.klikli_dev.theurgy.common.handlers;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.client.particle.CrucibleBubbleParticleType;
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

        Theurgy.LOGGER.info("Registered Particle Factories");
    }
    //endregion Static Methods
}