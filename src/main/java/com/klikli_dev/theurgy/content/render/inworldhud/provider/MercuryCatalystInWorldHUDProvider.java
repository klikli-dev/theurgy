// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud.provider;

import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDBuilder;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDProvider;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MercuryCatalystInWorldHUDProvider implements InWorldHUDProvider {

    public static Block appliesBlock() {
        return BlockRegistry.MERCURY_CATALYST.get();
    }

    @Override
    public boolean activatesHUD() {
        return true;
    }

    @Override
    public boolean applies(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        return state.is(BlockRegistry.MERCURY_CATALYST.get());
    }

    @Override
    public void appendClientData(InWorldHUDBuilder builder, Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        builder.setTitleIfAbsent(state.getBlock().getName());
    }
}
