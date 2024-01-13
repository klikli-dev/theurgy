// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.content.behaviour.*;
import com.klikli_dev.theurgy.content.recipe.DigestionRecipe;
import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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

public class DigestionVatBlockEntity extends BlockEntity implements HasCraftingBehaviour<RecipeWrapperWithFluid, DigestionRecipe, DigestionCachedCheck> {

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

    public DigestionCraftingBehaviour craftingBehaviour;

    public DigestionVatBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.DIGESTION_VAT.get(), pPos, pBlockState);

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

        this.craftingBehaviour = new DigestionCraftingBehaviour(this, () -> this.inputInventory, () -> this.outputInventory, () -> this.fluidTank);

        this.fluidTank = new WaterTank(FluidType.BUCKET_VOLUME * 10, this.craftingBehaviour::canProcess);
        this.fluidTankCapability = LazyOptional.of(() -> this.fluidTank);
        this.fluidTankReadOnlyCapability = LazyOptional.of(() -> new PreventInsertExtractFluidWrapper(this.fluidTank));
    }

    public void tickServer() {
        //TODO: isProcessing syncs to client - should we act on that?
        //      a bubbling sound would be good
        //      we may be able to just use the closed state clientside to not have to tick the TE -> however, not really a relevant optimization
        boolean isOpen = this.getBlockState().getValue(BlockStateProperties.OPEN);
        boolean hasInput = this.hasInput();

        this.craftingBehaviour.tickServer(!isOpen, hasInput);

        if (!this.craftingBehaviour.isProcessing() && !isOpen) {
            this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(BlockStateProperties.OPEN, true), Block.UPDATE_CLIENTS);
        }
    }

    public Direction getDirection() {
        return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
    }

    public boolean hasInput() {
        for (int i = 0; i < this.inputInventory.getSlots(); i++) {
            if (!this.inputInventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        var isOpen = this.getBlockState().getValue(BlockStateProperties.OPEN);

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP)
                return isOpen ? this.inputInventoryCapability.cast() : this.inputInventoryReadOnlyCapability.cast();
            if (side == Direction.DOWN)
                return isOpen ? this.outputInventoryExtractOnlyCapability.cast() : this.outputInventoryReadOnlyCapability.cast();
            return isOpen ? this.inventoryCapability.cast() : this.inventoryReadOnlyCapability.cast();
        }

        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return isOpen ? this.fluidTankCapability.cast() : this.fluidTankReadOnlyCapability.cast();

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.inventoryCapability.invalidate();
        this.inventoryReadOnlyCapability.invalidate();
        this.inputInventoryCapability.invalidate();
        this.inputInventoryReadOnlyCapability.invalidate();
        this.outputInventoryExtractOnlyCapability.invalidate();
        this.outputInventoryReadOnlyCapability.invalidate();
        this.fluidTankCapability.invalidate();
        this.fluidTankReadOnlyCapability.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("inputInventory", this.inputInventory.serializeNBT());
        pTag.put("outputInventory", this.outputInventory.serializeNBT());
        pTag.put("fluidTank", this.fluidTank.writeToNBT(new CompoundTag()));

        this.craftingBehaviour.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inputInventory")) this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory")) this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));

        if (pTag.contains("fluidTank")) {
            this.fluidTank.readFromNBT(pTag.getCompound("fluidTank"));
        }

        this.craftingBehaviour.load(pTag);
    }


    @Override
    public DigestionCraftingBehaviour craftingBehaviour() {
        return this.craftingBehaviour;
    }

    public class WaterTank extends FluidTank {
        public WaterTank(int capacity, Predicate<FluidStack> validator) {
            super(capacity, validator);
        }

        @Override
        protected void onContentsChanged() {
            DigestionVatBlockEntity.this.setChanged();
        }
    }

    public class InputInventory extends MonitoredItemStackHandler {

        public InputInventory() {
            super(3);
        }


        @Override
        protected void onSetStackInSlot(int slot, ItemStack oldStack, ItemStack newStack, boolean isSameItem) {
            if (!isSameItem) {
                DigestionVatBlockEntity.this.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            }
        }

        @Override
        protected void onInsertItem(int slot, ItemStack oldStack, ItemStack newStack, ItemStack toInsert, ItemStack remaining) {
            if (remaining != newStack) {
                DigestionVatBlockEntity.this.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            }
        }

        @Override
        protected void onExtractItem(int slot, ItemStack oldStack, ItemStack newStack, ItemStack extracted) {
            if (newStack.isEmpty()) {
                DigestionVatBlockEntity.this.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            //we only allow one item type to fill maximum one slot, so if another slot has the stack, return false.
            for (int i = 0; i < this.getSlots(); i++) {
                if (i != slot && ItemHandlerHelper.canItemStacksStack(stack, this.getStackInSlot(i))) {
                    return false;
                }
            }

            return DigestionVatBlockEntity.this.craftingBehaviour.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            DigestionVatBlockEntity.this.setChanged();
        }
    }

    public class OutputInventory extends ItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        protected void onContentsChanged(int slot) {
            DigestionVatBlockEntity.this.setChanged();
        }
    }
}
