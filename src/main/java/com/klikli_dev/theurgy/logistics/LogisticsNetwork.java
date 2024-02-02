package com.klikli_dev.theurgy.logistics;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
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
    //TODO: handle frequency changes
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

    public void addLeafNode(GlobalPos pos, LogisticsLeafNode<?, ?> leafNode) {
        this.leafNodes.add(pos);
        this.keyToLeafNodes.put(new Key(leafNode.capabilityType(), leafNode.frequency()), pos);
    }

    public void onFrequencyChange(GlobalPos pos, BlockCapability<?, ?> capability, int oldFrequency, int newFrequency) {
        var oldKey = new Key(capability, oldFrequency);
        var newKey = new Key(capability, newFrequency);

        //TODO: need to notify the other nodes of the frequency change so they can update their cache
        //      specifically we can just directly call the add/remove leaf node methods

        this.keyToLeafNodes.remove(oldKey, pos);
        this.keyToLeafNodes.put(newKey, pos);
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
