// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LogisticsFluidConnectorBlockItem extends BlockItem {
    public LogisticsFluidConnectorBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        if (pContext.getPlayer() != null) {
            var fluidPlacementResult = this.tryPlaceAgainstSourceFluid(pContext.getLevel(), pContext.getPlayer(), pContext.getHand());
            if (fluidPlacementResult != InteractionResult.PASS) {
                return fluidPlacementResult;
            }
        }

        var clickedPos = pContext.getClickedPos();
        if (!pContext.getLevel().getFluidState(clickedPos).isEmpty()) {
            var adjustedContext = BlockPlaceContext.at(new BlockPlaceContext(pContext), clickedPos.relative(pContext.getClickedFace()), pContext.getClickedFace());
            return this.place(adjustedContext);
        }

        return super.useOn(pContext);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        var fluidPlacementResult = this.tryPlaceAgainstSourceFluid(pLevel, pPlayer, pUsedHand);
        return fluidPlacementResult != InteractionResult.PASS ? fluidPlacementResult : super.use(pLevel, pPlayer, pUsedHand);
    }

    protected @NotNull InteractionResult tryPlaceAgainstSourceFluid(Level level, Player player, InteractionHand hand) {
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hitResult.getType() != BlockHitResult.Type.BLOCK || level.getFluidState(hitResult.getBlockPos()).isEmpty()) {
            return InteractionResult.PASS;
        }

        var adjustedHit = hitResult.withPosition(hitResult.getBlockPos().relative(hitResult.getDirection()));
        return this.place(new BlockPlaceContext(new UseOnContext(player, hand, adjustedHit)));
    }

    @Override
    protected boolean updateCustomBlockEntityTag(@NotNull BlockPos pPos, Level pLevel, @Nullable Player pPlayer, @NotNull ItemStack pStack, @NotNull BlockState pState) {
        var result = super.updateCustomBlockEntityTag(pPos, pLevel, pPlayer, pStack, pState);
        if (pLevel.getBlockEntity(pPos) instanceof LogisticsFluidConnectorBlockEntity connector) {
            var direction = pState.getValue(BlockStateProperties.FACING).getOpposite();
            connector.leafNode().targets().clear();
            connector.leafNode().targets().add(pPos.relative(direction));
            connector.setChanged();
        }
        return result;
    }
}
