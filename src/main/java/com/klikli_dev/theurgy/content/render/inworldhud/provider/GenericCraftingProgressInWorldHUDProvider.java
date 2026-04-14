// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud.provider;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDBuilder;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDProvider;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;

public class GenericCraftingProgressInWorldHUDProvider implements InWorldHUDProvider {

    private static final int BAR_WIDTH = 10;
    private static final String FILLED_SEGMENT = "█";
    private static final String EMPTY_SEGMENT = "░";

    @Override
    public boolean activatesHUD() {
        return false;
    }

    @Override
    public boolean applies(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        // Exclude MercuryCatalyst - it has its own HUD with flux display
        if (state.is(BlockRegistry.MERCURY_CATALYST.get())) {
            return false;
        }
        HasCraftingBehaviour<?, ?, ?> craftingBlockEntity = this.getCraftingBlockEntity(level, pos, state, blockEntity);
        return craftingBlockEntity != null && craftingBlockEntity.craftingBehaviour().isProcessing();
    }

    @Override
    public void appendServerData(InWorldHUDBuilder builder, ServerPlayer player, ServerLevel level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        HasCraftingBehaviour<?, ?, ?> craftingBlockEntity = this.getCraftingBlockEntity(level, pos, state, blockEntity);
        if (craftingBlockEntity == null || !craftingBlockEntity.craftingBehaviour().isProcessing()) {
            return;
        }

        int progressPercent = craftingBlockEntity.craftingBehaviour().progressPercent();

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

    private @Nullable HasCraftingBehaviour<?, ?, ?> getCraftingBlockEntity(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (blockEntity instanceof HasCraftingBehaviour<?, ?, ?> craftingBlockEntity) {
            return craftingBlockEntity;
        }

        if (state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) && state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
            BlockEntity lowerBlockEntity = level.getBlockEntity(pos.below());
            if (lowerBlockEntity instanceof HasCraftingBehaviour<?, ?, ?> craftingBlockEntity) {
                return craftingBlockEntity;
            }
        }

        return null;
    }
}
