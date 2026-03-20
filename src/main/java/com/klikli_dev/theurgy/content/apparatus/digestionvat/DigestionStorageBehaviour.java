// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.content.behaviour.storage.OutputStorageBehaviour;
import com.klikli_dev.theurgy.content.behaviour.storage.StorageBehaviour;
import com.klikli_dev.theurgy.content.storage.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class DigestionStorageBehaviour extends StorageBehaviour<DigestionStorageBehaviour> implements OutputStorageBehaviour {

    public ItemStackHandler inputInventory;
    public IItemHandlerModifiable inputInventoryReadOnlyWrapper;
    /**
     * Depending on the state of the vat returns either a read only, or an extractable handler.
     */
    public SelectItemHandlerWrapper inputInventoryOpenCloseAwareWrapper;

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
    public IItemHandlerModifiable outputInventoryReadOnlyWrapper;
    /**
     * Depending on the state of the vat returns either a read only, or an extractable handler.
     */
    public SelectItemHandlerWrapper outputInventoryOpenCloseAwareWrapper;

    public CombinedInvWrapper inventory;
    public IItemHandlerModifiable inventoryReadOnlyWrapper;
    /**
     * Depending on the state of the vat returns either a read only, or an extractable handler.
     */
    public SelectItemHandlerWrapper inventoryOpenCloseAwareWrapper;

    public FluidTank fluidTank;
    public IFluidHandler fluidTankReadOnlyWrapper;
    /**
     * Depending on the state of the vat returns either a read only, or an extractable handler.
     */
    public SelectFluidHandlerWrapper fluidTankCloseAwareWrapper;

    public Supplier<DigestionCraftingBehaviour> craftingBehaviour;

    public DigestionStorageBehaviour(BlockEntity blockEntity, Supplier<DigestionCraftingBehaviour> craftingBehaviour) {
        super(blockEntity);

        this.craftingBehaviour = craftingBehaviour;

        this.inputInventory = new InputInventory();
        this.inputInventoryReadOnlyWrapper = new PreventInsertExtractWrapper(this.inputInventory);
        this.inputInventoryOpenCloseAwareWrapper = new SelectItemHandlerWrapper(this.selectHandler(), this.inputInventory, this.inputInventoryReadOnlyWrapper);

        this.outputInventory = new OutputInventory();
        this.outputInventoryExtractOnlyWrapper = new PreventInsertWrapper(this.outputInventory);
        this.outputInventoryReadOnlyWrapper = new PreventInsertExtractWrapper(this.outputInventory);
        this.outputInventoryOpenCloseAwareWrapper = new SelectItemHandlerWrapper(this.selectHandler(), this.outputInventoryExtractOnlyWrapper, this.outputInventoryReadOnlyWrapper);

        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventoryExtractOnlyWrapper);
        this.inventoryReadOnlyWrapper = new PreventInsertExtractWrapper(this.inventory);
        this.inventoryOpenCloseAwareWrapper = new SelectItemHandlerWrapper(this.selectHandler(), this.inventory, this.inventoryReadOnlyWrapper);

        this.fluidTank = new WaterTank(FluidType.BUCKET_VOLUME * 10, f -> this.craftingBehaviour.get().canProcess(f));
        this.fluidTankReadOnlyWrapper = new PreventInsertExtractFluidWrapper(this.fluidTank);
        this.fluidTankCloseAwareWrapper = new SelectFluidHandlerWrapper(this.selectHandler(), this.fluidTank, this.fluidTankReadOnlyWrapper);
    }

    private Supplier<Integer> selectHandler() {
        return () -> this.blockEntity.getBlockState().getValue(BlockStateProperties.OPEN) ? 0 : 1;
    }

    @Override
    public void readNetwork(ValueInput input) {
        input.child("inputInventory").ifPresent(tag -> this.inputInventory.deserialize(tag));
        input.child("outputInventory").ifPresent(tag -> this.outputInventory.deserialize(tag));
        input.child("fluidTank").ifPresent(tag -> this.fluidTank.deserialize(tag));
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

        ValueOutput fluidTankOutput = output.child("fluidTank");
        this.fluidTank.serialize(fluidTankOutput);
        if (fluidTankOutput.isEmpty()) {
            output.discard("fluidTank");
        }
    }

    @Override
    public void loadAdditional(ValueInput input) {
        this.readNetwork(input);
    }

    @Override
    public boolean hasOutput() {
        return !this.outputInventory.getStackInSlot(0).isEmpty();
    }

    @Override
    public IItemHandler outputInventory() {
        return this.outputInventory;
    }

    public class WaterTank extends MonitoredFluidTank {

        public WaterTank(int capacity, Predicate<FluidStack> validator) {
            super(capacity, validator);
        }

        @Override
        protected void onContentsChanged() {
            DigestionStorageBehaviour.this.craftingBehaviour.get().onInputChanged();
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
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
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
            DigestionStorageBehaviour.this.craftingBehaviour.get().onInputChanged();
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

            //also update redstone state for neighbors, because redstone output depends on output inventory contents
            DigestionStorageBehaviour.this.blockEntity.getLevel().updateNeighborsAt(DigestionStorageBehaviour.this.blockEntity.getBlockPos(), DigestionStorageBehaviour.this.blockEntity.getBlockState().getBlock());
        }

        @Override
        protected void onContentsChanged(int slot) {
            DigestionStorageBehaviour.this.setChanged();
        }
    }
}
