// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.logistics;

import com.klikli_dev.theurgy.content.behaviour.filter.FilterBehaviour;
import com.klikli_dev.theurgy.content.behaviour.filter.HasFilterBehaviour;
import com.klikli_dev.theurgy.content.item.filter.Filter;
import com.klikli_dev.theurgy.logistics.Logistics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.ArrayList;
import java.util.List;

/**
 * A leaf node in the logistics graph is a node that interfaces with one or more target block entities - or rather, their capabilities.
 */
public abstract class LeafNodeBehaviour<T, C> {

    //TODO: target removal: probably not a problem for Logistics.get(), but rather just locally to the leaf node.
    //      the leaf node reports the target unload, and never reports a target load -> because removed
    //      for the logistics system that does not matter, and the node itself can e.g. show an error message if status checked
    //      the general path will then likely be to just remove the node, link it and re-add it.
    //      t1 nodes like the connector will likely just get destroyed when the target is removed anyway.

    protected BlockEntity blockEntity;
    protected Lazy<GlobalPos> globalPos;
    protected BlockCapability<T, C> capabilityType;
    protected Filter filter;
    /**
     * The block this node makes accessible to the network (e.g. by being attached to a block).
     * Child node behaviours can e.g. build insert/extract target lists from this list.
     *
     * For the most basic leaf nodes the target list is simply the block they are attached to.
     */
    protected List<BlockPos> targets;
    protected int frequency;

    public LeafNodeBehaviour(BlockEntity blockEntity, BlockCapability<T, C> capabilityType) {
        this.blockEntity = blockEntity;
        this.globalPos = Lazy.of(() -> GlobalPos.of(this.level().dimension(), this.blockEntity.getBlockPos())); //will be initialized lazily
        this.capabilityType = capabilityType;
        this.targets = new ArrayList<>();
        this.frequency = 0;
        this.filter = Filter.empty();
    }

    public Level level() {
        return this.blockEntity.getLevel();
    }

    public GlobalPos globalPos() {
        return this.globalPos.get();
    }

    public Filter filter(){
        return this.filter;
    }

    public void filter(Filter filter){
        this.filter = filter;
    }

    /**
     * Gets the capability type of this node.
     */
    public BlockCapability<T, C> capabilityType() {
        return this.capabilityType;
    }

    /**
     * The targets of this leaf node, i.e. the block positions of the block entities this leaf node interfaces with.
     */
    public List<BlockPos> targets() {
        return this.targets;
    }

    public int frequency() {
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
    public void onLoad() {
        Logistics.get().add(this);
    }

    /**
     * Call from BlockEntity.onChunkUnload()
     * This is called when the chunk is unloaded.
     */
    public void onChunkUnload() {
        Logistics.get().remove(this, false);
    }

    /**
     * Call from Block.onRemove(), it is only called if a block is destroyed.
     * Do not call from BlockEntity.setRemoved()
     * -> that is also called on chunk unload and would permanently disconnect the node from its network.
     */
    public void onDestroyed() {
        Logistics.get().remove(this, true);
    }

    public void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.writeNetwork(pTag, pRegistries);
    }

    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.readNetwork(pTag, pRegistries);
    }

    public void writeNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.putInt("frequency", this.frequency);
        var list = new ListTag();
        for (var target : this.targets) {
            list.add(LongTag.valueOf(target.asLong()));
        }
        pTag.put("targets", list);
    }

    public void readNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.frequency = pTag.getInt("frequency");
        this.targets = new ArrayList<>();
        var list = pTag.getList("targets", Tag.TAG_LONG);
        for (int i = 0; i < list.size(); i++) {
            this.targets.add(BlockPos.of(((LongTag) list.get(i)).getAsLong()));
        }
    }

    /**
     * The mode of this leaf node, i.e. if it is an insert or extract node.
     */
    public abstract LeafNodeMode mode();

    public abstract C getTargetContext(BlockPos targetPos);

}
