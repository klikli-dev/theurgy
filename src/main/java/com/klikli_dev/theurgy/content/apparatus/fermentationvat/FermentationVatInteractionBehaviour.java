package com.klikli_dev.theurgy.content.apparatus.fermentationvat;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.InteractionBehaviour;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class FermentationVatInteractionBehaviour implements InteractionBehaviour {
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        var blockEntity = pLevel.getBlockEntity(pPos);

        if (!(blockEntity instanceof FermentationVatBlockEntity vat))
            return InteractionResult.PASS;

        //interaction with shift and empty hand opens/closes the vat
        if (!pPlayer.isShiftKeyDown() || !pPlayer.getMainHandItem().isEmpty())
            return InteractionResult.PASS;

        if (pLevel.isClientSide)
            return InteractionResult.SUCCESS;


        var craftingBehaviour = vat.getCraftingBehaviour();

        var isOpen = pState.getValue(BlockStateProperties.OPEN);

        if (isOpen) {
            //we can only close if we have a valid recipe and can craft it
            var recipe = craftingBehaviour.getRecipe();
            if (recipe.isPresent() && craftingBehaviour.canCraft(recipe.get())) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(BlockStateProperties.OPEN, Boolean.FALSE));
            } else {
                this.showNoRecipeMessage(pLevel, pPlayer);
                return InteractionResult.FAIL;
            }
        } else {
            //when opening we stop processing (because we want to interrupt the crafting process)
            //we also set changed so that gets saved.
            pLevel.setBlockAndUpdate(pPos, pState.setValue(BlockStateProperties.OPEN, Boolean.TRUE));
            craftingBehaviour.stopProcessing();
            vat.setChanged();
        }

        return InteractionResult.SUCCESS;
    }

    private void showNoRecipeMessage(Level level, Player player) {
        player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.INTERACTION_FERMENTATION_VAT_NO_RECIPE).withStyle(ChatFormatting.RED), true);
    }

}
