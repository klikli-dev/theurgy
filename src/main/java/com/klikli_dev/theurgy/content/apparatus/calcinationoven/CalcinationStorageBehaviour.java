// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.calcinationoven;

import com.klikli_dev.theurgy.content.behaviour.storage.StorageBehaviour;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.content.storage.PreventInsertWrapper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.function.Supplier;

public class CalcinationStorageBehaviour extends StorageBehaviour<CalcinationStorageBehaviour> {

    public ItemStackHandler inputInventory;

    /**
     * The underlying outputInventory which allows inserting too - we use this when crafting.
     */
    public ItemStackHandler outputInventory;
    /**
     * A wrapper that only allows taking from the outputInventory - this is what we show to the outside.
     */
    public PreventInsertWrapper outputInventoryExtractOnlyWrapper;

    public CombinedInvWrapper inventory;

    public Supplier<CalcinationCraftingBehaviour> craftingBehaviour;

    public CalcinationStorageBehaviour(BlockEntity blockEntity, Supplier<CalcinationCraftingBehaviour> craftingBehaviour) {
        super(blockEntity);

        this.craftingBehaviour = craftingBehaviour;

        this.inputInventory = new InputInventory();

        this.outputInventory = new OutputInventory();
        this.outputInventoryExtractOnlyWrapper = new PreventInsertWrapper(this.outputInventory);

        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventoryExtractOnlyWrapper);
    }

    @Override
    public void readNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        if (pTag.contains("inputInventory")) this.inputInventory.deserializeNBT(pRegistries, pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory")) this.outputInventory.deserializeNBT(pRegistries, pTag.getCompound("outputInventory"));
    }

    @Override
    public void writeNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inputInventory", this.inputInventory.serializeNBT(pRegistries));
        pTag.put("outputInventory", this.outputInventory.serializeNBT(pRegistries));
    }

    @Override
    public void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.writeNetwork(pTag, pRegistries);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.readNetwork(pTag, pRegistries);
    }

    @Override
    public boolean hasOutput() {
        return !this.outputInventory.getStackInSlot(0).isEmpty();
    }

    public class InputInventory extends MonitoredItemStackHandler {

        public InputInventory() {
            super(1);
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            CalcinationStorageBehaviour.this.craftingBehaviour.get().onInputItemChanged(oldStack, newStack);
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            CalcinationStorageBehaviour.this.sendBlockUpdated();
        }


        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return CalcinationStorageBehaviour.this.craftingBehaviour.get().canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            CalcinationStorageBehaviour.this.setChanged();
        }
    }

    public class OutputInventory extends MonitoredItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            CalcinationStorageBehaviour.this.sendBlockUpdated();
        }

        @Override
        protected void onContentsChanged(int slot) {
            CalcinationStorageBehaviour.this.setChanged();
        }
    }
}
