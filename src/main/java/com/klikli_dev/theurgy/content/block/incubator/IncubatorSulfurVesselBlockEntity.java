/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.incubator;

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

public class IncubatorSulfurVesselBlockEntity extends BlockEntity {

    public IncubatorBlockEntity incubator;

    public ItemStackHandler inputInventory;

    public LazyOptional<IItemHandler> inputInventoryCapability;

    public IncubatorSulfurVesselBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.INCUBATOR_SULFUR_VESSEL.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);
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

    public void setIncubator(IncubatorBlockEntity incubator) {
        this.incubator = incubator;
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
                if (IncubatorSulfurVesselBlockEntity.this.incubator != null)
                    IncubatorSulfurVesselBlockEntity.this.incubator.onVesselItemChanged();
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.is(ItemTagRegistry.ALCHEMICAL_SULFURS) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            IncubatorSulfurVesselBlockEntity.this.setChanged();
        }
    }
}
