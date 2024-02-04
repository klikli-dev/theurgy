package com.klikli_dev.theurgy.content.behaviour.logistics.distribution;

import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import java.util.List;

public enum DistributionMode {
    SINGLE_TARGET,
    ROUND_ROBIN;

    public static <T, C> Distributor<T, C> createDistributor(DistributionMode mode, List<BlockCapabilityCache<T, C>> targets) {
        return switch (mode) {
            case SINGLE_TARGET -> throw new IllegalArgumentException("Not yet implemented: " + mode);
            case ROUND_ROBIN -> new RoundRobinDistributor<>(targets);
            default -> throw new IllegalArgumentException("Unknown distribution mode: " + mode);
        };
    }
}
