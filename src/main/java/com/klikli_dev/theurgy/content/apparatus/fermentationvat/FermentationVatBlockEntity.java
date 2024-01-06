package com.klikli_dev.theurgy.content.apparatus.fermentationvat;

import com.klikli_dev.theurgy.content.behaviour.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.content.behaviour.PreventInsertWrapper;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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

public class FermentationVatBlockEntity extends BlockEntity implements HasCraftingBehaviour<RecipeWrapperWithFluid, FermentationRecipe, FermentationCachedCheck> {

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

    public FluidTank fluidTank;
    public LazyOptional<IFluidHandler> fluidTankCapability;

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

        this.craftingBehaviour = new FermentationCraftingBehaviour(this, () -> this.inputInventory, () -> this.outputInventory, () -> this.fluidTank);

        this.fluidTank = new WaterTank(FluidType.BUCKET_VOLUME * 10, this.craftingBehaviour::canProcess);
        this.fluidTankCapability = LazyOptional.of(() -> this.fluidTank);
    }

    public void tickServer() {
        //TODO: isProcessing syncs to client - should we act on that?
        //      a bubbling sound would be good
        //      we may be able to just use the closed state clientside to not have to tick the TE -> however, not really a relevant optimization
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

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP) return this.inputInventoryCapability.cast();
            if (side == Direction.DOWN) return this.outputInventoryCapability.cast();
            return this.inventoryCapability.cast();
        }

        if (cap == ForgeCapabilities.FLUID_HANDLER) return this.fluidTankCapability.cast();

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.inventoryCapability.invalidate();
        this.inputInventoryCapability.invalidate();
        this.outputInventoryCapability.invalidate();
        this.fluidTankCapability.invalidate();
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
    public FermentationCraftingBehaviour craftingBehaviour() {
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
