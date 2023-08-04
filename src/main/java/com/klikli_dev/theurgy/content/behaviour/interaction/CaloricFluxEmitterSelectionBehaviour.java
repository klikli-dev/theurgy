package com.klikli_dev.theurgy.content.behaviour.interaction;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageCaloricFluxEmitterSelection;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CaloricFluxEmitterSelectionBehaviour extends SelectionBehaviour<CaloricFluxEmitterSelectedPoint> {

    @Override
    protected void displaySummary(BlockPos pos, Player player) {
        if (this.selectedPoints.isEmpty()) {
            player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_CALORIC_FLUX_EMITTER_NO_SELECTION).withStyle(ChatFormatting.RED), true);
        } else {
            var target = this.selectedPoints.get(0);
            player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_CALORIC_FLUX_EMITTER, target.getBlockPos().toString()).withStyle(ChatFormatting.WHITE), true);
        }
    }

    @Override
    protected void sendPlacementPacket(BlockPos pos) {
        Networking.sendToServer(new MessageCaloricFluxEmitterSelection(pos, this.selectedPoints));
    }

    @Override
    protected int getBlockRange() {
        return 5;
    }

    @Override
    public boolean canCreate(Level level, BlockPos pos, BlockState state) {
        return true; //TODO: only heat receivers
    }

    @Override
    protected CaloricFluxEmitterSelectedPoint create(Level level, BlockPos pos, BlockState state) {
        if (!this.canCreate(level, pos, state))
            return null;

        //max one selection point
        if (!this.selectedPoints.isEmpty())
            this.selectedPoints.clear();

        return new CaloricFluxEmitterSelectedPoint(level, pos, state);
    }

    @Override
    protected boolean isSelectionItem(ItemStack stack) {
        return stack.is(ItemRegistry.CALORIC_FLUX_EMITTER.get());
    }
}
