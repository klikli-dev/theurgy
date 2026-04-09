// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticscapabilityproxy;

import com.klikli_dev.theurgy.content.apparatus.logisticscapabilityprobe.LogisticsCapabilityProbeBlock;
import com.klikli_dev.theurgy.content.capability.MercuryFluxStorage;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ProxyMercuryFluxStorage implements MercuryFluxStorage {

    protected final Level level;
    protected final BlockPos probePos;

    public ProxyMercuryFluxStorage(Level level, BlockPos probePos) {
        this.level = level;
        this.probePos = probePos.immutable();
    }

    protected @Nullable MercuryFluxStorage storage() {
        return LogisticsCapabilityProbeBlock.resolveSidedCapability(this.level, this.probePos, CapabilityRegistry.MERCURY_FLUX_HANDLER);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        var storage = this.storage();
        return storage == null ? 0 : storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        var storage = this.storage();
        return storage == null ? 0 : storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        var storage = this.storage();
        return storage == null ? 0 : storage.getEnergyStored();
    }

    @Override
    public void setEnergyStored(int energy) {
        var storage = this.storage();
        if (storage != null) {
            storage.setEnergyStored(energy);
        }
    }

    @Override
    public int getMaxEnergyStored() {
        var storage = this.storage();
        return storage == null ? 0 : storage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        var storage = this.storage();
        return storage != null && storage.canExtract();
    }

    @Override
    public boolean canReceive() {
        var storage = this.storage();
        return storage != null && storage.canReceive();
    }
}
