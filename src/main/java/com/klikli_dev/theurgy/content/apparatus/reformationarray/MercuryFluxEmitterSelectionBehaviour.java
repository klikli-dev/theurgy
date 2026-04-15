// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.selection.SelectionBehaviour;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageMercuryFluxEmitterSelection;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class MercuryFluxEmitterSelectionBehaviour extends SelectionBehaviour<MercuryFluxEmitterSelectedPoint> {

    @Override
    public boolean onRightClickBlock(Level level, Player player, InteractionHand hand, BlockPos pos, Direction direction) {
        // If player is holding shift, allow normal block placement instead of selection
        if (player.isShiftKeyDown()) {
            return false;
        }
        return super.onRightClickBlock(level, player, hand, pos, direction);
    }

    @Override
    protected void displaySummary(BlockPos pos, Player player) {
        if (this.selectedPoints.isEmpty()) {
            player.sendOverlayMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_MERCURY_FLUX_EMITTER_NO_SELECTION).withStyle(ChatFormatting.RED));
        } else {
            var target = this.selectedPoints.getFirst();
            var state = target.getBlockState();
            player.sendOverlayMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_MERCURY_FLUX_EMITTER, state.getBlock().getName()).withStyle(ChatFormatting.WHITE));
        }
    }

    @Override
    protected void sendPlacementPacket(BlockPos pos) {
        Networking.sendToServer(new MessageMercuryFluxEmitterSelection(pos, this.selectedPoints));
    }

    @Override
    public int getBlockRange() {
        return 8;
    }

    @Override
    public boolean canCreate(Level level, BlockPos pos, BlockState state) {
        if (!level.isLoaded(pos))
            return false;

        return level.getCapability(CapabilityRegistry.MERCURY_FLUX_HANDLER, pos, state, null, null) != null;
    }

    @Override
    protected MercuryFluxEmitterSelectedPoint create(Level level, BlockPos pos, BlockState state) {
        if (!this.canCreate(level, pos, state))
            return null;

        //max one selection point
        if (!this.selectedPoints.isEmpty())
            this.selectedPoints.clear();

        return new MercuryFluxEmitterSelectedPoint(level, pos, state);
    }

    @Override
    protected boolean isSelectionItem(ItemStack stack) {
        return stack.is(ItemRegistry.MERCURY_FLUX_EMITTER.get());
    }
}
