// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticscapabilityproxy;

import com.klikli_dev.theurgy.content.apparatus.logisticscapabilityprobe.LogisticsCapabilityProbeBlock;
import com.klikli_dev.theurgy.content.capability.HeatReceiver;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ProxyHeatReceiver implements HeatReceiver {

    protected final Level level;
    protected final BlockPos probePos;

    public ProxyHeatReceiver(Level level, BlockPos probePos) {
        this.level = level;
        this.probePos = probePos.immutable();
    }

    protected @Nullable HeatReceiver receiver() {
        return LogisticsCapabilityProbeBlock.resolveSidedCapability(this.level, this.probePos, CapabilityRegistry.HEAT_RECEIVER);
    }

    @Override
    public void setHotUntil(long gameTime) {
        var receiver = this.receiver();
        if (receiver != null) {
            receiver.setHotUntil(gameTime);
        }
    }

    @Override
    public long getIsHotUntil() {
        var receiver = this.receiver();
        return receiver == null ? 0 : receiver.getIsHotUntil();
    }

    @Override
    public boolean readyToReceive() {
        var receiver = this.receiver();
        return receiver != null && receiver.readyToReceive();
    }
}
