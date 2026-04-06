// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jade;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.view.*;

import java.util.List;
import java.util.Objects;

public class MercuryFluxEnergyProvider implements IServerExtensionProvider<CompoundTag>, IClientExtensionProvider<CompoundTag, EnergyView> {

    public static final Identifier ID = Theurgy.loc("mercury_flux");
    private static final MercuryFluxEnergyProvider instance = new MercuryFluxEnergyProvider();

    public static MercuryFluxEnergyProvider get() {
        return instance;
    }

    public static @Nullable List<ViewGroup<CompoundTag>> wrapMercuryFluxStorage(BlockEntity blockEntity) {
        var storage = blockEntity.getLevel().getCapability(CapabilityRegistry.MERCURY_FLUX_HANDLER, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, null);
        if (storage != null) {
            CompoundTag tag = new CompoundTag();
            tag.putLong("cur", storage.getEnergyStored());
            tag.putLong("max", storage.getMaxEnergyStored());
            tag.putString("unit", "MF");
            ViewGroup<CompoundTag> group = new ViewGroup<>(List.of(tag));
            return List.of(group);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ClientViewGroup<EnergyView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
        return groups.stream().map($ -> {
            List<EnergyView> views = $.views.stream().map(tag -> {
                long cur = tag.getLong("cur").orElse(0L);
                long max = tag.getLong("max").orElse(0L);
                String unit = tag.getString("unit").orElse("MF");
                return EnergyView.read(new EnergyView.Data(cur, max), unit);
            }).filter(Objects::nonNull).toList();
            return new ClientViewGroup<>(views);
        }).toList();
    }

    @Override
    public Identifier getUid() {
        return ID;
    }

    @Override
    public @Nullable List<ViewGroup<CompoundTag>> getGroups(Accessor<?> accessor) {
        if (accessor instanceof BlockAccessor blockAccessor) {
            return wrapMercuryFluxStorage(blockAccessor.getBlockEntity());
        }
        return null;
    }

    public enum Client implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        }

        @Override
        public Identifier getUid() {
            return ID;
        }
    }
}
