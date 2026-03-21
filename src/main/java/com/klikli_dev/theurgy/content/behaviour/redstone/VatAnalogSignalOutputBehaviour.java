// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.redstone;

import com.klikli_dev.theurgy.content.behaviour.storage.HasStorageBehaviour;
import com.klikli_dev.theurgy.content.behaviour.storage.OutputStorageBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;

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
    public static int getRedstoneSignalFromContainer(@Nullable ResourceHandler<ItemResource> pContainer) {
        return pContainer == null ? 0 : ResourceHandlerUtil.getRedstoneSignalFromResourceHandler(pContainer);
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
