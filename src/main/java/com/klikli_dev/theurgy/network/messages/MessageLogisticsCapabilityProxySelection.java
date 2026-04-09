// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.logisticscapabilityproxy.LogisticsCapabilityProxyBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticscapabilityproxy.LogisticsCapabilityProxySelectedPoint;
import com.klikli_dev.theurgy.network.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MessageLogisticsCapabilityProxySelection implements Message {
    public static final Type<MessageLogisticsCapabilityProxySelection> TYPE = new Type<>(Theurgy.loc("logistics_capability_proxy_selection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageLogisticsCapabilityProxySelection> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    (m) -> m.blockPos,
                    LogisticsCapabilityProxySelectedPoint.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    (m) -> m.selectedPoints,
                    MessageLogisticsCapabilityProxySelection::new
            );

    private final List<LogisticsCapabilityProxySelectedPoint> selectedPoints;
    private final BlockPos blockPos;

    public MessageLogisticsCapabilityProxySelection(BlockPos blockPos, List<LogisticsCapabilityProxySelectedPoint> selectedPoints) {
        this.blockPos = blockPos;
        this.selectedPoints = new ArrayList<>(selectedPoints);
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        Level level = player.level();

        if (level == null || !level.isLoaded(this.blockPos)) {
            return;
        }

        this.selectedPoints.forEach(point -> point.setLevel(level));

        BlockEntity blockEntity = level.getBlockEntity(this.blockPos);
        if (blockEntity instanceof LogisticsCapabilityProxyBlockEntity proxyBlockEntity) {
            proxyBlockEntity.setLinkedProbes(this.selectedPoints.stream()
                    .map(LogisticsCapabilityProxySelectedPoint::getBlockPos)
                    .filter(pos -> pos.closerThan(this.blockPos, 32))
                    .toList());
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
