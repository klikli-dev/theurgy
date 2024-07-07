// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.content.behaviour.storage.StorageBehaviour;
import com.klikli_dev.theurgy.content.storage.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class DigestionStorageBehaviour extends StorageBehaviour<DigestionStorageBehaviour> {

    public ItemStackHandler inputInventory;
    public IItemHandler inputInventoryReadOnlyWrapper;

    /**
     * The underlying outputInventory which allows inserting too - we use this when crafting.
     */
    public ItemStackHandler outputInventory;
    /**
     * A capability wrapper for the outputInventory that only allows extracting.
     */
    public IItemHandlerModifiable outputInventoryExtractOnlyWrapper;
    /**
     * A capability wrapper for the outputInventory that allows neither inserting nor extracting
     */
    public IItemHandler outputInventoryReadOnlyWrapper;

    public CombinedInvWrapper inventory;
    public IItemHandler inventoryReadOnlyWrapper;
    public FluidTank fluidTank;
    public IFluidHandler fluidTankReadOnlyWrapper;

    public Supplier<DigestionCraftingBehaviour> craftingBehaviour;

    public DigestionStorageBehaviour(BlockEntity blockEntity, Supplier<DigestionCraftingBehaviour> craftingBehaviour) {
        super(blockEntity);

        this.craftingBehaviour = craftingBehaviour;

        this.inputInventory = new InputInventory();
        this.inputInventoryReadOnlyWrapper = new PreventInsertExtractWrapper(this.inputInventory);

        this.outputInventory = new OutputInventory();
        this.outputInventoryExtractOnlyWrapper = new PreventInsertWrapper(this.outputInventory);
        this.outputInventoryReadOnlyWrapper = new PreventInsertExtractWrapper(this.outputInventory);

        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventoryExtractOnlyWrapper);
        this.inventoryReadOnlyWrapper = new PreventInsertExtractWrapper(this.inventory);

        this.fluidTank = new WaterTank(FluidType.BUCKET_VOLUME * 10, f -> this.craftingBehaviour.get().canProcess(f));
        this.fluidTankReadOnlyWrapper = new PreventInsertExtractFluidWrapper(this.fluidTank);
    }

    @Override
    public void readNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        if (pTag.contains("inputInventory")) this.inputInventory.deserializeNBT(pRegistries, pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory")) this.outputInventory.deserializeNBT(pRegistries, pTag.getCompound("outputInventory"));
        if (pTag.contains("fluidTank")) this.fluidTank.readFromNBT(pRegistries, pTag.getCompound("fluidTank"));
    }

    @Override
    public void writeNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inputInventory", this.inputInventory.serializeNBT(pRegistries));
        pTag.put("outputInventory", this.outputInventory.serializeNBT(pRegistries));
        pTag.put("fluidTank", this.fluidTank.writeToNBT(pRegistries, new CompoundTag()));
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

    public class WaterTank extends MonitoredFluidTank {

        public WaterTank(int capacity, Predicate<FluidStack> validator) {
            super(capacity, validator);
        }

        @Override
        protected void onContentsChanged() {
            //on any content change mark for saving to disk
            DigestionStorageBehaviour.this.setChanged();
        }

        @Override
        protected void onContentTypeChanged(FluidStack oldStack, FluidStack newStack) {
            //if the content type (different fluid or empty/nonempty) changes we sync to client to ensure consistent client side "use" results.
            DigestionStorageBehaviour.this.sendBlockUpdated();
        }
    }

    public class InputInventory extends MonitoredItemStackHandler {

        public InputInventory() {
            super(3);
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            DigestionStorageBehaviour.this.craftingBehaviour.get().onInputItemChanged(oldStack, newStack);
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            DigestionStorageBehaviour.this.sendBlockUpdated();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            //we only allow one item type to fill maximum one slot, so if another slot has the stack, return false.
            for (int i = 0; i < this.getSlots(); i++) {
                if (i != slot && ItemStack.isSameItemSameComponents(stack, this.getStackInSlot(i))) {
                    return false;
                }
            }

            return DigestionStorageBehaviour.this.craftingBehaviour.get().canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            DigestionStorageBehaviour.this.setChanged();
        }
    }

    public class OutputInventory extends MonitoredItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            DigestionStorageBehaviour.this.sendBlockUpdated();
            DigestionStorageBehaviour.this.blockEntity.getLevel().updateNeighbourForOutputSignal(DigestionStorageBehaviour.this.blockEntity.getBlockPos(), DigestionStorageBehaviour.this.blockEntity.getBlockState().getBlock());
        }

        @Override
        protected void onContentsChanged(int slot) {
            DigestionStorageBehaviour.this.setChanged();
        }
    }
}
