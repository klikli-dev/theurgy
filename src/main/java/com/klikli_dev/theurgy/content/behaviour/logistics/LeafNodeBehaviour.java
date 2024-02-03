package com.klikli_dev.theurgy.content.behaviour.logistics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;

import java.util.List;

/**
 * A leaf node in the logistics graph is a node that interfaces with one or more target block entities - or rather, their capabilities.
 */
public abstract class LeafNodeBehaviour<T, C> {

    //TODO: handle the case of a leaf node being loaded when its target is in another chunk -> needs to listen for chunk load to update its targets
    //      then needs to call the onTargetAddedToGraph

    //TODO: add some basic logic to the behaviour, including storage, nbt serialization, etc

    //TODO: leaf node needs to listen to neighbor chunk load/unload to detect target changes if not in the same chunk
    //      target unload probably doable via the capability cache

    //TODO: target removal: probably not a problem for Logistics.get(), but rather just locally to the leaf node.
    //      the leaf node reports the target unload, and never reports a target load -> because removed
    //      for the logistics system that does not matter, and the node itself can e.g. show an error message if status checked
    //      the general path will then likely be to just remove the node, link it and re-add it.
    //      t1 nodes like the connector will likely just get destroyed when the target is removed anyway.


    /**
     * The targets of this leaf node, i.e. the block positions of the block entities this leaf node interfaces with.
     */
    public abstract List<BlockPos> targets();

    public abstract GlobalPos globalPos();

    /**
     * The mode of this leaf node, i.e. if it is an insert or extract node.
     */
    public abstract LeafNodeMode mode();

    public abstract Level level();

    /**
     * Gets the capability type of this node.
     */
    public abstract BlockCapability<T, C> capabilityType();

    public abstract int frequency();

    public ExtractorNodeBehaviour<T, C> asExtractor() {
        return (ExtractorNodeBehaviour<T, C>) this;
    }

    public InserterNodeBehaviour<T, C> asInserter() {
        return (InserterNodeBehaviour<T, C>) this;
    }

}
