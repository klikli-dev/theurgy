// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.selection.SelectionBehaviour;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageSulfuricFluxEmitterSelection;
import com.klikli_dev.theurgy.registry.BlockTagRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public class SulfuricFluxEmitterSelectionBehaviour extends SelectionBehaviour<SulfuricFluxEmitterSelectedPoint> {

    @Override
    protected void displaySummary(BlockPos pos, Player player) {
        if (this.selectedPoints.isEmpty()) {
            player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_SELECTION).withStyle(ChatFormatting.RED), true);
        } else {
            var sources = this.selectedPoints.stream().filter(s -> s.getType() == SulfuricFluxEmitterSelectedPoint.Type.SOURCE).count();
            var targets = this.selectedPoints.stream().filter(s -> s.getType() == SulfuricFluxEmitterSelectedPoint.Type.TARGET).count();
            var results = this.selectedPoints.stream().filter(s -> s.getType() == SulfuricFluxEmitterSelectedPoint.Type.RESULT).count();

            if (targets == 0) {
                player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_TARGET).withStyle(ChatFormatting.RED), true);
            }
            if (sources == 0) {
                player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_SOURCES).withStyle(ChatFormatting.RED), true);
            }
            if (results == 0) {
                player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_RESULT).withStyle(ChatFormatting.RED), true);
            }

            if (sources > 0 && targets > 0 && results > 0) {
                player.displayClientMessage(Component.translatable(
                        TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER,
                        Component.literal(String.valueOf(sources)).withStyle(ChatFormatting.DARK_PURPLE),
                        Component.literal(String.valueOf(targets)).withStyle(ChatFormatting.BLUE),
                        Component.literal(String.valueOf(results)).withStyle(ChatFormatting.GREEN)
                ).withStyle(ChatFormatting.WHITE), true);
            }
        }
    }

    @Override
    protected void sendPlacementPacket(BlockPos pos) {
        var sources = this.selectedPoints.stream().filter(s -> s.getType() == SulfuricFluxEmitterSelectedPoint.Type.SOURCE).toList();
        var target = this.selectedPoints.stream().filter(s -> s.getType() == SulfuricFluxEmitterSelectedPoint.Type.TARGET).findFirst().orElse(null);
        var result = this.selectedPoints.stream().filter(s -> s.getType() == SulfuricFluxEmitterSelectedPoint.Type.RESULT).findFirst().orElse(null);

        Networking.sendToServer(new MessageSulfuricFluxEmitterSelection(pos, sources, target, result));
    }

    @Override
    public int getBlockRange() {
        return 16;
    }

    @Override
    public boolean canCreate(Level level, BlockPos pos, BlockState state) {
        if (!level.isLoaded(pos))
            return false;

        return state.is(BlockTagRegistry.REFORMATION_PEDESTALS);
    }

    @Override
    protected SulfuricFluxEmitterSelectedPoint create(Level level, BlockPos pos, BlockState state) {
        if (!this.canCreate(level, pos, state))
            return null;

        var type = this.getType(state);

        this.makeSpaceForNewSelection(type);

        return new SulfuricFluxEmitterSelectedPoint(level, pos, state, type);
    }

    private void makeSpaceForNewSelection(SulfuricFluxEmitterSelectedPoint.Type type) {
        if (type == SulfuricFluxEmitterSelectedPoint.Type.SOURCE) {
            //we can have unlimited source pedestals
        } else if (type == SulfuricFluxEmitterSelectedPoint.Type.TARGET) {
            //we can have only one target pedestal
            this.selectedPoints.removeIf(point -> point.getType() == SulfuricFluxEmitterSelectedPoint.Type.TARGET);
        } else {
            //we can have only one result pedestal
            this.selectedPoints.removeIf(point -> point.getType() == SulfuricFluxEmitterSelectedPoint.Type.RESULT);
        }
    }

    private SulfuricFluxEmitterSelectedPoint.Type getType(BlockState state) {
        if (state.is(BlockTagRegistry.REFORMATION_SOURCE_PEDESTALS))
            return SulfuricFluxEmitterSelectedPoint.Type.SOURCE;

        if (state.is(BlockTagRegistry.REFORMATION_TARGET_PEDESTALS))
            return SulfuricFluxEmitterSelectedPoint.Type.TARGET;

        return SulfuricFluxEmitterSelectedPoint.Type.RESULT;
    }

    @Override
    protected boolean isSelectionItem(ItemStack stack) {
        return stack.is(ItemRegistry.SULFURIC_FLUX_EMITTER.get());
    }
}
