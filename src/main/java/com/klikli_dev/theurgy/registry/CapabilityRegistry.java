// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.content.capability.HeatProvider;
import com.klikli_dev.theurgy.content.capability.HeatReceiver;
import com.klikli_dev.theurgy.content.capability.MercuryFluxStorage;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;
import net.neoforged.neoforge.common.capabilities.RegisterCapabilitiesEvent;

import static net.neoforged.neoforge.common.capabilities.CapabilityManager.get;

public class CapabilityRegistry {

    public static Capability<HeatProvider> HEAT_PROVIDER = CapabilityManager.get(new CapabilityToken<>() {});
    public static Capability<HeatReceiver> HEAT_RECEIVER = CapabilityManager.get(new CapabilityToken<>() {});

    public static final Capability<MercuryFluxStorage> MERCURY_FLUX = get(new CapabilityToken<>(){});


    public static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
        event.register(HeatProvider.class);
        event.register(HeatReceiver.class);
        event.register(MercuryFluxStorage.class);
    }

}
