package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.behaviour.InteractionBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SulfuricFluxEmitterInteractionBehaviour implements InteractionBehaviour {
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        var blockEntity = pLevel.getBlockEntity(pPos);

        if (!(blockEntity instanceof SulfuricFluxEmitterBlockEntity sulfuricFluxEmitter))
            return InteractionResult.PASS;

        if(pLevel.isClientSide)
            return InteractionResult.SUCCESS;

        //TODO send packet

        //allows players to tell the system to re-check
        //this is mainly useful if they removed a pedestal and want it to be recognized it again after rebuilding it
        sulfuricFluxEmitter.checkValidMultiblockOnNextQuery = true;

        return InteractionResult.SUCCESS;
    }
}
