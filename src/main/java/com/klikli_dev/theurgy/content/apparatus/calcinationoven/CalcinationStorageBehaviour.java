// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.calcinationoven;

import com.klikli_dev.theurgy.content.behaviour.storage.StorageBehaviour;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.content.storage.PreventInsertWrapper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.function.Supplier;

public class CalcinationStorageBehaviour extends StorageBehaviour<CalcinationStorageBehaviour> {

    public InputInventory inputInventory;

    /**
     * The underlying outputInventory which allows inserting too - we use this when crafting.
     */
    public OutputInventory outputInventory;
    /**
     * A wrapper that only allows taking from the outputInventory - this is what we show to the outside.
     */
    public PreventInsertWrapper outputInventoryExtractOnlyWrapper;

    public CombinedResourceHandler<ItemResource> inventory;

    public Supplier<CalcinationCraftingBehaviour> craftingBehaviour;

    public CalcinationStorageBehaviour(BlockEntity blockEntity, Supplier<CalcinationCraftingBehaviour> craftingBehaviour) {
        super(blockEntity);

        this.craftingBehaviour = craftingBehaviour;

        this.inputInventory = new InputInventory();

        this.outputInventory = new OutputInventory();
        this.outputInventoryExtractOnlyWrapper = new PreventInsertWrapper(this.outputInventory);

        this.inventory = new CombinedResourceHandler<>(this.inputInventory, this.outputInventoryExtractOnlyWrapper);
    }

    @Override
    public void readNetwork(ValueInput input) {
        input.child("inputInventory").ifPresent(tag -> this.inputInventory.deserialize(tag));
        input.child("outputInventory").ifPresent(tag -> this.outputInventory.deserialize(tag));
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
    }

    @Override
    public void loadAdditional(ValueInput input) {
        this.readNetwork(input);
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
        public boolean isValid(int index, ItemResource resource) {
            return CalcinationStorageBehaviour.this.craftingBehaviour.get().canProcess(resource.toStack(1));
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
