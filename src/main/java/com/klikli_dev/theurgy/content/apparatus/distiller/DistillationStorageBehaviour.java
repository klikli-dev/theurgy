// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.distiller;

import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.StorageBehaviour;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.content.storage.PreventInsertWrapper;
import com.klikli_dev.theurgy.registry.FluidTagRegistry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class DistillationStorageBehaviour extends StorageBehaviour<DistillationStorageBehaviour> {

    public ItemStackHandler inputInventory;
    /**
     * The underlying outputInventory which allows inserting too - we use this when crafting.
     */
    public ItemStackHandler outputInventory;
    /**
     * A wrapper that only allows taking from the outputInventory - this is what we show to the outside.
     */
    public PreventInsertWrapper outputInventoryTakeOnlyWrapper;

    public CombinedInvWrapper inventory;
    public LazyOptional<IItemHandler> inventoryCapability;
    public LazyOptional<IItemHandler> inputInventoryCapability;
    public LazyOptional<IItemHandler> outputInventoryCapability;

    public Supplier<DistillationCraftingBehaviour> craftingBehaviour;

    public DistillationStorageBehaviour(BlockEntity blockEntity, Supplier<DistillationCraftingBehaviour> craftingBehaviour) {
        super(blockEntity);

        this.craftingBehaviour = craftingBehaviour;

        this.inputInventory = new InputInventory();
        this.outputInventory = new OutputInventory();
        this.outputInventoryTakeOnlyWrapper = new PreventInsertWrapper(this.outputInventory);
        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventoryTakeOnlyWrapper);

        this.inventoryCapability = LazyOptional.of(() -> this.inventory);
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventoryTakeOnlyWrapper);

        this.register(this.inventoryCapability);
        this.register(this.inputInventoryCapability);
        this.register(this.outputInventoryCapability);
    }

    @Override
    public void readNetwork(CompoundTag pTag) {
        if (pTag.contains("inputInventory")) this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory")) this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));
    }

    @Override
    public void writeNetwork(CompoundTag pTag) {
        pTag.put("inputInventory", this.inputInventory.serializeNBT());
        pTag.put("outputInventory", this.outputInventory.serializeNBT());
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        this.writeNetwork(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        this.readNetwork(pTag);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == Capabilities.ITEM_HANDLER) {
            if (side == Direction.UP) return this.inputInventoryCapability.cast();
            if (side == Direction.DOWN) return this.outputInventoryCapability.cast();
            return this.inventoryCapability.cast();
        }

        return LazyOptional.empty();
    }

    public class InputInventory extends MonitoredItemStackHandler {

        public InputInventory() {
            super(1);
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            DistillationStorageBehaviour.this.craftingBehaviour.get().onInputItemChanged(oldStack, newStack);
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            DistillationStorageBehaviour.this.sendBlockUpdated();
        }


        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return DistillationStorageBehaviour.this.craftingBehaviour.get().canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            DistillationStorageBehaviour.this.setChanged();
        }
    }

    public class OutputInventory extends MonitoredItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            DistillationStorageBehaviour.this.sendBlockUpdated();
        }

        @Override
        protected void onContentsChanged(int slot) {
            DistillationStorageBehaviour.this.setChanged();
        }
    }
}
