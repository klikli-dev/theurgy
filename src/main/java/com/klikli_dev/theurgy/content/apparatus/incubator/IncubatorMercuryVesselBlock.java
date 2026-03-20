// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator;

import com.klikli_dev.theurgy.content.behaviour.itemhandler.ItemHandlerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.itemhandler.OneSlotItemHandlerBehaviour;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

public class IncubatorMercuryVesselBlock extends Block implements EntityBlock {
    protected ItemHandlerBehaviour itemHandlerBehaviour;

    public IncubatorMercuryVesselBlock(Properties pProperties) {
        super(pProperties);
        this.itemHandlerBehaviour = new OneSlotItemHandlerBehaviour();
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState pState, ServerLevel pLevel, BlockPos pPos, boolean pMovedByPiston) {
        Containers.updateNeighboursAfterDestroy(pState, pLevel, pPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState pState) {
        //Why model for the top? because then we get the particle texture from destroying it.
        return RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.INCUBATOR_MERCURY_VESSEL.get().create(pPos, pState);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        //We do not check for client side because
        // a) returning success causes https://github.com/klikli-dev/theurgy/issues/158
        // b) client side BEs are separate objects even in SP, so modification in our behaviours is safe

        if (this.itemHandlerBehaviour.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
