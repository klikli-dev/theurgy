/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.itemhandlers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class OneOutputSlotBlockItemHandler implements BlockItemHandler {

    public static final int SLOT = 0;


    /**
     * Default interaction for blocks that have a block entity with an output inventory with one slot.
     */
    @Override
    public InteractionResult useItemHandler(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        var blockEntity = pLevel.getBlockEntity(pPos);

        if (blockEntity == null)
            return InteractionResult.PASS;

        var blockItemHandlerCap = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (!blockItemHandlerCap.isPresent())
            return InteractionResult.PASS;

        var blockItemHandler = blockItemHandlerCap.orElse(null);

        ItemStack stackInHand = pPlayer.getItemInHand(pHand);

        if (stackInHand.isEmpty()) {
            //with empty hand, try to take out
            var extracted = blockItemHandler.extractItem(SLOT, blockItemHandler.getSlotLimit(SLOT), false);
            if (!extracted.isEmpty()) {
                pPlayer.getInventory().placeItemBackInInventory(extracted);
                return InteractionResult.SUCCESS;
            }
        } else {
            // no insertion for this block
        }

        return InteractionResult.PASS;
    }
}
