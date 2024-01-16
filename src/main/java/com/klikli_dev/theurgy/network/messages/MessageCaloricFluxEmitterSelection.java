// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterSelectedPoint;
import com.klikli_dev.theurgy.network.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.NetworkEvent;
import java.util.List;

public class MessageCaloricFluxEmitterSelection implements Message {

    private List<CaloricFluxEmitterSelectedPoint> selectedPoints;
    private BlockPos blockPos;

    public MessageCaloricFluxEmitterSelection(BlockPos blockPos, List<CaloricFluxEmitterSelectedPoint> selectedPoints) {
        this.blockPos = blockPos;
        this.selectedPoints = selectedPoints;
    }

    public MessageCaloricFluxEmitterSelection(FriendlyByteBuf buf) {
        this.decode(buf);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeCollection(this.selectedPoints, (buf1, point) -> buf1.writeJsonWithCodec(point.codec(), point));
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
        this.selectedPoints = buf.readList(buf1 -> buf1.readJsonWithCodec(CaloricFluxEmitterSelectedPoint.CODEC));
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player, NetworkEvent.Context context) {
        Level level = player.level();

        if (level == null || !level.isLoaded(this.blockPos))
            return;

        this.selectedPoints.forEach(point -> point.setLevel(level));

        BlockEntity blockEntity = level.getBlockEntity(this.blockPos);
        if (blockEntity instanceof CaloricFluxEmitterBlockEntity caloricFluxEmitterBlockEntity)
            caloricFluxEmitterBlockEntity.setSelectedPoints(this.selectedPoints);
    }
}
