package com.klikli_dev.theurgy.content.behaviour.logistics;

import com.klikli_dev.theurgy.logistics.Logistics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.common.util.Lazy;

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

    protected BlockEntity blockEntity;
    protected Lazy<GlobalPos> globalPos;
    protected BlockCapability<T, C> capabilityType;
    protected List<BlockPos> targets;
    protected int frequency;

    public LeafNodeBehaviour(BlockEntity blockEntity, BlockCapability<T, C> capabilityType){
        this.blockEntity = blockEntity;
        this.globalPos = Lazy.of(() -> GlobalPos.of(this.level().dimension(), this.blockEntity.getBlockPos())); //will be initialized lazily
        this.capabilityType = capabilityType;
        this.targets = List.of();
        this.frequency = 0;
    }

    public Level level(){
        return this.blockEntity.getLevel();
    }

    public GlobalPos globalPos(){
        return this.globalPos.get();
    }

    /**
     * Gets the capability type of this node.
     */
    public BlockCapability<T, C> capabilityType(){
        return this.capabilityType;
    }

    /**
     * The targets of this leaf node, i.e. the block positions of the block entities this leaf node interfaces with.
     */
    public List<BlockPos> targets(){
        return this.targets;
    }

    public int frequency(){
        return this.frequency;
    }


    public ExtractorNodeBehaviour<T, C> asExtractor() {
        return (ExtractorNodeBehaviour<T, C>) this;
    }

    public InserterNodeBehaviour<T, C> asInserter() {
        return (InserterNodeBehaviour<T, C>) this;
    }

    /**
     * Call from BlockEntity.onLoad()
     * This is called before the first tick of the BE after a chunk was loaded.
     */
    public void onLoad(){
        Logistics.get().add(this);
    }

    /**
     * Call from BlockEntity.onChunkUnload()
     * This is called when the chunk is unloaded.
     */
    public void onChunkUnload(){
        Logistics.get().remove(this, false);
    }

    /**
     * Call from Block.onRemove(), it is only called if a block is destroyed.
     * Do not call from BlockEntity.setRemoved()
     *      -> that is also called on chunk unload and would permanently disconnect the node from its network.
     */
    public void onDestroyed(){
        Logistics.get().remove(this, true);
    }

    /**
     * The mode of this leaf node, i.e. if it is an insert or extract node.
     */
    public abstract LeafNodeMode mode();
}
