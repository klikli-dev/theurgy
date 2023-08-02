/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier;

import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlock;
import com.klikli_dev.theurgy.content.capability.HeatProvider;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PyromanticBrazierBlockEntity extends BlockEntity {
    public ItemStackHandler inventory;

    public LazyOptional<IItemHandler> inventoryCapability;
    public LazyOptional<HeatProvider> heatProviderCapability;

    private int remainingLitTime;

    public PyromanticBrazierBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.PYROMANTIC_BRAZIER.get(), pPos, pBlockState);

        this.inventory = new Inventory();
        this.inventoryCapability = LazyOptional.of(() -> this.inventory);

        this.heatProviderCapability = LazyOptional.of(() -> () -> this.getBlockState().getValue(PyromanticBrazierBlock.LIT));
    }

    protected int getBurnDuration(ItemStack pFuel) {
        if (pFuel.isEmpty()) {
            return 0;
        } else {
            return ForgeHooks.getBurnTime(pFuel, RecipeTypeRegistry.PYROMANTIC_BRAZIER.get());
        }
    }

    public boolean isLit() {
        return this.remainingLitTime > 0;
    }

    public void tickClient() {
//        RandomSource randomsource = this.level.random;
//        if (randomsource.nextFloat() < 0.11F) {
//            for (int i = 0; i < randomsource.nextInt(2) + 2; ++i) {
//                CampfireBlock.makeParticles(this.level, this.getBlockPos(), false, false);
//            }
//        }
    }

    public void tickServer() {
        boolean wasTurnedOnDuringThisTick = false;
        boolean wasLitAtBeginOfTick = this.isLit();
        if (wasLitAtBeginOfTick) {
            --this.remainingLitTime;
        }

        var fuelStack = this.inventory.getStackInSlot(0);
        boolean hasFuel = !fuelStack.isEmpty();

        //light the block / turn it on if we have fuel
        if (!this.isLit() && hasFuel) {
            this.remainingLitTime = this.getBurnDuration(fuelStack);

            if (this.isLit()) {
                wasTurnedOnDuringThisTick = true;
                //handle lava bucket
                if (fuelStack.hasCraftingRemainingItem())
                    this.inventory.setStackInSlot(0, fuelStack.getCraftingRemainingItem());
                    //handle all other fuel items
                else if (hasFuel) {
                    fuelStack.shrink(1);
                    if (fuelStack.isEmpty()) {
                        this.inventory.setStackInSlot(0, fuelStack.getCraftingRemainingItem());
                    }
                }
            }
        }


        if (wasLitAtBeginOfTick != this.isLit()) {
            wasTurnedOnDuringThisTick = true;
            var newState = this.getBlockState().setValue(CalcinationOvenBlock.LIT, this.isLit());
            this.level.setBlock(this.getBlockPos(), newState, 3);
        }

        if (wasTurnedOnDuringThisTick) {
            this.setChanged();
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.inventoryCapability.cast();
        }

        if (cap == CapabilityRegistry.HEAT_PROVIDER) {
            return this.heatProviderCapability.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.inventoryCapability.invalidate();
        this.heatProviderCapability.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("inventory", this.inventory.serializeNBT());
        pTag.putShort("remainingLitTime", (short) this.remainingLitTime);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inventory"))
            this.inventory.deserializeNBT(pTag.getCompound("inventory"));

        if (pTag.contains("remainingLitTime"))
            this.remainingLitTime = pTag.getShort("remainingLitTime");
    }

    private class Inventory extends ItemStackHandler {
        public Inventory() {
            super(1);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return PyromanticBrazierBlockEntity.this.getBurnDuration(stack) > 0;
        }

        @Override
        protected void onContentsChanged(int slot) {
            PyromanticBrazierBlockEntity.this.setChanged();
        }
    }
}
