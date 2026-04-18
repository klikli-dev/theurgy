// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.mercuryfluxemitter.MercuryFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.mercuryfluxemitter.MercuryFluxEmitterSelectedPoint;
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

public class MessageMercuryFluxEmitterSelection implements Message {
    public static final Type<MessageMercuryFluxEmitterSelection> TYPE = new Type<>(Theurgy.loc("mercury_flux_emitter_selection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageMercuryFluxEmitterSelection> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    (m) -> m.blockPos,
                    MercuryFluxEmitterSelectedPoint.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    (m) -> m.selectedPoints,
                    MessageMercuryFluxEmitterSelection::new
            );

    private final List<MercuryFluxEmitterSelectedPoint> selectedPoints;
    private final BlockPos blockPos;

    public MessageMercuryFluxEmitterSelection(BlockPos blockPos, List<MercuryFluxEmitterSelectedPoint> selectedPoints) {
        this.blockPos = blockPos;
        this.selectedPoints = new ArrayList<>(selectedPoints);
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        Level level = player.level();

        if (level == null || !level.isLoaded(this.blockPos))
            return;

        this.selectedPoints.forEach(point -> point.setLevel(level));

        BlockEntity blockEntity = level.getBlockEntity(this.blockPos);
        if (blockEntity instanceof MercuryFluxEmitterBlockEntity mercuryFluxEmitterBlockEntity)
            mercuryFluxEmitterBlockEntity.setSelectedPoints(this.selectedPoints);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
