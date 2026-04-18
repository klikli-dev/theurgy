// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.behaviour.itemhandler.ItemHandlerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.itemhandler.OneSlotItemHandlerBehaviour;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ReformationResultPedestalBlock extends Block implements EntityBlock {

    private final ItemHandlerBehaviour itemHandlerBehaviour;

    public ReformationResultPedestalBlock(Properties pProperties) {
        super(pProperties);

        this.itemHandlerBehaviour = new OneSlotItemHandlerBehaviour();
    }

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        //We do not check for client side because
        // a) returning success causes https://github.com/klikli-dev/theurgy/issues/158
        // b) client side BEs are separate objects even in SP, so modification in our behaviours is safe

        if (pStack.is(ItemRegistry.SULFURIC_FLUX_EMITTER.get())) {
            return InteractionResult.PASS;
        }

        if (this.itemHandlerBehaviour.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState pState, ServerLevel pLevel, BlockPos pPos, boolean pMovedByPiston) {
        Containers.updateNeighboursAfterDestroy(pState, pLevel, pPos);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return (lvl, pos, blockState, t) -> {
                if (t instanceof ReformationResultPedestalBlockEntity blockEntity) {
                    blockEntity.tickClient();
                }
            };
        }
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.REFORMATION_RESULT_PEDESTAL.get().create(pPos, pState);
    }
}
