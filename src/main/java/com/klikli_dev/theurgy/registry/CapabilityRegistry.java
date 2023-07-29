/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.content.capability.HeatProvider;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.energy.IEnergyStorage;

import static net.minecraftforge.common.capabilities.CapabilityManager.get;

public class CapabilityRegistry {

    public static Capability<HeatProvider> HEAT_PROVIDER = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Capability<IEnergyStorage> MERCURY_FLUX = get(new CapabilityToken<>(){});


    public static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
        event.register(HeatProvider.class);
    }

}
