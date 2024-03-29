// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.content.behaviour.*;
import com.klikli_dev.theurgy.content.storage.*;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class DigestionStorageBehaviour extends StorageBehaviour<DigestionStorageBehaviour> {

    public ItemStackHandler inputInventory;
    public LazyOptional<IItemHandler> inputInventoryCapability;
    public LazyOptional<IItemHandler> inputInventoryReadOnlyCapability;

    /**
     * The underlying outputInventory which allows inserting too - we use this when crafting.
     */
    public ItemStackHandler outputInventory;
    /**
     * A capability wrapper for the outputInventory that only allows extracting.
     */
    public LazyOptional<IItemHandler> outputInventoryExtractOnlyCapability;
    /**
     * A capability wrapper for the outputInventory that allows neither inserting nor extracting
     */
    public LazyOptional<IItemHandler> outputInventoryReadOnlyCapability;

    public CombinedInvWrapper inventory;
    public LazyOptional<IItemHandler> inventoryCapability;
    public LazyOptional<IItemHandler> inventoryReadOnlyCapability;
    public FluidTank fluidTank;
    public LazyOptional<IFluidHandler> fluidTankCapability;
    public LazyOptional<IFluidHandler> fluidTankReadOnlyCapability;

    public Supplier<DigestionCraftingBehaviour> craftingBehaviour;

    public DigestionStorageBehaviour(BlockEntity blockEntity, Supplier<DigestionCraftingBehaviour> craftingBehaviour) {
        super(blockEntity);

        this.craftingBehaviour = craftingBehaviour;

        this.inputInventory = new InputInventory();
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);
        this.inputInventoryReadOnlyCapability = LazyOptional.of(() -> new PreventInsertExtractWrapper(this.inputInventory));

        this.outputInventory = new OutputInventory();
        var outputInventoryTakeOnlyWrapper = new PreventInsertWrapper(this.outputInventory);
        this.outputInventoryExtractOnlyCapability = LazyOptional.of(() -> outputInventoryTakeOnlyWrapper);
        this.outputInventoryReadOnlyCapability = LazyOptional.of(() -> new PreventInsertExtractWrapper(this.outputInventory));

        this.inventory = new CombinedInvWrapper(this.inputInventory, outputInventoryTakeOnlyWrapper);
        this.inventoryCapability = LazyOptional.of(() -> this.inventory);
        this.inventoryReadOnlyCapability = LazyOptional.of(() -> new PreventInsertExtractWrapper(this.inventory));

        this.fluidTank = new WaterTank(FluidType.BUCKET_VOLUME * 10, f -> this.craftingBehaviour.get().canProcess(f));
        this.fluidTankCapability = LazyOptional.of(() -> this.fluidTank);
        this.fluidTankReadOnlyCapability = LazyOptional.of(() -> new PreventInsertExtractFluidWrapper(this.fluidTank));

        this.register(this.inventoryCapability);
        this.register(this.inputInventoryCapability);
        this.register(this.inventoryReadOnlyCapability);
        this.register(this.fluidTankCapability);
        this.register(this.fluidTankReadOnlyCapability);
        this.register(this.outputInventoryExtractOnlyCapability);
        this.register(this.outputInventoryReadOnlyCapability);
    }

    @Override
    public void readNetwork(CompoundTag pTag) {
        if (pTag.contains("inputInventory")) this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory")) this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));
        if (pTag.contains("fluidTank")) this.fluidTank.readFromNBT(pTag.getCompound("fluidTank"));
    }

    @Override
    public void writeNetwork(CompoundTag pTag) {
        pTag.put("inputInventory", this.inputInventory.serializeNBT());
        pTag.put("outputInventory", this.outputInventory.serializeNBT());
        pTag.put("fluidTank", this.fluidTank.writeToNBT(new CompoundTag()));
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

        var isOpen = this.blockEntity.getBlockState().getValue(BlockStateProperties.OPEN);

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP)
                return isOpen ? this.inputInventoryCapability.cast() : this.inputInventoryReadOnlyCapability.cast();
            if (side == Direction.DOWN)
                return isOpen ? this.outputInventoryExtractOnlyCapability.cast() : this.outputInventoryReadOnlyCapability.cast();
            return isOpen ? this.inventoryCapability.cast() : this.inventoryReadOnlyCapability.cast();
        }

        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return isOpen ? this.fluidTankCapability.cast() : this.fluidTankReadOnlyCapability.cast();

        return LazyOptional.empty();
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
                if (i != slot && ItemHandlerHelper.canItemStacksStack(stack, this.getStackInSlot(i))) {
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
        }

        @Override
        protected void onContentsChanged(int slot) {
            DigestionStorageBehaviour.this.setChanged();
        }
    }
}
