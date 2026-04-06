// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud.provider;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDBuilder;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDProvider;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockTitleInWorldHUDProvider implements InWorldHUDProvider {

    @Override
    public boolean applies(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        return Theurgy.MODID.equals(BuiltInRegistries.BLOCK.getKey(state.getBlock()).getNamespace())
                && (level.getCapability(CapabilityRegistry.ITEM_HANDLER, pos, state, blockEntity, null) != null
                || level.getCapability(CapabilityRegistry.FLUID_HANDLER, pos, state, blockEntity, null) != null);
    }

    @Override
    public void appendClientData(InWorldHUDBuilder builder, Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        builder.setTitleIfAbsent(state.getBlock().getName());
    }
}
