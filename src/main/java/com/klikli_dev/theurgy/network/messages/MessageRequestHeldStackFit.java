// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.render.HeldStackFitProvider;
import com.klikli_dev.theurgy.content.render.HeldStackFitStatus;
import com.klikli_dev.theurgy.network.Message;
import com.klikli_dev.theurgy.network.Networking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public record MessageRequestHeldStackFit(BlockPos displayPos, int requestId) implements Message {

    public static final Type<MessageRequestHeldStackFit> TYPE = new Type<>(Theurgy.loc("request_held_stack_fit"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageRequestHeldStackFit> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            MessageRequestHeldStackFit::displayPos,
            ByteBufCodecs.INT,
            MessageRequestHeldStackFit::requestId,
            MessageRequestHeldStackFit::new
    );

    private static BlockPos resolveQueryPos(BlockPos pos, BlockState state) {
        if (state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) && state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
            return pos.below();
        }

        return pos;
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        ServerLevel level = player.level();
        HeldStackFitStatus status = HeldStackFitStatus.NOT_APPLICABLE;

        if (level.isLoaded(this.displayPos) && this.isActuallyLookingAt(player, level)) {
            BlockState displayState = level.getBlockState(this.displayPos);
            BlockEntity blockEntity = level.getBlockEntity(resolveQueryPos(this.displayPos, displayState));
            if (blockEntity instanceof HeldStackFitProvider provider && !player.getMainHandItem().isEmpty()) {
                status = HeldStackFitStatus.of(provider.heldStackFits(player.getMainHandItem()));
            }
        }

        Networking.sendTo(player, new MessageSyncHeldStackFit(this.displayPos, this.requestId, status));
    }

    private boolean isActuallyLookingAt(ServerPlayer player, ServerLevel level) {
        var eyePosition = player.getEyePosition();
        var lookTarget = eyePosition.add(player.calculateViewVector(player.getXRot(), player.getYRot()).scale(player.blockInteractionRange()));
        BlockHitResult hitResult = level.clip(new ClipContext(eyePosition, lookTarget, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        return hitResult.getType() == HitResult.Type.BLOCK && this.displayPos.equals(hitResult.getBlockPos());
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
