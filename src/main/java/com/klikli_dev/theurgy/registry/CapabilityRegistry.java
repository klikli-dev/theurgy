package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.capability.HeatProvider;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityRegistry {

    public static Capability<HeatProvider> HEAT_PROVIDER = CapabilityManager.get(new CapabilityToken<>() {
    });


    public static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
        event.register(HeatProvider.class);
    }

}
