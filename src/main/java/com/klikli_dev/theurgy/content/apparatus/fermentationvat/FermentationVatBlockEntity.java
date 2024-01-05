package com.klikli_dev.theurgy.content.apparatus.fermentationvat;

import com.klikli_dev.theurgy.content.behaviour.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.content.behaviour.PreventInsertWrapper;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.function.Predicate;

public class FermentationVatBlockEntity extends BlockEntity {

    public ItemStackHandler inputInventory;
    public LazyOptional<IItemHandler> inputInventoryCapability;

    /**
     * The underlying outputInventory which allows inserting too - we use this when crafting.
     */
    public ItemStackHandler outputInventory;
    /**
     * A wrapper that only allows taking from the outputInventory - this is what we show to the outside.
     */
    public PreventInsertWrapper outputInventoryTakeOnlyWrapper;
    public LazyOptional<IItemHandler> outputInventoryCapability;

    public CombinedInvWrapper inventory;
    public LazyOptional<IItemHandler> inventoryCapability;

    public FluidTank waterTank;
    public LazyOptional<IFluidHandler> waterTankCapability;

    public FermentationCraftingBehaviour craftingBehaviour;

    public FermentationVatBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.FERMENTATION_VAT.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);

        this.outputInventory = new OutputInventory();
        this.outputInventoryTakeOnlyWrapper = new PreventInsertWrapper(this.outputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventoryTakeOnlyWrapper);

        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventoryTakeOnlyWrapper);
        this.inventoryCapability = LazyOptional.of(() -> this.inventory);

        this.craftingBehaviour = new FermentationCraftingBehaviour(this, () -> this.inventory, () -> this.inventory, () -> this.waterTank);

        this.waterTank = new WaterTank(FluidType.BUCKET_VOLUME * 10, this.craftingBehaviour::canProcess);
        this.waterTankCapability = LazyOptional.of(() -> this.waterTank);
    }

    public void tickServer() {
        //TODO: only allow closing if valid recipe ingredients are present
        boolean isOpen = this.getBlockState().getValue(FermentationVatBlock.OPEN);
        boolean hasInput = this.hasInput();

        this.craftingBehaviour.tickServer(!isOpen, hasInput);

        if(!this.craftingBehaviour.isProcessing() && !isOpen){
            this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(FermentationVatBlock.OPEN, true));
        }
    }

    public boolean hasInput() {
        for (int i = 0; i < this.inputInventory.getSlots(); i++) {
            if (!this.inputInventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public FermentationCraftingBehaviour getCraftingBehaviour() {
        return this.craftingBehaviour;
    }

    public class WaterTank extends FluidTank {
        public WaterTank(int capacity, Predicate<FluidStack> validator) {
            super(capacity, validator);
        }

        @Override
        protected void onContentsChanged() {
            FermentationVatBlockEntity.this.setChanged();
        }
    }

    public class InputInventory extends MonitoredItemStackHandler {

        public InputInventory() {
            super(3);
        }


        @Override
        protected void onSetStackInSlot(int slot, ItemStack oldStack, ItemStack newStack, boolean isSameItem) {
            if (!isSameItem) {
                FermentationVatBlockEntity.this.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            }
        }

        @Override
        protected void onInsertItem(int slot, ItemStack oldStack, ItemStack newStack, ItemStack toInsert, ItemStack remaining) {
            if (remaining != newStack) {
                FermentationVatBlockEntity.this.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            }
        }

        @Override
        protected void onExtractItem(int slot, ItemStack oldStack, ItemStack newStack, ItemStack extracted) {
            if (newStack.isEmpty()) {
                FermentationVatBlockEntity.this.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            //we only allow one item type to fill maximum one slot, so if another slot has the stack, return false.
            for(int i = 0; i < this.getSlots(); i++){
                if(i != slot && ItemHandlerHelper.canItemStacksStack(stack,  this.getStackInSlot(i))){
                    return false;
                }
            }

            return FermentationVatBlockEntity.this.craftingBehaviour.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            FermentationVatBlockEntity.this.setChanged();
        }
    }

    public class OutputInventory extends ItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        protected void onContentsChanged(int slot) {
            FermentationVatBlockEntity.this.setChanged();
        }
    }

}
