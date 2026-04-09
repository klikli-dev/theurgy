// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticscapabilityproxy;

import com.klikli_dev.theurgy.content.apparatus.logisticscapabilityprobe.LogisticsCapabilityProbeBlock;
import com.klikli_dev.theurgy.content.capability.HeatProvider;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ProxyHeatProvider implements HeatProvider {

    protected final Level level;
    protected final BlockPos probePos;

    public ProxyHeatProvider(Level level, BlockPos probePos) {
        this.level = level;
        this.probePos = probePos.immutable();
    }

    protected @Nullable HeatProvider provider() {
        return LogisticsCapabilityProbeBlock.resolveSidedCapability(this.level, this.probePos, CapabilityRegistry.HEAT_PROVIDER);
    }

    @Override
    public boolean isHot() {
        var provider = this.provider();
        return provider != null && provider.isHot();
    }
}
