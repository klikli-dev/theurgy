// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsnode;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.selection.SelectionBehaviour;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageCaloricFluxEmitterSelection;
import com.klikli_dev.theurgy.network.messages.MessageLogisticsNodeSelection;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public class LogisticsNodeSelectionBehaviour extends SelectionBehaviour<LogisticsNodeSelectedPoint> {

    @Override
    protected void displaySummary(BlockPos pos, Player player) {
        if (this.selectedPoints.isEmpty()) {
            //TODO fix messaeg
            player.displayClientMessage(Component.translatable("no selection").withStyle(ChatFormatting.RED), true);
        } else {
            player.displayClientMessage(Component.translatable("selected").withStyle(ChatFormatting.GREEN), true);
        }
    }

    @Override
    protected void sendPlacementPacket(BlockPos pos) {
        Networking.sendToServer(new MessageLogisticsNodeSelection(pos, this.selectedPoints));
    }

    @Override
    public int getBlockRange() {
        return 32;
    }

    @Override
    public boolean canCreate(Level level, BlockPos pos, BlockState state) {
        if (!level.isLoaded(pos))
            return false;

        return state.is(BlockRegistry.LOGISTICS_NODE.get());
    }

    @Override
    protected LogisticsNodeSelectedPoint create(Level level, BlockPos pos, BlockState state) {
        if (!this.canCreate(level, pos, state))
            return null;

        //max one selection point
        if (!this.selectedPoints.isEmpty())
            this.selectedPoints.clear();

        return new LogisticsNodeSelectedPoint(level, pos, state);
    }

    @Override
    protected boolean isSelectionItem(ItemStack stack) {
        return stack.is(ItemRegistry.LOGISTICS_NODE.get());
    }
}
