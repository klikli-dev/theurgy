// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jade;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.capability.MercuryFluxStorage;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.capabilities.CapabilityProvider;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.*;

import java.util.List;
import java.util.Objects;

public class MercuryFluxEnergyProvider implements IServerExtensionProvider<Object, CompoundTag>, IClientExtensionProvider<CompoundTag, EnergyView> {

    public static final ResourceLocation ID = Theurgy.loc("mercury_flux");
    private static final MercuryFluxEnergyProvider instance = new MercuryFluxEnergyProvider();

    public static MercuryFluxEnergyProvider get() {
        return instance;
    }

    public static List<ViewGroup<CompoundTag>> wrapMercuryFluxStorage(Object target, @Nullable Player player) {
        if (target instanceof CapabilityProvider<?> capProvider) {
            var storage = (MercuryFluxStorage) capProvider.getCapability(CapabilityRegistry.MERCURY_FLUX).orElse(null);
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
    public List<ViewGroup<CompoundTag>> getGroups(ServerPlayer player, ServerLevel level, Object target, boolean showDetails) {
        return wrapMercuryFluxStorage(target, player);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }
}
