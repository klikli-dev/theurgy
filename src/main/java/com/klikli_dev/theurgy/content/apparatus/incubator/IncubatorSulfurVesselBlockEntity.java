// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator;

import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
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
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class IncubatorSulfurVesselBlockEntity extends BlockEntity implements GeoBlockEntity {

    protected final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

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

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.inputInventoryCapability.invalidate();
    }

    public void setIncubator(IncubatorBlockEntity incubator) {
        this.incubator = incubator;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    public class InputInventory extends MonitoredItemStackHandler {

        public InputInventory() {
            super(1);
        }

        @Override
        protected void onSetStackInSlot(int slot, ItemStack oldStack, ItemStack newStack, boolean isSameItem) {
            if (!isSameItem) {
                if (IncubatorSulfurVesselBlockEntity.this.incubator != null)
                    IncubatorSulfurVesselBlockEntity.this.incubator.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            }
        }

        @Override
        protected void onInsertItem(int slot, ItemStack oldStack, ItemStack newStack, ItemStack toInsert, ItemStack remaining) {
            if (remaining != newStack) {
                if (IncubatorSulfurVesselBlockEntity.this.incubator != null)
                    IncubatorSulfurVesselBlockEntity.this.incubator.craftingBehaviour.onInputItemChanged(oldStack, newStack);

            }
        }

        @Override
        protected void onExtractItem(int slot, ItemStack oldStack, ItemStack newStack, ItemStack extracted) {
            if (newStack.isEmpty()) {
                if (IncubatorSulfurVesselBlockEntity.this.incubator != null)
                    IncubatorSulfurVesselBlockEntity.this.incubator.craftingBehaviour.onInputItemChanged(oldStack, newStack);
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
