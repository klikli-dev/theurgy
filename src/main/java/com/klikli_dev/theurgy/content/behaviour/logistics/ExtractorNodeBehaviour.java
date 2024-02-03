package com.klikli_dev.theurgy.content.behaviour.logistics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import java.util.Set;

/**
 * A special leaf node that is used to extract from its targets and inserts into valid insert targets in the graph.
 */
public abstract class ExtractorNodeBehaviour<T, C> extends LeafNodeBehaviour<T, C> {

    //TODO: if we end up with nodes that can support multiple capability types we need to rework this
    //      for now we are working with the assumption that each leaf node represents exactly one capability type.
    //      if we get multi-type nodes we can probably just handle the functionality in the subclasses

    @Override
    public LeafNodeMode mode() {
        return LeafNodeMode.EXTRACT;
    }

    //TODO: the extractor blocks are the ones that tick and "push" stuff around the network

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
        this.insertTargets().add(capability);
    }

    protected void removeInsertTarget(GlobalPos pos) {
        this.removeInsertTarget(pos.dimension(), pos.pos());
    }

    protected void removeInsertTarget(ResourceKey<Level> dimension, BlockPos pos) {
        this.insertTargets().removeIf(cached -> cached.level().dimension().equals(dimension) && cached.pos().equals(pos));
    }

    /**
     * Checks if the target is a valid insert target for this extractor node.
     * This should mainly perform the check if the desired capability is present.
     */
    protected abstract boolean isValidInsertTarget(LeafNodeBehaviour<T, C> leafNode, BlockCapabilityCache<T, C> capability);

    /**
     * Gets the list of cached block entities connected to insert nodes that this extractor will insert into
     */
    public abstract Set<BlockCapabilityCache<T, C>> insertTargets();

    public abstract void resetInsertTargets();
}
