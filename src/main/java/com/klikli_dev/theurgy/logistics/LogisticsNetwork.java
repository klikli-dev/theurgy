package com.klikli_dev.theurgy.logistics;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
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
    private final Set<BlockPos> nodes = new ObjectOpenHashSet<>();
    private final Set<BlockPos> leafNodes = new ObjectOpenHashSet<>();
    private final SetMultimap<Key, BlockPos> keyToLeafNodes = HashMultimap.create();

    public Set<BlockPos> nodes() {
        return this.nodes;
    }

    public Set<BlockPos> getLeafNodes(Key key) {
        return this.keyToLeafNodes.get(key);
    }

    public Set<BlockPos> getLeafNodes(BlockCapability<?, ?> capability, int frequency) {
        return this.getLeafNodes(new Key(capability, frequency));
    }

    public void addNode(BlockPos pos) {
        this.nodes.add(pos);
    }

    public void addLeafNode(BlockPos pos, LogisticsLeafNode<?, ?> leafNode) {
        this.leafNodes.add(pos);
        this.keyToLeafNodes.put(new Key(leafNode.capabilityType(), leafNode.frequency()), pos);
    }

    public void onFrequencyChange(BlockPos pos, BlockCapability<?, ?> capability, int oldFrequency, int newFrequency) {
        var oldKey = new Key(capability, oldFrequency);
        var newKey = new Key(capability, newFrequency);

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
