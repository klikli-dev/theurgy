package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.behaviour.PreventInsertWrapper;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class ReformationResultPedestalBlockEntity extends BlockEntity {

    /**
     * The underlying outputInventory which allows inserting too - we use this when crafting.
     */
    public ItemStackHandler outputInventory;
    /**
     * A wrapper that only allows taking from the outputInventory - this is what we show to the outside.
     */
    public PreventInsertWrapper outputInventoryTakeOnlyWrapper;
    public LazyOptional<IItemHandler> outputInventoryCapability;

    public WeakReference<SulfuricFluxEmitterBlockEntity> sulfuricFluxEmitter;

    public ReformationResultPedestalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.REFORMATION_RESULT_PEDESTAL.get(), pPos, pBlockState);

        this.outputInventory = new OutputInventory();
        this.outputInventoryTakeOnlyWrapper = new PreventInsertWrapper(this.outputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventoryTakeOnlyWrapper);
    }

    public void setSulfuricFluxEmitter(SulfuricFluxEmitterBlockEntity sulfuricFluxEmitter) {
        this.sulfuricFluxEmitter = new WeakReference<>(sulfuricFluxEmitter);
    }

    @Override
    public void setRemoved() {
        if (this.sulfuricFluxEmitter != null && this.sulfuricFluxEmitter.get() != null) {
            this.sulfuricFluxEmitter.get().removeResultPedestal(this);
            this.sulfuricFluxEmitter.clear();
        }

        super.setRemoved();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.outputInventoryCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.outputInventoryCapability.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("outputInventory", this.outputInventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("outputInventory"))
            this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));
    }

    public class OutputInventory extends ItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        protected void onContentsChanged(int slot) {
            ReformationResultPedestalBlockEntity.this.setChanged();
        }
    }
}
