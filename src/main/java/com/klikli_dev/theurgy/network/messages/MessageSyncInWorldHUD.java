// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUD;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDSnapshot;
import com.klikli_dev.theurgy.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public record MessageSyncInWorldHUD(BlockPos pos, InWorldHUDSnapshot snapshot) implements Message {

    public static final Type<MessageSyncInWorldHUD> TYPE = new Type<>(Theurgy.loc("sync_in_world_hud"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSyncInWorldHUD> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            MessageSyncInWorldHUD::pos,
            InWorldHUDSnapshot.STREAM_CODEC,
            MessageSyncInWorldHUD::snapshot,
            MessageSyncInWorldHUD::new
    );

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        InWorldHUD.get().acceptServerSnapshot(this.pos, this.snapshot);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
