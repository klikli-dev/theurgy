// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector.inserter;

import com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector.LogisticsFluidConnectorBlock;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LogisticsFluidInserterBlock extends LogisticsFluidConnectorBlock {

    public static final MapCodec<LogisticsFluidInserterBlock> CODEC = simpleCodec(LogisticsFluidInserterBlock::new);

    public LogisticsFluidInserterBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return BlockEntityRegistry.LOGISTICS_FLUID_INSERTER.get().create(pPos, pState);
    }
}
