// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron;

import com.klikli_dev.theurgy.content.behaviour.storage.StorageBehaviour;
import com.klikli_dev.theurgy.content.storage.CombinedItemStorage;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.content.storage.PreventInsertWrapper;
import com.klikli_dev.theurgy.registry.FluidTagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class LiquefactionStorageBehaviour extends StorageBehaviour<LiquefactionStorageBehaviour> {

    public InputInventory inputInventory;
    /**
     * The underlying outputInventory which allows inserting too - we use this when crafting.
     */
    public OutputInventory outputInventory;
    /**
     * A wrapper that only allows taking from the outputInventory - this is what we show to the outside.
     */
    public PreventInsertWrapper outputInventoryTakeOnlyWrapper;

    public CombinedItemStorage inventory;

    public FluidTank solventTank;

    public Supplier<LiquefactionCraftingBehaviour> craftingBehaviour;

    public LiquefactionStorageBehaviour(BlockEntity blockEntity, Supplier<LiquefactionCraftingBehaviour> craftingBehaviour) {
        super(blockEntity);

        this.craftingBehaviour = craftingBehaviour;

        this.inputInventory = new InputInventory();
        this.outputInventory = new OutputInventory();
        this.outputInventoryTakeOnlyWrapper = new PreventInsertWrapper(this.outputInventory);
        this.inventory = new CombinedItemStorage(this.inputInventory, this.outputInventoryTakeOnlyWrapper);
        this.solventTank = new SolventTank(FluidType.BUCKET_VOLUME * 2, (fluidStack -> fluidStack.getFluid().is(FluidTagRegistry.SOLVENT)));
    }

    @Override
    public void readNetwork(ValueInput input) {
        input.child("inputInventory").ifPresent(tag -> this.inputInventory.deserialize(tag));
        input.child("outputInventory").ifPresent(tag -> this.outputInventory.deserialize(tag));
        input.child("solventTank").ifPresent(tag -> this.solventTank.deserialize(tag));
    }

    @Override
    public void writeNetwork(ValueOutput output) {
        this.saveAdditional(output);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        ValueOutput inputInventoryOutput = output.child("inputInventory");
        this.inputInventory.serialize(inputInventoryOutput);
        if (inputInventoryOutput.isEmpty()) {
            output.discard("inputInventory");
        }

        ValueOutput outputInventoryOutput = output.child("outputInventory");
        this.outputInventory.serialize(outputInventoryOutput);
        if (outputInventoryOutput.isEmpty()) {
            output.discard("outputInventory");
        }

        ValueOutput solventTankOutput = output.child("solventTank");
        this.solventTank.serialize(solventTankOutput);
        if (solventTankOutput.isEmpty()) {
            output.discard("solventTank");
        }
    }

    @Override
    public void loadAdditional(ValueInput input) {
        this.readNetwork(input);
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
