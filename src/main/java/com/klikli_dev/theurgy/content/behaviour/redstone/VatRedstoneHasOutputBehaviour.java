package com.klikli_dev.theurgy.content.behaviour.redstone;

import com.klikli_dev.theurgy.content.behaviour.storage.HasStorageBehaviour;
import com.klikli_dev.theurgy.content.behaviour.storage.StorageBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

/**
 * A behaviour that opens or closes the lid if the redstone signal changes.
 */
public class VatRedstoneHasOutputBehaviour<T extends StorageBehaviour<?>> {

    public VatRedstoneHasOutputBehaviour() {

    }

    public int getSignal(@NotNull BlockState pState, BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull Direction pDirection) {
        if(!pState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) || pDirection != pState.getValue(BlockStateProperties.HORIZONTAL_FACING))
            return 0;

        var blockEntity = pLevel.getBlockEntity(pPos);
        if (!(blockEntity instanceof HasStorageBehaviour<?>))
            return 0;

        @SuppressWarnings("unchecked") var vat = (HasStorageBehaviour<T>) blockEntity;

        return vat.storageBehaviour().hasOutput() ? 15 : 0;
    }
}
