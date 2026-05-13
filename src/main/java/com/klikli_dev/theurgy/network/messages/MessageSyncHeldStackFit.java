// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.render.HeldStackFitOutline;
import com.klikli_dev.theurgy.content.render.HeldStackFitStatus;
import com.klikli_dev.theurgy.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public record MessageSyncHeldStackFit(BlockPos displayPos, int requestId,
                                      HeldStackFitStatus status) implements Message {

    public static final Type<MessageSyncHeldStackFit> TYPE = new Type<>(Theurgy.loc("sync_held_stack_fit"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSyncHeldStackFit> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            MessageSyncHeldStackFit::displayPos,
            ByteBufCodecs.INT,
            MessageSyncHeldStackFit::requestId,
            HeldStackFitStatus.STREAM_CODEC,
            MessageSyncHeldStackFit::status,
            MessageSyncHeldStackFit::new
    );

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        HeldStackFitOutline.acceptServerFit(this.displayPos, this.requestId, this.status);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
