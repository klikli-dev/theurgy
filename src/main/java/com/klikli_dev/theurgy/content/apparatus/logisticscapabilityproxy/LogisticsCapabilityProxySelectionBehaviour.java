// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticscapabilityproxy;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.apparatus.logisticscapabilityprobe.LogisticsCapabilityProbeBlock;
import com.klikli_dev.theurgy.content.behaviour.selection.SelectionBehaviour;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageLogisticsCapabilityProxySelection;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LogisticsCapabilityProxySelectionBehaviour extends SelectionBehaviour<LogisticsCapabilityProxySelectedPoint> {

    @Override
    protected void displaySummary(BlockPos pos, Player player) {
        if (this.selectedPoints.isEmpty()) {
            player.sendOverlayMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_LOGISTICS_CAPABILITY_PROXY_NO_SELECTION).withStyle(ChatFormatting.RED));
            return;
        }

        player.sendOverlayMessage(Component.translatable(
                TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_LOGISTICS_CAPABILITY_PROXY,
                this.selectedPoints.size()
        ).withStyle(ChatFormatting.WHITE));
    }

    @Override
    protected void sendPlacementPacket(BlockPos pos) {
        Networking.sendToServer(new MessageLogisticsCapabilityProxySelection(pos, this.selectedPoints));
    }

    @Override
    public int getBlockRange() {
        return 32;
    }

    @Override
    public boolean canCreate(Level level, BlockPos pos, BlockState state) {
        return level.isLoaded(pos) && state.getBlock() instanceof LogisticsCapabilityProbeBlock;
    }

    @Override
    protected LogisticsCapabilityProxySelectedPoint create(Level level, BlockPos pos, BlockState state) {
        if (!this.canCreate(level, pos, state)) {
            return null;
        }

        return new LogisticsCapabilityProxySelectedPoint(level, pos, state);
    }

    @Override
    protected boolean isSelectionItem(ItemStack stack) {
        return stack.is(ItemRegistry.LOGISTICS_CAPABILITY_PROXY.get());
    }
}
