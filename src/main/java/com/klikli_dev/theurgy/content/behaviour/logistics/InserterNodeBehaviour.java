// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.logistics;

import com.klikli_dev.theurgy.logistics.Logistics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import java.util.ArrayList;
import java.util.List;

/**
 * A special leaf node whose targets can be inserted into.
 */
public abstract class InserterNodeBehaviour<T, C> extends LeafNodeBehaviour<T, C> {
    protected List<BlockCapabilityCache<T, C>> targetCapabilities;
    private int targetCapabilityCacheGeneration;
    private boolean targetCapabilityCachesActive;

    public InserterNodeBehaviour(BlockEntity blockEntity, BlockCapability<T, C> capabilityType) {
        super(blockEntity, capabilityType);

        this.targetCapabilities = new ArrayList<>();
        this.targetCapabilityCacheGeneration = 0;
        this.targetCapabilityCachesActive = false;
    }

    @Override
    public LeafNodeMode mode() {
        return LeafNodeMode.INSERT;
    }

    @Override
    public void onLoad() {
        this.rebuildTargetCapabilities();
        super.onLoad();
    }

    @Override
    public void onChunkUnload() {
        this.targetCapabilityCachesActive = false;
        super.onChunkUnload();
    }

    @Override
    public void onDestroyed() {
        this.targetCapabilityCachesActive = false;
        super.onDestroyed();
    }

    protected void rebuildTargetCapabilities() {
        this.targetCapabilityCachesActive = true;
        this.targetCapabilities = this.buildTargetCapabilities(this.targets());
    }

    protected void deactivateTargetCapabilities() {
        this.targetCapabilityCachesActive = false;
    }

    /**
     * Target capabilities are built irrespective of if the target is blockloaded or not.
     * The invalidator will be called if the loaded state changes (or if the target is destroyed).
     * The target capabilities will be updated accordingly.
     */
    public List<BlockCapabilityCache<T, C>> buildTargetCapabilities(List<BlockPos> targets) {
        var serverLevel = (ServerLevel) this.level();
        var server = serverLevel.getServer();
        var generation = ++this.targetCapabilityCacheGeneration;

        return targets.stream()
                .map(target -> {
                    var immutableTarget = target.immutable();
                    return BlockCapabilityCache.create(this.capabilityType(), serverLevel, immutableTarget, this.getTargetContext(immutableTarget),
                            //Only listen while this exact cache generation is still active for this inserter lifecycle.
                            //This avoids stale callbacks after chunk unloads, disable toggles, or direction/context rebuilds.
                            () -> this.isTargetCapabilityCacheValid(generation),
                            () -> server.schedule(server.wrapRunnable(() -> this.handleCapabilityInvalidated(immutableTarget, generation))));
                }).toList();
    }

    protected boolean isTargetCapabilityCacheValid(int generation) {
        return generation == this.targetCapabilityCacheGeneration
                && this.targetCapabilityCachesActive
                && this.enabled()
                && !this.blockEntity.isRemoved();
    }

    protected void handleCapabilityInvalidated(BlockPos targetPos, int generation) {
        if (!this.isTargetCapabilityCacheValid(generation)) {
            return;
        }

        this.onCapabilityInvalidated(targetPos, false);
    }

    public void onCapabilityInvalidated(BlockPos targetPos, boolean forceSetRemoved) {
        var serverLevel = (ServerLevel) this.level();

        var targetGlobalPos = GlobalPos.of(serverLevel.dimension(), targetPos);

        var network = Logistics.get().getNetwork(this.globalPos());
        if (network != null) {
            //always call remove to ensure the target is removed from the graph if it was changed
            //this avoids duplicates because we don't know if any extractor nodes already had it in their list
            network.onInserterNodeTargetRemoved(targetGlobalPos, this);

            //then if we have a still valid one, re-add it / or if it is valid for the first time add it
            if (!forceSetRemoved) {
                this.targetCapabilities.stream()
                        .filter(cache -> cache.pos().equals(targetPos))
                        .filter(this::hasAvailableTargetCapability)
                        .forEach(cache -> network.onInserterNodeTargetAdded(targetGlobalPos, cache, this));
            }
        }
    }

    /**
     * Notify the network that a new capability cache was created -> calls onInserterNodeTargetAdded.
     * The main use is to notify the network when a target capability cache was re-created after its context (direction) changed.
     */
    protected void notifyTargetCapabilityCacheCreated(BlockCapabilityCache<T, C> capability) {
        //only notify if we actually have a valid one - otherwise onCapabilityInvalidated will handle it on load of target
        var targetValid = this.hasAvailableTargetCapability(capability);

        var targetGlobalPos = GlobalPos.of(this.level().dimension(), capability.pos());

        var network = Logistics.get().getNetwork(this.globalPos());
        if (network != null && targetValid) {
            network.onInserterNodeTargetAdded(targetGlobalPos, capability, this);
        }
    }

    /**
     * gets the target capabilities that are currently loaded & available.
     *
     * @return
     */
    public List<BlockCapabilityCache<T, C>> availableTargetCapabilities() {
        return this.targetCapabilities.stream().filter(this::hasAvailableTargetCapability).toList();
    }

    protected boolean hasAvailableTargetCapability(BlockCapabilityCache<T, C> capability) {
        if (capability.getCapability() != null) {
            return true;
        }

        return this.level().getCapability(this.capabilityType(), capability.pos(), capability.context()) != null;
    }

    /**
     * Gets all target capabilities.
     */
    public List<BlockCapabilityCache<T, C>> allTargetCapabilities() {
        return this.targetCapabilities;
    }

}
