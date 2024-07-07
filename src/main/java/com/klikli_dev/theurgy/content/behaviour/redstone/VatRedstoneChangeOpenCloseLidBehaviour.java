package com.klikli_dev.theurgy.content.behaviour.redstone;

import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.DigestionRecipe;
import com.klikli_dev.theurgy.util.RedstoneUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

/**
 * A behaviour that opens or closes the lid if the redstone signal changes.
 */
public class VatRedstoneChangeOpenCloseLidBehaviour<R extends Recipe<?>> {

    public VatRedstoneChangeOpenCloseLidBehaviour() {

    }

    public void neighborChanged(BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Block pBlock, @NotNull BlockPos pFromPos, boolean pIsMoving) {
        //Closed = is processing
        //has signal -> should be processing -> close

        //in case we ever want to improve this, this is how we get the signal only from the changed neighbor:
//        pLevel.hasSignal(pFromPos, Direction.getNearest(Vec3.atCenterOf(pPos).subtract(Vec3.atCenterOf(pFromPos))));

        boolean hasSignal = RedstoneUtil.hasNeighborSignal(pLevel, pPos, pState.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
        boolean wasOpen = pState.getValue(BlockStateProperties.OPEN);
        if (hasSignal && wasOpen) {

            var blockEntity = pLevel.getBlockEntity(pPos);
            if (!(blockEntity instanceof HasCraftingBehaviour<?, ?, ?>))
                return;

            @SuppressWarnings("unchecked") var vat = (HasCraftingBehaviour<?, R, ?>) blockEntity;

            var craftingBehaviour = vat.craftingBehaviour();

            var recipe = craftingBehaviour.getRecipe();
            if (recipe.isPresent() && craftingBehaviour.canCraft(recipe.get())) {
                pLevel.setBlock(pPos, pState.setValue(BlockStateProperties.OPEN, false), Block.UPDATE_CLIENTS);
            }
        } else if (!hasSignal && !wasOpen) {
            pLevel.setBlock(pPos, pState.setValue(BlockStateProperties.OPEN, true), Block.UPDATE_CLIENTS);
        }
    }
}
