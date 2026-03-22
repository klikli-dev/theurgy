// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterSelectedPoint;
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
import java.util.Optional;

public class MessageSulfuricFluxEmitterSelection implements Message {
    public static final Type<MessageSulfuricFluxEmitterSelection> TYPE = new Type<>(Theurgy.loc("sulfuric_flux_emitter_selection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSulfuricFluxEmitterSelection> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    (m) -> m.blockPos,
                    SulfuricFluxEmitterSelectedPoint.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    (m) -> m.sourcePedestals,
                    ByteBufCodecs.optional(SulfuricFluxEmitterSelectedPoint.STREAM_CODEC),
                    (m) -> Optional.ofNullable(m.targetPedestal),
                    ByteBufCodecs.optional(SulfuricFluxEmitterSelectedPoint.STREAM_CODEC),
                    (m) -> Optional.ofNullable(m.resultPedestal),
                    (blockPos, sourcePedestals, targetPedestal, resultPedestal) -> new MessageSulfuricFluxEmitterSelection(blockPos, sourcePedestals, targetPedestal.orElse(null), resultPedestal.orElse(null))
            );

    private final List<SulfuricFluxEmitterSelectedPoint> sourcePedestals;
    private final SulfuricFluxEmitterSelectedPoint targetPedestal;
    private final SulfuricFluxEmitterSelectedPoint resultPedestal;
    private final BlockPos blockPos;

    public MessageSulfuricFluxEmitterSelection(BlockPos blockPos, List<SulfuricFluxEmitterSelectedPoint> sourcePedestals, SulfuricFluxEmitterSelectedPoint targetPedestal, SulfuricFluxEmitterSelectedPoint resultPedestal) {
        this.blockPos = blockPos;
        this.sourcePedestals = new ArrayList<>(sourcePedestals);
        this.targetPedestal = targetPedestal;
        this.resultPedestal = resultPedestal;
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        Level level = player.level();

        if (level == null || !level.isLoaded(this.blockPos))
            return;

        this.sourcePedestals.forEach(point -> point.setLevel(level));
        if (this.targetPedestal != null)
            this.targetPedestal.setLevel(level);
        if (this.resultPedestal != null)
            this.resultPedestal.setLevel(level);

        BlockEntity blockEntity = level.getBlockEntity(this.blockPos);
        if (blockEntity instanceof SulfuricFluxEmitterBlockEntity sulfuricFluxEmitterBlockEntity)
            sulfuricFluxEmitterBlockEntity.setSelectedPoints(this.sourcePedestals, this.targetPedestal, this.resultPedestal);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
