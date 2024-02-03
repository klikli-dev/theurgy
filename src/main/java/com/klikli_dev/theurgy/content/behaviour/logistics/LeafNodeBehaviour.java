package com.klikli_dev.theurgy.content.behaviour.logistics;

import net.minecraft.core.BlockPos;
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

    /**
     * The targets of this leaf node, i.e. the block positions of the block entities this leaf node interfaces with.
     */
    public abstract List<BlockPos> targets();

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
