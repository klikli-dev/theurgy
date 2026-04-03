// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface InWorldHUDProvider {

    boolean applies(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity);

    default void appendClientData(InWorldHUDBuilder builder, Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
    }

    default void appendServerData(InWorldHUDBuilder builder, ServerPlayer player, ServerLevel level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
    }
}
