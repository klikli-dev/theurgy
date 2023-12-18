package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.behaviour.InteractionBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class SulfuricFluxEmitterInteractionBehaviour implements InteractionBehaviour {
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        var blockEntity = pLevel.getBlockEntity(pPos);

        if (!(blockEntity instanceof SulfuricFluxEmitterBlockEntity sulfuricFluxEmitter))
            return InteractionResult.PASS;




        var blockItemHandlerCap = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (!blockItemHandlerCap.isPresent())
            return InteractionResult.PASS;

        return null;
    }
}
