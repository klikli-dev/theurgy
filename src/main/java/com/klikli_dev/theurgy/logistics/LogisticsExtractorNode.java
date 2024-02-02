package com.klikli_dev.theurgy.logistics;

import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import java.util.Set;

/**
 * A special leaf node that is used to extract from its targets and inserts into valid insert targets in the graph.
 */
public interface LogisticsExtractorNode<T, C> extends LogisticsLeafNode<T, C> {

    //TODO: if we end up with nodes that can support multiple capability types we need to rework this
    //      for now we are working with the assumption that each leaf node represents exactly one capability type.
    //      if we get multi-type nodes we can probably just handle the functionality in the subclasses

    @Override
    default LeafNodeMode mode() {
        return LeafNodeMode.EXTRACT;
    }

    //TODO: BlockCapabilityCache
    //      Figure out how we handle the generics
    //      -> we need to store the nodes by their capability in a map in our logistics system, that way we can easily notify those of the same type

    /**
     * Called if a leaf node is added to the graph.
     * Should be used to update the cached insertTargets.
     *
     * @param pos      the position of the leaf node
     * @param leafNode the leaf node added
     */
    default void onLeafNodeAddedToGraph(BlockPos pos, LogisticsLeafNode<T, C> leafNode) {
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
    default void onLeafNodeRemovedFromGraph(BlockPos pos, LogisticsLeafNode<T, C> leafNode) {
        if (leafNode.mode() != LeafNodeMode.INSERT)
            return; //as we are only caching insert mode all other nodes are not relevant

        for (var target : leafNode.targets()) {
            this.removeInsertTarget(target);
        }
    }

    /**
     * Called if a single target is added to the graph.
     * This is NOT called if a leaf node (with one or more targets) is added to the graph.
     * For that see onLeafNodeAddedToGraph.
     */
    default void onTargetAddedToGraph(BlockPos pos, BlockCapabilityCache<T, C> capability, LogisticsLeafNode<T, C> leafNode) {
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
    default void onTargetRemovedFromGraph(BlockPos pos, LogisticsLeafNode<T, C> leafNode) {
        if (leafNode.mode() != LeafNodeMode.INSERT)
            return; //as we are only caching insert mode all other nodes are not relevant

        this.removeInsertTarget(pos);
    }

    default void addInsertTarget(BlockCapabilityCache<T, C> capability) {
        this.insertTargets().add(capability);
    }

    default void removeInsertTarget(BlockPos pos) {
        this.insertTargets().removeIf(be -> be.pos().equals(pos));
    }

    /**
     * Checks if the target is a valid insert target for this extractor node.
     * This should mainly perform the check if the desired capability is present.
     */
    boolean isValidInsertTarget(LogisticsLeafNode<T, C> leafNode, BlockCapabilityCache<T, C> capability);

    /**
     * Gets the list of cached block entities connected to insert nodes that this extractor will insert into
     */
    Set<BlockCapabilityCache<T, C>> insertTargets();
}
