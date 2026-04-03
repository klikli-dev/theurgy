// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud.provider;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDBuilder;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDProvider;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MercuryFluxStorageInWorldHUDProvider implements InWorldHUDProvider {

    @Override
    public boolean applies(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        return level.getCapability(CapabilityRegistry.MERCURY_FLUX_HANDLER, pos, state, blockEntity, null) != null;
    }

    @Override
    public void appendServerData(InWorldHUDBuilder builder, ServerPlayer player, ServerLevel level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        this.addFluxLine(builder, level, pos, state, blockEntity);
    }

    private void addFluxLine(InWorldHUDBuilder builder, Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        var storage = level.getCapability(CapabilityRegistry.MERCURY_FLUX_HANDLER, pos, state, blockEntity, null);
        if (storage == null) {
            return;
        }

        builder.addLine(Component.translatable(
                TheurgyConstants.I18n.JEI.MERCURY_FLUX,
                storage.getEnergyStored() + " / " + storage.getMaxEnergyStored()
        ).withStyle(ChatFormatting.GRAY));
    }
}
