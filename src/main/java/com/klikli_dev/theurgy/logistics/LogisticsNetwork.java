package com.klikli_dev.theurgy.logistics;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
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

    public void addLeafNode(GlobalPos pos, LeafNodeBehaviour<?, ?> leafNode) {
        this.leafNodes.add(pos);
        this.keyToLeafNodes.put(new Key(leafNode.capabilityType(), leafNode.frequency()), pos);
    }

    public <T, C> void onFrequencyChange(GlobalPos pos, LeafNodeBehaviour<T, C> leafNode, BlockCapability<T, C> capability, int oldFrequency, int newFrequency) {
        var oldKey = new Key(capability, oldFrequency);
        var newKey = new Key(capability, newFrequency);

        //TODO: handle frequency changes


        //TODO: need to notify the other nodes of the frequency change so they can update their cache
        //      specifically we can just directly call the add/remove leaf node methods

        //TODO: how do we handle unloaded nodes? -> they will query their cache on load

        this.keyToLeafNodes.remove(oldKey, pos);
        this.keyToLeafNodes.put(newKey, pos);

        //TODO: if the changed node is an insert node -> we need to update the extract nodes that are connected to it
        //      if the changed node is an extract one, we need it to re-query all its targets and update its cache, but the other nodes need not be informed


        if (leafNode.mode() == LeafNodeMode.INSERT) {
            this.onInsertNodeFrequencyChange(pos, leafNode, oldKey, newKey);
        }


    }

    protected <T, C> void onExtractNodeFrequencyChange(GlobalPos pos, LeafNodeBehaviour<T, C> leafNode, Key oldKey, Key newKey) {
        
    }

    protected <T, C> void onInsertNodeFrequencyChange(GlobalPos pos, LeafNodeBehaviour<T, C> leafNode, Key oldKey, Key newKey) {
        var oldSet = this.keyToLeafNodes.get(oldKey);
        for (var other : oldSet) {
            if (other.equals(pos)) {
                continue;
            }

            //noinspection unchecked -> our set ensures only compatible nodes are available
            var otherLeafNode = (LeafNodeBehaviour<T, C>) Logistics.get().getLeafNode(other, LeafNodeMode.EXTRACT);
            if (otherLeafNode == null) {
                continue;
            }

            var extractNode = otherLeafNode.asExtractor();
            extractNode.onLeafNodeRemovedFromGraph(pos, leafNode);
        }

        var newSet = this.keyToLeafNodes.get(newKey);
        for (var other : newSet) {
            if (other.equals(pos)) {
                continue;
            }

            //noinspection unchecked -> our set ensures only compatible nodes are available
            var otherLeafNode = (LeafNodeBehaviour<T, C>) Logistics.get().getLeafNode(other, LeafNodeMode.EXTRACT);
            if (otherLeafNode == null) {
                continue;
            }

            var extractNode = otherLeafNode.asExtractor();
            extractNode.onLeafNodeAddedToGraph(pos, leafNode);
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
