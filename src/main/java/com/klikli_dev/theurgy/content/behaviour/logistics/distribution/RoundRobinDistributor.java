// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.logistics.distribution;

import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import java.util.List;

public class RoundRobinDistributor<T, C> extends Distributor<T, C>{
    protected int currentTargetIndex;
    protected BlockCapabilityCache<T, C> cachedTarget;

    public RoundRobinDistributor(List<BlockCapabilityCache<T, C>> targets) {
        super(targets);
    }

    @Override
    public DistributionMode mode() {
        return DistributionMode.ROUND_ROBIN;
    }

    @Override
    public BlockCapabilityCache<T, C> target() {
        return this.cachedTarget;
    }

    @Override
    public void tick() {
        var oldIndex = this.currentTargetIndex;

        if(this.targets.isEmpty()) {
            this.currentTargetIndex = -1;
        } else {
            this.currentTargetIndex = (this.currentTargetIndex + 1) % this.targets.size();
        }

        if(oldIndex != this.currentTargetIndex || this.cachedTarget == null){
            if(this.currentTargetIndex >= 0 && this.currentTargetIndex < this.targets.size()) {
                this.cachedTarget = this.targets.get(this.currentTargetIndex);
            } else {
                this.cachedTarget = null;
            }
        }

    }

    @Override
    public void onTargetsChanged() {
        this.cachedTarget = null;
        if(this.targets.isEmpty()) {
            this.currentTargetIndex = -1;
        } else if(this.currentTargetIndex >= this.targets.size()) {
            this.currentTargetIndex = 0;
        }
    }
}
