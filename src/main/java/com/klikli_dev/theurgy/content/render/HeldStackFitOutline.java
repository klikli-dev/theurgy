// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.config.ClientConfig;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageRequestHeldStackFit;
import com.klikli_dev.theurgy.registry.KeyMappingsRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
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
    private static ItemStack requestedHeldStack = ItemStack.EMPTY;
    private static BlockPos requestedDisplayPos;
    private static ResourceKey<Level> requestedDimension;
    private static HeldStackFitStatus currentFitStatus;
    private static int requestId;

    private HeldStackFitOutline() {
    }

    public static void onClientTick(Player player) {
        if (!ClientConfig.get().rendering.enableHeldStackFitOutline.get() || !KeyMappingsRegistry.isHeldStackFitOutlineKeyActive()) {
            clear();
            return;
        }

        ItemStack heldStack = player.getMainHandItem();
        if (heldStack.isEmpty()) {
            clear();
            return;
        }

        var hitResult = Minecraft.getInstance().hitResult;
        if (!(hitResult instanceof BlockHitResult blockHitResult) || hitResult.getType() != HitResult.Type.BLOCK) {
            clear();
            return;
        }

        Level level = player.level();
        BlockPos displayPos = blockHitResult.getBlockPos();
        BlockState displayState = level.getBlockState(displayPos);
        if (!hasProvider(level, displayPos, displayState)) {
            clear();
            return;
        }

        VoxelShape shape = displayState.getShape(level, displayPos);
        if (shape.isEmpty()) {
            clear();
            return;
        }

        ItemStack normalizedHeldStack = heldStack.copyWithCount(1);
        if (requestChanged(level, displayPos, normalizedHeldStack)) {
            requestId++;
            requestedDisplayPos = displayPos.immutable();
            requestedDimension = level.dimension();
            requestedHeldStack = normalizedHeldStack;
            currentFitStatus = null;
            Outliner.get().remove(SLOT);
            Networking.sendToServer(new MessageRequestHeldStackFit(requestedDisplayPos, requestId));
            return;
        }

        if (currentFitStatus == null || currentFitStatus == HeldStackFitStatus.NOT_APPLICABLE) {
            Outliner.get().remove(SLOT);
            return;
        }

        if (currentFitStatus == HeldStackFitStatus.DOES_NOT_FIT && ClientConfig.get().rendering.heldStackFitOutlineRenderMode.get() == HeldStackFitRenderMode.GREEN_ONLY) {
            Outliner.get().remove(SLOT);
            return;
        }

        Outliner.get().showAABB(SLOT, shape.bounds().move(displayPos), 1)
                .withFaceTexture(Theurgy.loc("block/overlay/white"))
                .colored(currentFitStatus == HeldStackFitStatus.FITS ? Color.GREEN : Color.RED)
                .lineWidth(1 / 32f);
    }

    public static void acceptServerFit(BlockPos displayPos, int requestId, HeldStackFitStatus status) {
        if (requestId != HeldStackFitOutline.requestId || requestedDisplayPos == null || !requestedDisplayPos.equals(displayPos)) {
            return;
        }

        currentFitStatus = status;
    }

    private static boolean hasProvider(Level level, BlockPos pos, BlockState state) {
        BlockPos queryPos = resolveQueryPos(pos, state);
        BlockEntity blockEntity = level.getBlockEntity(queryPos);
        return blockEntity instanceof HeldStackFitProvider;
    }

    private static BlockPos resolveQueryPos(BlockPos pos, BlockState state) {
        if (state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) && state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
            return pos.below();
        }

        return pos;
    }

    private static boolean requestChanged(Level level, BlockPos displayPos, ItemStack heldStack) {
        return requestedDimension == null
                || requestedDisplayPos == null
                || !requestedDimension.equals(level.dimension())
                || !requestedDisplayPos.equals(displayPos)
                || !ItemStack.isSameItemSameComponents(requestedHeldStack, heldStack);
    }

    private static void clear() {
        requestedHeldStack = ItemStack.EMPTY;
        requestedDisplayPos = null;
        requestedDimension = null;
        currentFitStatus = null;
        Outliner.get().remove(SLOT);
    }
}
