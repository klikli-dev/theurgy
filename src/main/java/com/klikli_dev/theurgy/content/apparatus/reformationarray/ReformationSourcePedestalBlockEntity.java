package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
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

public class ReformationSourcePedestalBlockEntity extends BlockEntity {

    public ItemStackHandler inputInventory;
    public LazyOptional<IItemHandler> inputInventoryCapability;

    public WeakReference<SulfuricFluxEmitterBlockEntity> sulfuricFluxEmitter;

    public ReformationSourcePedestalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.REFORMATION_SOURCE_PEDESTAL.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);
    }

    public void setSulfuricFluxEmitter(SulfuricFluxEmitterBlockEntity sulfuricFluxEmitter) {
        this.sulfuricFluxEmitter = new WeakReference<>(sulfuricFluxEmitter);
    }

    @Override
    public void setRemoved() {
        if (this.sulfuricFluxEmitter != null && this.sulfuricFluxEmitter.get() != null) {
            this.sulfuricFluxEmitter.get().removeSourcePedestal(this);
            this.sulfuricFluxEmitter.clear();
        }

        super.setRemoved();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inputInventory", this.inputInventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inputInventory"))
            this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.inputInventoryCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.inputInventoryCapability.invalidate();
    }

    public class InputInventory extends ItemStackHandler {

        public InputInventory() {
            super(1);
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack newStack) {
            var oldStack = this.getStackInSlot(slot);

            boolean sameItem = !newStack.isEmpty() && ItemStack.isSameItemSameTags(newStack, oldStack);

            super.setStackInSlot(slot, newStack);

            if (!sameItem) {
                var emitter = ReformationSourcePedestalBlockEntity.this.sulfuricFluxEmitter;
                if (emitter != null && emitter.get() != null)
                    emitter.get().craftingBehaviour.onInputItemChanged(oldStack, newStack);
            }
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack newStack, boolean simulate) {
            if (!simulate) {
                var oldStack = this.getStackInSlot(slot);
                var result = super.insertItem(slot, newStack, simulate);

                if (result != newStack) {
                    var emitter = ReformationSourcePedestalBlockEntity.this.sulfuricFluxEmitter;
                    if (emitter != null && emitter.get() != null)
                        emitter.get().craftingBehaviour.onInputItemChanged(oldStack, newStack);
                }

                return result;
            }
            return super.insertItem(slot, newStack, simulate);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.is(ItemTagRegistry.ALCHEMICAL_SULFURS) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            ReformationSourcePedestalBlockEntity.this.setChanged();
            var emitter = ReformationSourcePedestalBlockEntity.this.sulfuricFluxEmitter;

            if (emitter != null && emitter.get() != null)
                emitter.get().onSourcePedestalContentChange(ReformationSourcePedestalBlockEntity.this);
        }
    }
}
