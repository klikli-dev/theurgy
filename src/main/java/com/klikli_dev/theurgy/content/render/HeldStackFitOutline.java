// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class HeldStackFitOutline {
    private static final Object SLOT = new Object();

    private HeldStackFitOutline() {
    }

    public static void onClientTick(Player player) {
        ItemStack heldStack = player.getMainHandItem();
        if (heldStack.isEmpty()) {
            Outliner.get().remove(SLOT);
            return;
        }

        var hitResult = Minecraft.getInstance().hitResult;
        if (!(hitResult instanceof BlockHitResult blockHitResult) || hitResult.getType() != HitResult.Type.BLOCK) {
            Outliner.get().remove(SLOT);
            return;
        }

        Level level = player.level();
        BlockPos displayPos = blockHitResult.getBlockPos();
        BlockState displayState = level.getBlockState(displayPos);
        FitResult fit = getFit(level, displayPos, displayState, heldStack);
        if (fit == FitResult.NOT_APPLICABLE) {
            Outliner.get().remove(SLOT);
            return;
        }

        VoxelShape shape = displayState.getShape(level, displayPos);
        if (shape.isEmpty()) {
            Outliner.get().remove(SLOT);
            return;
        }

        Outliner.get().showAABB(SLOT, shape.bounds().move(displayPos), 1)
                .withFaceTexture(Theurgy.loc("block/overlay/white"))
                .colored(fit == FitResult.FITS ? Color.GREEN : Color.RED)
                .lineWidth(1 / 32f);
    }

    private static FitResult getFit(Level level, BlockPos pos, BlockState state, ItemStack heldStack) {
        BlockPos queryPos = resolveQueryPos(pos, state);
        BlockEntity blockEntity = level.getBlockEntity(queryPos);
        if (!(blockEntity instanceof HeldStackFitProvider provider)) {
            return FitResult.NOT_APPLICABLE;
        }

        return FitResult.of(provider.heldStackFits(heldStack));
    }

    private static BlockPos resolveQueryPos(BlockPos pos, BlockState state) {
        if (state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) && state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
            return pos.below();
        }

        return pos;
    }

    private enum FitResult {
        FITS,
        DOES_NOT_FIT,
        NOT_APPLICABLE;

        private static FitResult of(boolean fits) {
            return fits ? FITS : DOES_NOT_FIT;
        }
    }
}
