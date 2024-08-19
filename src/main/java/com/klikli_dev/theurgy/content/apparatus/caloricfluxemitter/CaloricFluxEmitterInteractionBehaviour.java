// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.interaction.InteractionBehaviour;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageShowCaloricFluxEmitterStatus;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class CaloricFluxEmitterInteractionBehaviour implements InteractionBehaviour {

    @Override
    public ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pHand != InteractionHand.MAIN_HAND)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        var blockEntity = pLevel.getBlockEntity(pPos);

        if (!(blockEntity instanceof CaloricFluxEmitterBlockEntity caloricFluxEmitter))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (pLevel.isClientSide)
            return ItemInteractionResult.SUCCESS;

        Networking.sendTo((ServerPlayer) pPlayer, new MessageShowCaloricFluxEmitterStatus(
                pPos,
                caloricFluxEmitter.selectedPoints
        ));

        return ItemInteractionResult.SUCCESS;
    }

    public void showStatus(Level level, BlockPos pos, Player player) {
        var blockEntity = level.getBlockEntity(pos);

        if (!(blockEntity instanceof CaloricFluxEmitterBlockEntity caloricFluxEmitter))
            return;

        this.showOutlines(level, caloricFluxEmitter);
        this.showStatusMessage(level, player, caloricFluxEmitter);
    }

    private void showOutlines(Level level, CaloricFluxEmitterBlockEntity caloricFluxEmitter) {
        for (var selectedPoint : caloricFluxEmitter.selectedPoints) {
            BlockPos pos = selectedPoint.getBlockPos();
            VoxelShape shape = Shapes.block();

            var isValid = level.getCapability(CapabilityRegistry.HEAT_RECEIVER, pos, null) != null;

            Outliner.get().showAABB(selectedPoint, shape.bounds()
                            .move(pos), 20 * 5)
                    .colored(isValid ? selectedPoint.getColor().getRGB() : 0xFF0000)
                    .lineWidth(1 / 16f);
        }
    }

    private void showStatusMessage(Level level, Player player, CaloricFluxEmitterBlockEntity caloricFluxEmitter) {
        if (caloricFluxEmitter.selectedPoints.isEmpty()) {
            player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_CALORIC_FLUX_EMITTER_NO_SELECTION).withStyle(ChatFormatting.RED), true);
        } else {
            var target = caloricFluxEmitter.selectedPoints.getFirst();
            var state = target.getBlockState();
            player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_CALORIC_FLUX_EMITTER, state.getBlock().getName()).withStyle(ChatFormatting.WHITE), true);
        }
    }
}
