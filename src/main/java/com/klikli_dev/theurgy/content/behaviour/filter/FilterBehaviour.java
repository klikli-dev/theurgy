package com.klikli_dev.theurgy.content.behaviour.filter;

import com.klikli_dev.theurgy.content.item.filter.Filter;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

public class FilterBehaviour {

    protected BlockEntity blockEntity;
    protected Consumer<Filter> callback;

    protected Filter filter;

    public FilterBehaviour(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        this.callback = (stack) -> {
        };
        this.filter = Filter.empty();

        //TODO: In the callback, update the filter of the leaf node
        //TODO: in the callback, update the blockstate based on filter == empty or not
    }

    public Filter filter() {
        return this.filter;
    }

    public void filter(Filter filter) {
        this.filter = filter;
        this.callback.accept(filter);
    }

    public void withCallback(Consumer<Filter> callback) {
        this.callback = callback;
    }

    public void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.writeNetwork(pTag, pRegistries);
    }

    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.readNetwork(pTag, pRegistries);
    }

    public void writeNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("filter", this.filter.serializeNBT(pRegistries));
    }

    public void readNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.filter = Filter.of(pRegistries, pTag.getCompound("filter"));
    }
}
