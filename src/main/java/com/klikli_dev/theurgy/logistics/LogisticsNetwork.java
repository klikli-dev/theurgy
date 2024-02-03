package com.klikli_dev.theurgy.logistics;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.klikli_dev.theurgy.content.behaviour.logistics.ExtractorNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.InserterNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeMode;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.GlobalPos;
import net.neoforged.neoforge.capabilities.BlockCapability;

import java.util.Set;

/**
 * Represents one network within the full logistics graph
 * <p>
 * A network is simply a collection of nodes that are connected directly or indirectly.
 * The interesting feature of the network is the leaf nodes - only those are interacted with by the outside.
 * The leaf nodes form sub-networks that are defined by the capability type they support, and by a frequency.
 */
public class LogisticsNetwork {
    private final Set<GlobalPos> nodes = new ObjectOpenHashSet<>();
    private final Set<GlobalPos> leafNodes = new ObjectOpenHashSet<>();
    private final SetMultimap<Key, GlobalPos> keyToLeafNodes = HashMultimap.create();

    public Set<GlobalPos> nodes() {
        return this.nodes;
    }

    public Set<GlobalPos> getLeafNodes(Key key) {
        return this.keyToLeafNodes.get(key);
    }

    public Set<GlobalPos> getLeafNodes(BlockCapability<?, ?> capability, int frequency) {
        return this.getLeafNodes(new Key(capability, frequency));
    }

    public void addNode(GlobalPos pos) {
        this.nodes.add(pos);
    }

    public void addLeafNode(LeafNodeBehaviour<?, ?> leafNode) {
        var pos = leafNode.globalPos();
        this.leafNodes.add(pos);
        this.keyToLeafNodes.put(new Key(leafNode.capabilityType(), leafNode.frequency()), pos);
    }

    public <T, C> void onFrequencyChange(LeafNodeBehaviour<T, C> leafNode, BlockCapability<T, C> capability, int oldFrequency, int newFrequency) {
        var pos = leafNode.globalPos();
        var oldKey = new Key(capability, oldFrequency);
        var newKey = new Key(capability, newFrequency);

        this.keyToLeafNodes.remove(oldKey, pos);
        this.keyToLeafNodes.put(newKey, pos);

        //Note: When we update the network, it only updates currently loaded leaf nodes.
        //      That is ok -> the unloaded ones re-query their status when they are loaded.

        if (leafNode.mode() == LeafNodeMode.INSERT) {
            this.onInsertNodeFrequencyChange(leafNode.asInserter(), oldKey, newKey);
        }
        if (leafNode.mode() == LeafNodeMode.EXTRACT) {
            this.onExtractNodeFrequencyChange(leafNode.asExtractor(), oldKey, newKey);
        }
    }

    protected <T, C> void onExtractNodeFrequencyChange(ExtractorNodeBehaviour<T, C> leafNode, Key oldKey, Key newKey) {
        this.onUnloadExtractNode(leafNode, oldKey);
        this.onLoadExtractNode(leafNode, newKey);
    }

    protected <T, C> void onInsertNodeFrequencyChange(InserterNodeBehaviour<T, C> leafNode, Key oldKey, Key newKey) {
        this.onUnloadInsertNode(leafNode, oldKey);
        this.onLoadInsertNode(leafNode, newKey);
    }

    /**
     * Shorthand for loads called from the node itself.
     * Does not need to provide the key separately, because it is unchanged.
     * The main overload is also used for frequency changes so we need to be able to manually specify a key.
     */
    public <T, C> void onLoadExtractNode(ExtractorNodeBehaviour<T, C> leafNode) {
        this.onLoadExtractNode(leafNode, new Key(leafNode.capabilityType(), leafNode.frequency()));
    }

    /**
     * Called when an extract node is loaded on / added to the graph
     * It rebuilds the cache of insert targets for the node.
     */
    public <T, C> void onLoadExtractNode(ExtractorNodeBehaviour<T, C> leafNode, Key newKey) {
        var otherNodes = this.getLeafNodes(newKey);

        for (var other : otherNodes) {
            if (other.equals(leafNode.globalPos())) { //skip self
                continue;
            }

            //noinspection unchecked -> our set ensures only compatible nodes are available
            var otherLeafNode = (LeafNodeBehaviour<T, C>) Logistics.get().getLeafNode(other, LeafNodeMode.INSERT);
            if (otherLeafNode == null) {
                continue;
            }

            leafNode.onLeafNodeAddedToGraph(other, otherLeafNode);
        }
    }

    /**
     * Shorthand for unloads called from the node itself.
     * Does not need to provide the key separately, because it is unchanged.
     * The main overload is also used for frequency changes so we need to be able to manually specify a key.
     */
    public <T, C> void onUnloadExtractNode(ExtractorNodeBehaviour<T, C> leafNode) {
        this.onUnloadExtractNode(leafNode, new Key(leafNode.capabilityType(), leafNode.frequency()));
    }

    /**
     * Resets the node's cache of insert targets.
     */
    public <T, C> void onUnloadExtractNode(ExtractorNodeBehaviour<T, C> leafNode, Key oldKey) {
        //here we just reset the cache -> that way we avoid iterating the old ones
        leafNode.resetInsertTargets();
    }

    /**
     * Shorthand for loads called from the node itself.
     * Does not need to provide the key separately, because it is unchanged.
     * The main overload is also used for frequency changes so we need to be able to manually specify a key.
     */
    public <T, C> void onLoadInsertNode(InserterNodeBehaviour<T, C> leafNode) {
        this.onLoadInsertNode(leafNode, new Key(leafNode.capabilityType(), leafNode.frequency()));
    }

    /**
     * Informs live nodes to add us to their cache
     */
    public <T, C> void onLoadInsertNode(InserterNodeBehaviour<T, C> leafNode, Key newKey) {
        //we need to inform all our new nodes that they have to add us
        var newSet = this.keyToLeafNodes.get(newKey);
        for (var other : newSet) {
            if (other.equals(leafNode.globalPos())) {
                continue;
            }

            //noinspection unchecked -> our set ensures only compatible nodes are available
            var otherLeafNode = (LeafNodeBehaviour<T, C>) Logistics.get().getLeafNode(other, LeafNodeMode.EXTRACT);
            if (otherLeafNode == null) {
                continue;
            }

            var extractNode = otherLeafNode.asExtractor();
            extractNode.onLeafNodeAddedToGraph(leafNode.globalPos(), leafNode);
        }
    }

    /**
     * Shorthand for unloads called from the node itself.
     * Does not need to provide the key separately, because it is unchanged.
     * The main overload is also used for frequency changes so we need to be able to manually specify a key.
     */
    public <T, C> void onUnloadInsertNode(InserterNodeBehaviour<T, C> leafNode) {
        this.onUnloadInsertNode(leafNode, new Key(leafNode.capabilityType(), leafNode.frequency()));
    }

    /**
     * Informs live nodes to remove us from their cache.
     */
    public <T, C> void onUnloadInsertNode(InserterNodeBehaviour<T, C> leafNode, Key oldKey) {
        //we need to inform all our nodes that they have to remove us
        var oldSet = this.keyToLeafNodes.get(oldKey);
        for (var other : oldSet) {
            if (other.equals(leafNode.globalPos())) {
                continue;
            }

            //noinspection unchecked -> our set ensures only compatible nodes are available
            var otherLeafNode = (LeafNodeBehaviour<T, C>) Logistics.get().getLeafNode(other, LeafNodeMode.EXTRACT);
            if (otherLeafNode == null) {
                continue;
            }

            var extractNode = otherLeafNode.asExtractor();
            extractNode.onLeafNodeRemovedFromGraph(leafNode.globalPos(), leafNode);
        }
    }

    /**
     * Merges the other network into this one.
     */
    public void merge(LogisticsNetwork other) {
        this.nodes.addAll(other.nodes);
        this.leafNodes.addAll(other.leafNodes);
        this.keyToLeafNodes.putAll(other.keyToLeafNodes);
    }

    public record Key(BlockCapability<?, ?> capability, int frequency) {
    }
}
