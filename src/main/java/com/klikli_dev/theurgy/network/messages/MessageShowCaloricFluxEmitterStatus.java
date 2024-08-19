// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterSelectedPoint;
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

public class MessageShowCaloricFluxEmitterStatus implements Message {
    public static final Type<MessageShowCaloricFluxEmitterStatus> TYPE = new Type<>(Theurgy.loc("show_caloric_flux_emitter_status"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageShowCaloricFluxEmitterStatus> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    (m) -> m.blockPos,
                    CaloricFluxEmitterSelectedPoint.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    (m) -> m.selectedPoints,
                    MessageShowCaloricFluxEmitterStatus::new
            );

    private final List<CaloricFluxEmitterSelectedPoint> selectedPoints;
    private final BlockPos blockPos;

    public MessageShowCaloricFluxEmitterStatus(BlockPos blockPos, List<CaloricFluxEmitterSelectedPoint> selectedPoints) {
        this.blockPos = blockPos;
        this.selectedPoints = selectedPoints;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        Level level = player.level();

        this.selectedPoints.forEach(point -> point.setLevel(level));

        BlockEntity blockEntity = level.getBlockEntity(this.blockPos);
        if (blockEntity instanceof CaloricFluxEmitterBlockEntity caloricFluxEmitter) {
            caloricFluxEmitter.setSelectedPointsClient(this.selectedPoints);
        }

        BlockRegistry.CALORIC_FLUX_EMITTER.get().interactionBehaviour().showStatus(level, this.blockPos, player);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
