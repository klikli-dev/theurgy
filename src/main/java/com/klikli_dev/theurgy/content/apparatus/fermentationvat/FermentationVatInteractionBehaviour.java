// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.fermentationvat;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatBlockEntity;
import com.klikli_dev.theurgy.content.behaviour.interaction.GenericVatInteractionBehaviour;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;


public class FermentationVatInteractionBehaviour extends GenericVatInteractionBehaviour<FermentationRecipe> {

    @Override
    public ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        var blockEntity = pLevel.getBlockEntity(pPos);

        if (!(blockEntity instanceof FermentationVatBlockEntity vat))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    protected void showNoRecipeMessage(Level level, Player player) {
        player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.INTERACTION_FERMENTATION_VAT_NO_RECIPE).withStyle(ChatFormatting.RED), true);
    }

    @Override
    protected void showClosedMessage(Level level, Player player) {
        player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.INTERACTION_FERMENTATION_VAT_CLOSED).withStyle(ChatFormatting.RED), true);
    }
}
