// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniactank;

import com.klikli_dev.theurgy.content.behaviour.fluidhandler.FluidHandlerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.fluidhandler.OneTankFluidHandlerBehaviour;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SalAmmoniacTankBlock extends Block implements EntityBlock {

    protected FluidHandlerBehaviour fluidHandlerBehaviour;

    public SalAmmoniacTankBlock(Properties pProperties) {
        super(pProperties);
        this.fluidHandlerBehaviour = new OneTankFluidHandlerBehaviour();
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState pState) {
        //Why model for the top? because then we get the particle texture from destroying it.
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        //We do not check for client side because
        // a) returning success causes https://github.com/klikli-dev/theurgy/issues/158
        // b) client side BEs are separate objects even in SP, so modification in our behaviours is safe

        if (this.fluidHandlerBehaviour.useFluidHandler(pState, pLevel, pPos, pPlayer, pHand, pHit) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof SalAmmoniacTankBlockEntity blockEntity) {
                //no inventory
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.SAL_AMMONIAC_TANK.get().create(pPos, pState);
    }
}
