// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterSelectedPoint;
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

public class MessageCaloricFluxEmitterSelection implements Message {
    public static final Type<MessageCaloricFluxEmitterSelection> TYPE = new Type<>(Theurgy.loc("caloric_flux_emitter_selection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageCaloricFluxEmitterSelection> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    (m) -> m.blockPos,
                    CaloricFluxEmitterSelectedPoint.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    (m) -> m.selectedPoints,
                    MessageCaloricFluxEmitterSelection::new
            );

    private final List<CaloricFluxEmitterSelectedPoint> selectedPoints;
    private final BlockPos blockPos;

    public MessageCaloricFluxEmitterSelection(BlockPos blockPos, List<CaloricFluxEmitterSelectedPoint> selectedPoints) {
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
        if (blockEntity instanceof CaloricFluxEmitterBlockEntity caloricFluxEmitterBlockEntity)
            caloricFluxEmitterBlockEntity.setSelectedPoints(this.selectedPoints);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
