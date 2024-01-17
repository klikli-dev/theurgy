// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jade;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.view.*;

import java.util.List;
import java.util.Objects;

public class MercuryFluxEnergyProvider implements IServerExtensionProvider<Object, CompoundTag>, IClientExtensionProvider<CompoundTag, EnergyView> {

    public static final ResourceLocation ID = Theurgy.loc("mercury_flux");
    private static final MercuryFluxEnergyProvider instance = new MercuryFluxEnergyProvider();

    public static MercuryFluxEnergyProvider get() {
        return instance;
    }

    public static List<ViewGroup<CompoundTag>> wrapMercuryFluxStorage(Accessor<?> accessor, Object target) {
        if (accessor instanceof BlockAccessor ba) {
            var storage = ba.getLevel().getCapability(CapabilityRegistry.MERCURY_FLUX_HANDLER, ba.getPosition(), ba.getBlockState(), ba.getBlockEntity(), null);
            if (storage != null) {
                ViewGroup<CompoundTag> group = new ViewGroup(List.of(EnergyView.of(storage.getEnergyStored(), storage.getMaxEnergyStored())));
                group.getExtraData().putString("Unit", "MF");
                return List.of(group);
            }
        }

        return null;
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
    public @Nullable List<ViewGroup<CompoundTag>> getGroups(Accessor<?> accessor, Object o) {
        return wrapMercuryFluxStorage(accessor, o);
    }
}
