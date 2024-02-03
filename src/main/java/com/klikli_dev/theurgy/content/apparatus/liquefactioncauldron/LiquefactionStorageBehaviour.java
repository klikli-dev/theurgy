// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron;

import com.klikli_dev.theurgy.content.behaviour.storage.StorageBehaviour;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.content.storage.PreventInsertWrapper;
import com.klikli_dev.theurgy.registry.FluidTagRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class LiquefactionStorageBehaviour extends StorageBehaviour<LiquefactionStorageBehaviour> {

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

    public FluidTank solventTank;

    public Supplier<LiquefactionCraftingBehaviour> craftingBehaviour;

    public LiquefactionStorageBehaviour(BlockEntity blockEntity, Supplier<LiquefactionCraftingBehaviour> craftingBehaviour) {
        super(blockEntity);

        this.craftingBehaviour = craftingBehaviour;

        this.inputInventory = new InputInventory();
        this.outputInventory = new OutputInventory();
        this.outputInventoryTakeOnlyWrapper = new PreventInsertWrapper(this.outputInventory);
        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventoryTakeOnlyWrapper);
        this.solventTank = new SolventTank(FluidType.BUCKET_VOLUME * 2, (fluidStack -> fluidStack.getFluid().is(FluidTagRegistry.SOLVENT)));
    }

    @Override
    public void readNetwork(CompoundTag pTag) {
        if (pTag.contains("inputInventory")) this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory")) this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));
        if (pTag.contains("solventTank")) this.solventTank.readFromNBT(pTag.getCompound("solventTank"));
    }

    @Override
    public void writeNetwork(CompoundTag pTag) {
        pTag.put("inputInventory", this.inputInventory.serializeNBT());
        pTag.put("outputInventory", this.outputInventory.serializeNBT());
        pTag.put("solventTank", this.solventTank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        this.writeNetwork(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        this.readNetwork(pTag);
    }

    public class SolventTank extends FluidTank {

        public SolventTank(int capacity, Predicate<FluidStack> validator) {
            super(capacity, validator);
        }

        @Override
        protected void onContentsChanged() {
            LiquefactionStorageBehaviour.this.setChanged();
            LiquefactionStorageBehaviour.this.sendBlockUpdated();
        }
    }

    public class InputInventory extends MonitoredItemStackHandler {

        public InputInventory() {
            super(1);
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            LiquefactionStorageBehaviour.this.craftingBehaviour.get().onInputItemChanged(oldStack, newStack);
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            LiquefactionStorageBehaviour.this.sendBlockUpdated();
        }


        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return LiquefactionStorageBehaviour.this.craftingBehaviour.get().canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            LiquefactionStorageBehaviour.this.setChanged();
        }
    }

    public class OutputInventory extends MonitoredItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            LiquefactionStorageBehaviour.this.sendBlockUpdated();
        }

        @Override
        protected void onContentsChanged(int slot) {
            LiquefactionStorageBehaviour.this.setChanged();
        }
    }
}
