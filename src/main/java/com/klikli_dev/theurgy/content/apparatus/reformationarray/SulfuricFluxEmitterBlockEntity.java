package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SulfuricFluxEmitterBlockEntity extends BlockEntity {
    public SulfuricFluxEmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.SULFURIC_FLUX_EMITTER.get(), pPos, pBlockState);
    }

    public void tickServer() {

    }
}
