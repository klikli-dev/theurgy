// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDRegistry;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDSnapshot;
import com.klikli_dev.theurgy.network.Message;
import com.klikli_dev.theurgy.network.Networking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public record MessageRequestInWorldHUD(BlockPos pos) implements Message {

    public static final Type<MessageRequestInWorldHUD> TYPE = new Type<>(Theurgy.loc("request_in_world_hud"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageRequestInWorldHUD> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            MessageRequestInWorldHUD::pos,
            MessageRequestInWorldHUD::new
    );

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        InWorldHUDSnapshot snapshot = InWorldHUDSnapshot.EMPTY;

        if (level.isLoaded(this.pos) && player.isWithinBlockInteractionRange(this.pos, 1.0) && this.isActuallyLookingAt(player, level)) {
            snapshot = InWorldHUDRegistry.gatherServerSnapshot(player, level, this.pos);
        }

        Networking.sendTo(player, new MessageSyncInWorldHUD(this.pos, snapshot));
    }

    private boolean isActuallyLookingAt(ServerPlayer player, ServerLevel level) {
        var eyePosition = player.getEyePosition();
        var lookTarget = eyePosition.add(player.calculateViewVector(player.getXRot(), player.getYRot()).scale(player.blockInteractionRange()));
        BlockHitResult hitResult = level.clip(new ClipContext(eyePosition, lookTarget, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        return hitResult.getType() == HitResult.Type.BLOCK && this.pos.equals(hitResult.getBlockPos());
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
