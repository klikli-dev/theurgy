// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.logistics;

import com.klikli_dev.theurgy.content.behaviour.logistics.distribution.DistributionMode;
import com.klikli_dev.theurgy.content.behaviour.logistics.distribution.Distributor;
import com.klikli_dev.theurgy.content.behaviour.logistics.distribution.RoundRobinDistributor;
import com.klikli_dev.theurgy.logistics.Logistics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import java.util.ArrayList;
import java.util.List;

/**
 * A special leaf node that is used to extract from its targets and inserts into valid insert targets in the graph.
 */
public abstract class ExtractorNodeBehaviour<T, C> extends LeafNodeBehaviour<T, C> {

    protected List<BlockCapabilityCache<T, C>> insertTargets;
    protected List<BlockCapabilityCache<T, C>> extractTargets;
    protected Distributor<T, C> distributor;

    public ExtractorNodeBehaviour(BlockEntity blockEntity, BlockCapability<T, C> capabilityType) {
        super(blockEntity, capabilityType);
        this.insertTargets = new ArrayList<>();
        this.extractTargets = new ArrayList<>();
        this.distributor = DistributionMode.createDistributor(DistributionMode.ROUND_ROBIN, this.insertTargets);
    }

    //TODO: if we end up with nodes that can support multiple capability types we need to rework this
    //      for now we are working with the assumption that each leaf node represents exactly one capability type.
    //      if we get multi-type nodes we can probably just handle the functionality in the subclasses

    @Override
    public LeafNodeMode mode() {
        return LeafNodeMode.EXTRACT;
    }

    /**
     * Gets the list of cached block entities connected to insert nodes that this extractor will insert into
     */
    public List<BlockCapabilityCache<T, C>> insertTargets() {
        return this.insertTargets;
    }

    public void resetInsertTargets() {
        this.insertTargets.clear();
    }

    public List<BlockCapabilityCache<T, C>> extractTargets() {
        return this.extractTargets;
    }

    /**
     * Called if a leaf node is added to the graph.
     * Should be used to update the cached insertTargets.
     *
     * @param pos      the position of the leaf node
     * @param leafNode the leaf node added
     */
    public void onLeafNodeAddedToGraph(GlobalPos pos, LeafNodeBehaviour<T, C> leafNode) {
        if (leafNode.mode() != LeafNodeMode.INSERT)
            return; //as we are only caching insert mode all other nodes are not relevant

        var targets = leafNode.asInserter().targetCapabilities();

        for (var target : targets) {
            if (target != null && this.isValidInsertTarget(leafNode, target)) {
                this.addInsertTarget(target);
            }
        }
    }

    /**
     * Called if a leaf node is removed from the graph.
     * Should be used to update the cached insertTargets.
     */
    public void onLeafNodeRemovedFromGraph(GlobalPos pos, LeafNodeBehaviour<T, C> leafNode) {
        if (leafNode.mode() != LeafNodeMode.INSERT)
            return; //as we are only caching insert mode all other nodes are not relevant

        for (var target : leafNode.targets()) {
            this.removeInsertTarget(leafNode.level().dimension(), target);
        }
    }

    /**
     * Called if a single target is added to the graph.
     * This is NOT called if a leaf node (with one or more targets) is added to the graph.
     * For that see onLeafNodeAddedToGraph.
     */
    public void onTargetAddedToGraph(GlobalPos pos, BlockCapabilityCache<T, C> capability, LeafNodeBehaviour<T, C> leafNode) {
        if (leafNode.mode() != LeafNodeMode.INSERT)
            return; //as we are only caching insert mode all other nodes are not relevant

        if (this.isValidInsertTarget(leafNode, capability)) {
            this.addInsertTarget(capability);
        }
    }

    /**
     * Called if a single target is removed from the graph.
     * This is NOT called if a leaf node (with one or more targets) is removed from the graph.
     * For that see onLeafNodeRemovedFromGraph.
     */
    public void onTargetRemovedFromGraph(GlobalPos pos, LeafNodeBehaviour<T, C> leafNode) {
        if (leafNode.mode() != LeafNodeMode.INSERT)
            return; //as we are only caching insert mode all other nodes are not relevant

        this.removeInsertTarget(pos);
    }

    protected void addInsertTarget(BlockCapabilityCache<T, C> capability) {
        if (!this.insertTargets().contains(capability)){
            this.insertTargets().add(capability);
            this.distributor.onTargetsChanged();
        }
    }

    protected void removeInsertTarget(GlobalPos pos) {
        this.removeInsertTarget(pos.dimension(), pos.pos());
    }

    protected void removeInsertTarget(ResourceKey<Level> dimension, BlockPos pos) {
        if(this.insertTargets().removeIf(cached -> cached.level().dimension().equals(dimension) && cached.pos().equals(pos))){
            this.distributor.onTargetsChanged();
        }
    }


    @Override
    public void onLoad() {
        //targets are filled via load(tag) on the parent, the NBT in turn is provided by the BlockItem.
        this.rebuildExtractTargets();

        super.onLoad();
    }

    public void rebuildExtractTargets() {
        this.extractTargets.clear();
        this.extractTargets.addAll(this.buildTargetCapabilities(this.targets()));
    }

    /**
     * Target capabilities are built irrespective of if the target is blockloaded or not.
     * The invalidator will be called if the loaded state changes (or if the target is destroyed).
     * The target capabilities will be updated accordingly.
     */
    public List<BlockCapabilityCache<T, C>> buildTargetCapabilities(List<BlockPos> targets) {
        var serverLevel = (ServerLevel) this.level();
        return targets.stream()
                .map(target -> BlockCapabilityCache.create(this.capabilityType(), serverLevel, target, this.getTargetContext(target))).toList();
    }

    //TODO: the extractor blocks are the ones that tick and "push" stuff around the network

    public void tickServer() {
        this.distributor.tick();
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putByte("distributor", (byte) this.distributor.mode().ordinal());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        DistributionMode mode = DistributionMode.ROUND_ROBIN;
        if (pTag.contains("distributor")) {
            mode = DistributionMode.values()[pTag.getByte("distributor")];
        }
        this.distributor = DistributionMode.createDistributor(mode, this.insertTargets);
    }


    /**
     * Checks if the target is a valid insert target for this extractor node.
     */
    protected abstract boolean isValidInsertTarget(LeafNodeBehaviour<T, C> leafNode, BlockCapabilityCache<T, C> capability);
}
