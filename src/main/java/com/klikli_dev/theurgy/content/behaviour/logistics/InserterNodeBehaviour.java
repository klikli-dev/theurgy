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

    public InserterNodeBehaviour(BlockEntity blockEntity, BlockCapability<T, C> capabilityType) {
        super(blockEntity, capabilityType);

        this.targetCapabilities = new ArrayList<>();
    }

    @Override
    public LeafNodeMode mode() {
        return LeafNodeMode.INSERT;
    }

    @Override
    public void onLoad() {
        //targets are filled via load(tag) on the parent, the NBT in turn is provided by the BlockItem.
        this.targetCapabilities = this.buildTargetCapabilities(this.targets());

        super.onLoad();
    }

    /**
     * Target capabilities are built irrespective of if the target is blockloaded or not.
     * The invalidator will be called if the loaded state changes (or if the target is destroyed).
     * The target capabilities will be updated accordingly.
     */
    public List<BlockCapabilityCache<T, C>> buildTargetCapabilities(List<BlockPos> targets) {
        var serverLevel = (ServerLevel) this.level();
        return targets.stream()
                .map(target -> BlockCapabilityCache.create(this.capabilityType(), serverLevel, target, this.getTargetContext(target),
                        //only listen to the invalidator if we (the BE) still exist.
                        //Note: Previously we also checked if there is a valid network at this location, however that leads to issues
                        //  namely a single inserter node that is not connected to anything will not have a network, which can lead to the cap cache being removed despite the inserter being fine!
                        () -> !this.blockEntity.isRemoved(),
                        () -> {
                            //handles chunk loads/unloads and destruction of the target BE
                            this.onCapabilityInvalidated(target, this, false);
                        })).toList();
    }

    public void onCapabilityInvalidated(BlockPos targetPos, InserterNodeBehaviour<T, C> leafNode, boolean forceSetRemoved) {
        var serverLevel = (ServerLevel) this.level();
        //Note: we never modify this.targetCapabilities because it listens for chunk *loads* too!

        //a valid target means the capability changed but is still there.
        //an invalid target means removed/unloaded.
        var targetValid = serverLevel.isLoaded(targetPos) && serverLevel.getBlockEntity(targetPos) != null;

        var targetGlobalPos = GlobalPos.of(serverLevel.dimension(), targetPos);

        var network = Logistics.get().getNetwork(this.globalPos());
        if (network != null) {
            //always call remove to ensure the target is removed from the graph if it was changed
            //this avoids duplicates because we don't know if any extractor nodes already had it in their list
            network.onInserterNodeTargetRemoved(targetGlobalPos, leafNode);

            //then if we have a still valid one, re-add it / or if it is valid for the first time add it
            if (targetValid && !forceSetRemoved) {
                var capabilityCache = this.targetCapabilities.stream().filter(cache -> cache.pos().equals(targetPos)).findFirst().orElse(null);
                if (capabilityCache != null) {
                    network.onInserterNodeTargetAdded(targetGlobalPos, capabilityCache, leafNode);
                }
            }
        }
    }

    /**
     * Notify the network that a new capability cache was created -> calls onInserterNodeTargetAdded.
     * The main use is to notify the network when a target capability cache was re-created after its context (direction) changed.
     */
    protected void notifyTargetCapabilityCacheCreated(BlockCapabilityCache<T, C> capability) {
        var serverLevel = (ServerLevel) this.level();

        //only notify if we actually have a valid one - otherwise onCapabilityInvalidated will handle it on load of target
        var targetValid = serverLevel.isLoaded(capability.pos()) && serverLevel.getBlockEntity(capability.pos()) != null;

        var targetGlobalPos = GlobalPos.of(serverLevel.dimension(), capability.pos());

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
        return this.targetCapabilities.stream().filter(cache ->
                this.level().isLoaded(cache.pos()) &&
                        //instead of querying the BE, we query the cap.
                        //because some caps come without BE, and even our own double-height blocks don't have a BE for the upper part.
                        this.level().getCapability(this.capabilityType(), cache.pos(), cache.context()) != null
        ).toList();
    }

    /**
     * Gets all target capabilities.
     */
    public List<BlockCapabilityCache<T, C>> allTargetCapabilities() {
        return this.targetCapabilities;
    }

}
