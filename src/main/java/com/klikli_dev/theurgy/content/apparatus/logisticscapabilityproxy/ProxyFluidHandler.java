// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticscapabilityproxy;

import com.klikli_dev.theurgy.content.apparatus.logisticscapabilityprobe.LogisticsCapabilityProbeBlock;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;

public class ProxyFluidHandler implements ResourceHandler<FluidResource> {

    protected final Level level;
    protected final BlockPos probePos;

    public ProxyFluidHandler(Level level, BlockPos probePos) {
        this.level = level;
        this.probePos = probePos.immutable();
    }

    protected @Nullable ResourceHandler<FluidResource> handler() {
        return LogisticsCapabilityProbeBlock.resolveSidedCapability(this.level, this.probePos, CapabilityRegistry.FLUID_HANDLER);
    }

    @Override
    public int size() {
        var handler = this.handler();
        return handler == null ? 0 : handler.size();
    }

    @Override
    public FluidResource getResource(int index) {
        var handler = this.handler();
        return handler == null || index < 0 || index >= handler.size() ? FluidResource.EMPTY : handler.getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        var handler = this.handler();
        return handler == null || index < 0 || index >= handler.size() ? 0 : handler.getAmountAsLong(index);
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        var handler = this.handler();
        return handler == null || index < 0 || index >= handler.size() ? 0 : handler.getCapacityAsLong(index, resource);
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        var handler = this.handler();
        return handler != null && index >= 0 && index < handler.size() && handler.isValid(index, resource);
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        var handler = this.handler();
        return handler == null || index < 0 || index >= handler.size() ? 0 : handler.insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        var handler = this.handler();
        return handler == null || index < 0 || index >= handler.size() ? 0 : handler.extract(index, resource, amount, transaction);
    }
}
