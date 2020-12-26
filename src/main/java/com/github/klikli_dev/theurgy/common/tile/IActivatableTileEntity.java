package com.github.klikli_dev.theurgy.common.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public interface IActivatableTileEntity {
    //region Methods
    ActionResultType onTileEntityActivated(BlockState state, BlockPos pos, PlayerEntity player, Hand hand);
    //endregion Methods
}
