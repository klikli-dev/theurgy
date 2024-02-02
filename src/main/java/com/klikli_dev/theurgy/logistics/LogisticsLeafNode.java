package com.klikli_dev.theurgy.logistics;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;

import java.util.List;

/**
 * A leaf node in the logistics graph is a node that interfaces with one or more target block entities - or rather, their capabilities.
 */
public interface LogisticsLeafNode<T, C> {

    //TODO: handle the case of a leaf node being loaded when its target is in another chunk -> needs to listen for chunk load to update its targets
    //      then needs to call the onTargetAddedToGraph

    /**
     * The targets of this leaf node, i.e. the block positions of the block entities this leaf node interfaces with.
     */
    List<BlockPos> targets();

    /**
     * The mode of this leaf node, i.e. if it is an insert or extract node.
     */
    LeafNodeMode mode();

    Level level();

    ServerLevel serverLevel();

    /**
     * Gets the capability type of this node.
     */
    BlockCapability<T, C> capabilityType();

    int frequency();

    default LogisticsExtractorNode<T, C> asExtractor() {
        return (LogisticsExtractorNode<T, C>) this;
    }

    default LogisticsInserterNode<T, C> asInserter() {
        return (LogisticsInserterNode<T, C>) this;
    }

    enum LeafNodeMode {
        INSERT,
        EXTRACT
        //TODO: we may need a mode "mirror" that just mirrors the capability to another node.
        //      in this node if we have multiple source/target nodes we'd need a function to e.g. round-robin between them,
        //      or allow the player to select a fixed target.
    }
}
