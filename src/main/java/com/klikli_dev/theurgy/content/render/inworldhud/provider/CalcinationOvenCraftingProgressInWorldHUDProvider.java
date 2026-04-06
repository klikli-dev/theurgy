// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud.provider;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlock;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlockEntity;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDBuilder;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;

public class CalcinationOvenCraftingProgressInWorldHUDProvider implements InWorldHUDProvider {

    private static final int BAR_WIDTH = 10;
    private static final String FILLED_SEGMENT = "█";
    private static final String EMPTY_SEGMENT = "░";

    @Override
    public boolean activatesHUD() {
        return false;
    }

    @Override
    public boolean applies(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        return state.getBlock() instanceof CalcinationOvenBlock;
    }

    @Override
    public void appendServerData(InWorldHUDBuilder builder, ServerPlayer player, ServerLevel level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        CalcinationOvenBlockEntity oven = this.getBlockEntity(level, pos, state, blockEntity);
        if (oven == null || !oven.craftingBehaviour().isProcessing()) {
            return;
        }

        int progressPercent = oven.craftingBehaviour().progressPercent();

        builder.addLine(Component.translatable(
                TheurgyConstants.I18n.Misc.CRAFTING_PROGRESS,
                Component.literal(this.progressBar(progressPercent)).withStyle(ChatFormatting.GREEN),
                progressPercent
        ).withStyle(ChatFormatting.GRAY));
    }

    private String progressBar(int progressPercent) {
        int filledSegments = Math.clamp(progressPercent * BAR_WIDTH / 100, 0, BAR_WIDTH);
        return FILLED_SEGMENT.repeat(filledSegments) + EMPTY_SEGMENT.repeat(BAR_WIDTH - filledSegments);
    }

    private @Nullable CalcinationOvenBlockEntity getBlockEntity(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (blockEntity instanceof CalcinationOvenBlockEntity oven) {
            return oven;
        }

        if (state.hasProperty(CalcinationOvenBlock.HALF) && state.getValue(CalcinationOvenBlock.HALF) == DoubleBlockHalf.UPPER) {
            BlockEntity lowerBlockEntity = level.getBlockEntity(pos.below());
            if (lowerBlockEntity instanceof CalcinationOvenBlockEntity oven) {
                return oven;
            }
        }

        return null;
    }
}
