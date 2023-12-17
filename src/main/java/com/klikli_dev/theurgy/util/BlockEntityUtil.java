package com.klikli_dev.theurgy.util;

import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityUtil {
    public static void sendBlockUpdated(BlockEntity blockEntity) {
        blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
    }
}
