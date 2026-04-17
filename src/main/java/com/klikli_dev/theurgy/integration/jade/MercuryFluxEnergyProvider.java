// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jade;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.JadeIds;
import snownee.jade.api.StreamServerDataProvider;
import snownee.jade.api.TooltipPosition;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.EnergyView;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ViewGroup;
import snownee.jade.impl.WailaCommonRegistration;
import snownee.jade.util.CommonProxy;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

public class MercuryFluxEnergyProvider<T extends Accessor<?>> implements StreamServerDataProvider<T, Entry<Identifier, List<ViewGroup<EnergyView.Data>>>> {
    public static final Identifier ID = Theurgy.loc("mercury_flux");
    protected static final StreamCodec<RegistryFriendlyByteBuf, Entry<Identifier, List<ViewGroup<EnergyView.Data>>>> STREAM_CODEC = ViewGroup.listCodec(EnergyView.Data.STREAM_CODEC)
            .cast();
    public static final MercuryFluxEnergyProvider<BlockAccessor> BLOCK = new MercuryFluxEnergyProvider<>();

    public static @Nullable List<ViewGroup<EnergyView.Data>> wrapMercuryFluxStorage(Accessor<?> accessor) {
        if (!(accessor instanceof BlockAccessor)) {
            return null;
        }

        var storage = CommonProxy.getDefaultStorage(accessor, CapabilityRegistry.MERCURY_FLUX_HANDLER, null);
        if (storage == null || storage.getMaxEnergyStored() <= 0) {
            return null;
        }

        ViewGroup<EnergyView.Data> group = new ViewGroup<>(List.of(new EnergyView.Data(storage.getEnergyStored(), storage.getMaxEnergyStored())));
        group.getExtraData().putString("Unit", "MF");
        return List.of(group);
    }

    public static boolean hasDefaultMercuryFluxStorage(Accessor<?> accessor) {
        return accessor instanceof BlockAccessor && CommonProxy.hasDefaultStorage(accessor, CapabilityRegistry.MERCURY_FLUX_HANDLER, null);
    }

    @Override
    public @Nullable Entry<Identifier, List<ViewGroup<EnergyView.Data>>> streamData(T accessor) {
        return CommonProxy.getServerExtensionData(accessor, WailaCommonRegistration.instance().energyStorageProviders);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Entry<Identifier, List<ViewGroup<EnergyView.Data>>>> streamCodec() {
        return STREAM_CODEC;
    }

    @Override
    public Identifier getUid() {
        return ID;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.BODY + 1000;
    }

    @Override
    public boolean shouldRequestData(T accessor) {
        return (accessor.showDetails() || !IWailaConfig.get().plugin().get(JadeIds.UNIVERSAL_ENERGY_STORAGE_DETAILED)) && WailaCommonRegistration.instance().energyStorageProviders.hitsAny(accessor, IServerExtensionProvider::shouldRequestData);
    }

    public static class Extension implements IServerExtensionProvider<EnergyView.Data> {
        public static final Extension INSTANCE = new Extension();

        @Override
        public Identifier getUid() {
            return ID;
        }

        @Override
        public @Nullable List<ViewGroup<EnergyView.Data>> getGroups(Accessor<?> accessor) {
            return wrapMercuryFluxStorage(accessor);
        }

        @Override
        public boolean shouldRequestData(Accessor<?> accessor) {
            return hasDefaultMercuryFluxStorage(accessor);
        }

        @Override
        public int getDefaultPriority() {
            return 9999;
        }
    }
}
