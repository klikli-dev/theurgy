// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.redstone;

import com.klikli_dev.theurgy.content.behaviour.storage.HasStorageBehaviour;
import com.klikli_dev.theurgy.content.behaviour.storage.OutputStorageBehaviour;
import com.klikli_dev.theurgy.content.behaviour.storage.StorageBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * A behaviour that returns an analog signal based on the amount of output items.
 */
public class VatAnalogSignalOutputBehaviour {

    public VatAnalogSignalOutputBehaviour() {

    }

    /**
     * Taken from AbstractContainerMenu.getRedstoneSignalFromContainer and adapted for itemhandler
     */
    public static int getRedstoneSignalFromContainer(@Nullable IItemHandler pContainer) {
        if (pContainer == null) {
            return 0;
        } else {
            float f = 0.0F;

            for (int i = 0; i < pContainer.getSlots(); i++) {
                ItemStack itemstack = pContainer.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    f += (float) itemstack.getCount() / (float) Math.min(pContainer.getSlotLimit(i), itemstack.getMaxStackSize());
                }
            }

            f /= (float) pContainer.getSlots();
            return Mth.lerpDiscrete(f, 0, 15);
        }
    }

    public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
        var blockEntity = pLevel.getBlockEntity(pPos);
        if (!(blockEntity instanceof HasStorageBehaviour<?> hasStorageBehaviour))
            return 0;

        if (!(hasStorageBehaviour.storageBehaviour() instanceof OutputStorageBehaviour outputStorageBehaviour))
            return 0;

        return getRedstoneSignalFromContainer(outputStorageBehaviour.outputInventory());
    }
}
