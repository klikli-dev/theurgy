// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour;

import com.klikli_dev.theurgy.util.TetraConsumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class StorageBehaviour<S extends StorageBehaviour<?>> {
    protected BlockEntity blockEntity;
    protected TetraConsumer<IItemHandler, Integer, ItemStack, ItemStack> onContentTypeChanged;
    protected BiConsumer<IItemHandler, Integer> onContentsChanged;
    protected Consumer<IFluidHandler> onFluidContentsChanged;

    public StorageBehaviour(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    protected void onContentsChanged(IItemHandler handler, int slot) {
        if (this.onContentsChanged != null)
            this.onContentsChanged.accept(handler, slot);
    }

    protected void onContentTypeChanged(IItemHandler handler, int slot, ItemStack oldStack, ItemStack newStack) {
        if (this.onContentTypeChanged != null)
            this.onContentTypeChanged.accept(handler, slot, oldStack, newStack);
    }

    protected void onFluidContentsChanged(IFluidHandler handler) {
        if (this.onFluidContentsChanged != null)
            this.onFluidContentsChanged.accept(handler);
    }

    public S withOnContentTypeChanged(TetraConsumer<IItemHandler, Integer, ItemStack, ItemStack> onContentTypeChanged) {
        this.onContentTypeChanged = onContentTypeChanged;
        //noinspection unchecked
        return (S) this;
    }

    public S withOnContentsChanged(BiConsumer<IItemHandler, Integer> onContentsChanged) {
        this.onContentsChanged = onContentsChanged;
        //noinspection unchecked
        return (S) this;
    }

    public S onFluidContentsChanged(Consumer<IFluidHandler> onFluidContentsChanged) {
        this.onFluidContentsChanged = onFluidContentsChanged;
        //noinspection unchecked
        return (S) this;
    }

    public abstract void readNetwork(CompoundTag pTag);

    public abstract void writeNetwork(CompoundTag pTag);

    public abstract void saveAdditional(CompoundTag pTag);

    public abstract void load(CompoundTag pTag);

    protected void sendBlockUpdated() {
        if (this.blockEntity.getLevel() != null && !this.blockEntity.getLevel().isClientSide)
            this.blockEntity.getLevel().sendBlockUpdated(this.blockEntity.getBlockPos(), this.blockEntity.getBlockState(), this.blockEntity.getBlockState(), Block.UPDATE_CLIENTS);
    }

    protected void setChanged() {
        this.blockEntity.setChanged();
    }
}
