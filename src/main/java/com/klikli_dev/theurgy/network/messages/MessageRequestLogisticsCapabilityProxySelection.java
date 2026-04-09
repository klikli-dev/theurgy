// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.network.Message;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class MessageRequestLogisticsCapabilityProxySelection implements Message {
    public static final Type<MessageRequestLogisticsCapabilityProxySelection> TYPE = new Type<>(Theurgy.loc("request_logistics_capability_proxy_selection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageRequestLogisticsCapabilityProxySelection> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    (m) -> m.blockPos,
                    MessageRequestLogisticsCapabilityProxySelection::new
            );

    private final BlockPos blockPos;

    public MessageRequestLogisticsCapabilityProxySelection(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        BlockRegistry.LOGISTICS_CAPABILITY_PROXY.get().selectionBehaviour().onPlace(this.blockPos, player);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
