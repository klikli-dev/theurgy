// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.extractor;

import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlock;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LogisticsItemExtractorBlock extends LogisticsItemConnectorBlock {

    public static final MapCodec<LogisticsItemExtractorBlock> CODEC = simpleCodec(LogisticsItemExtractorBlock::new);

    public LogisticsItemExtractorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return BlockEntityRegistry.LOGISTICS_ITEM_EXTRACTOR.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof LogisticsItemExtractorBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }
}
