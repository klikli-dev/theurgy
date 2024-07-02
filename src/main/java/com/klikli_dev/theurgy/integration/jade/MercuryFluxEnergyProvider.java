// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jade;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.*;
import snownee.jade.util.CommonProxy;

import java.util.List;
import java.util.Objects;

public class MercuryFluxEnergyProvider implements IServerExtensionProvider<CompoundTag>, IClientExtensionProvider<CompoundTag, EnergyView> {

    public static final ResourceLocation ID = Theurgy.loc("mercury_flux");
    private static final MercuryFluxEnergyProvider instance = new MercuryFluxEnergyProvider();

    public static MercuryFluxEnergyProvider get() {
        return instance;
    }

    public static @Nullable List<ViewGroup<CompoundTag>> wrapMercuryFluxStorage(Accessor<?> accessor) {
        var storage = CommonProxy.getDefaultStorage(accessor, CapabilityRegistry.MERCURY_FLUX_HANDLER, null);
        if (storage != null) {
            ViewGroup<CompoundTag> group = new ViewGroup(List.of(EnergyView.of(storage.getEnergyStored(), storage.getMaxEnergyStored())));
            group.getExtraData().putString("Unit", "MF");
            return List.of(group);
        } else {
            return null;
        }
    }

    @Override
    public List<ClientViewGroup<EnergyView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
        return groups.stream().map($ -> {
            String unit = $.getExtraData().getString("Unit");
            return new ClientViewGroup<>($.views.stream().map(tag -> EnergyView.read(tag, unit)).filter(Objects::nonNull).toList());
        }).toList();
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public @Nullable List<ViewGroup<CompoundTag>> getGroups(Accessor<?> accessor) {
        return wrapMercuryFluxStorage(accessor);
    }

    public boolean shouldRequestData(Accessor<?> accessor) {
        return hasDefaultMercuryFluxStorage(accessor);
    }

    public static boolean hasDefaultMercuryFluxStorage(Accessor<?> accessor) {
        return CommonProxy.hasDefaultStorage(accessor, CapabilityRegistry.MERCURY_FLUX_HANDLER, null);
    }
}
