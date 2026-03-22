// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.logistics.distribution;

import com.klikli_dev.theurgy.content.behaviour.logistics.InsertTarget;

import java.util.List;

public enum DistributionMode {
    SINGLE_TARGET,
    ROUND_ROBIN;

    public static <T, C> Distributor<T, C> createDistributor(DistributionMode mode, List<InsertTarget<T, C>> targets) {
        return switch (mode) {
            //Note: We should not implement single target, it can be achieved with frequencies
            case SINGLE_TARGET -> throw new IllegalArgumentException("Not yet implemented: " + mode);
            case ROUND_ROBIN -> new RoundRobinDistributor<>(targets);
            default -> throw new IllegalArgumentException("Unknown distribution mode: " + mode);
        };
    }
}
