// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterSelectedPoint;
import com.klikli_dev.theurgy.network.Message;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class MessageShowSulfuricFluxEmitterStatus implements Message {
    public static final Type<MessageShowSulfuricFluxEmitterStatus> TYPE = new Type<>(Theurgy.loc("show_sulfuric_flux_emitter_status"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageShowSulfuricFluxEmitterStatus> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    (m) -> m.blockPos,
                    SulfuricFluxEmitterSelectedPoint.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    (m) -> m.sourcePedestals,
                    ByteBufCodecs.optional(SulfuricFluxEmitterSelectedPoint.STREAM_CODEC),
                    (m) -> Optional.ofNullable(m.targetPedestal),
                    ByteBufCodecs.optional(SulfuricFluxEmitterSelectedPoint.STREAM_CODEC),
                    (m) -> Optional.ofNullable(m.resultPedestal),
                    (blockPos, sourcePedestals, targetPedestal, resultPedestal) -> new MessageShowSulfuricFluxEmitterStatus(blockPos, sourcePedestals, targetPedestal.orElse(null), resultPedestal.orElse(null))
            );

    private final List<SulfuricFluxEmitterSelectedPoint> sourcePedestals;
    private final SulfuricFluxEmitterSelectedPoint targetPedestal;
    private final SulfuricFluxEmitterSelectedPoint resultPedestal;
    private final BlockPos blockPos;

    public MessageShowSulfuricFluxEmitterStatus(BlockPos blockPos, List<SulfuricFluxEmitterSelectedPoint> sourcePedestals, SulfuricFluxEmitterSelectedPoint targetPedestal, SulfuricFluxEmitterSelectedPoint resultPedestal) {
        this.blockPos = blockPos;
        this.sourcePedestals = sourcePedestals;
        this.targetPedestal = targetPedestal;
        this.resultPedestal = resultPedestal;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        Level level = player.level();

        this.sourcePedestals.forEach(point -> point.setLevel(level));
        if (this.targetPedestal != null)
            this.targetPedestal.setLevel(level);
        if (this.resultPedestal != null)
            this.resultPedestal.setLevel(level);

        BlockEntity blockEntity = level.getBlockEntity(this.blockPos);
        if (blockEntity instanceof SulfuricFluxEmitterBlockEntity sulfuricFluxEmitter) {
            sulfuricFluxEmitter.setSelectedPointsClient(this.sourcePedestals, this.targetPedestal, this.resultPedestal);
        }

        BlockRegistry.SULFURIC_FLUX_EMITTER.get().interactionBehaviour().showStatus(level, this.blockPos, player);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
