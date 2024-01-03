package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DigestionVatBlockEntity extends BlockEntity {

    public DigestionVatBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.DIGESTION_VAT.get(), pPos, pBlockState);
    }

    public Direction getDirection() {
        return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
    }
}
