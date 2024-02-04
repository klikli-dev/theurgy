// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.logistics;

import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.logistics.LogisticsNetwork;
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
    List<BlockCapabilityCache<T, C>> targetCapabilities;

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
                //only listen to the invalidator if the node is still valid
                () -> !this.blockEntity.isRemoved() && Logistics.get().getNetwork(this.globalPos()) != null,
                () -> {
                    //handles chunk loads/unloads and destruction of the target BE
                    this.onCapabilityInvalidated(target, this);
                })).toList();
    }

    public void onCapabilityInvalidated(BlockPos targetPos, InserterNodeBehaviour<T, C> leafNode) {
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
            if (targetValid) {
                var capabilityCache = this.targetCapabilities.stream().filter(cache -> cache.pos().equals(targetPos)).findFirst().orElse(null);
                if (capabilityCache != null){
                    network.onInserterNodeTargetAdded(targetGlobalPos, capabilityCache, leafNode);
                }
            }
        }
    }

    /**
     * gets the target capabilities that are currently loaded & available.
     * @return
     */
    public List<BlockCapabilityCache<T, C>> targetCapabilities() {
        return this.targetCapabilities.stream().filter(cache -> this.level().isLoaded(cache.pos()) && this.level().getBlockEntity(cache.pos()) != null).toList();
    }

    /**
     * Gets all target capabilities.
     */
    public List<BlockCapabilityCache<T, C>> targetAllCapabilities() {
        return this.targetCapabilities;
    }

}
