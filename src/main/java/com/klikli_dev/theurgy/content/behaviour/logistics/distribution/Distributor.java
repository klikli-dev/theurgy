// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.logistics.distribution;

import com.klikli_dev.theurgy.content.behaviour.logistics.InsertTarget;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import java.util.List;

public abstract class Distributor<T, C> {
    protected List<InsertTarget<T, C>> targets;

    public Distributor(List<InsertTarget<T, C>> targets) {
        this.targets = targets;
    }

    public abstract DistributionMode mode();

    public abstract InsertTarget<T, C> target();

    public abstract void tick();

    /**
     * Should be called if the list of targets on the leaf node using this distributor changes.
     */
    public abstract void onTargetsChanged();
}
