// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterSelectedPoint;
import com.klikli_dev.theurgy.network.Message;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;

public class MessageShowSulfuricFluxEmitterStatus implements Message {

    private List<SulfuricFluxEmitterSelectedPoint> sourcePedestals;
    private SulfuricFluxEmitterSelectedPoint targetPedestal;
    private SulfuricFluxEmitterSelectedPoint resultPedestal;
    private BlockPos blockPos;

    public MessageShowSulfuricFluxEmitterStatus(BlockPos blockPos, List<SulfuricFluxEmitterSelectedPoint> sourcePedestals, SulfuricFluxEmitterSelectedPoint targetPedestal, SulfuricFluxEmitterSelectedPoint resultPedestal) {
        this.blockPos = blockPos;
        this.sourcePedestals = sourcePedestals;
        this.targetPedestal = targetPedestal;
        this.resultPedestal = resultPedestal;
    }

    public MessageShowSulfuricFluxEmitterStatus(FriendlyByteBuf buf) {
        this.decode(buf);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeCollection(this.sourcePedestals, (buf1, point) -> buf1.writeJsonWithCodec(point.codec(), point));
        buf.writeNullable(this.targetPedestal, (buf1, point) -> buf1.writeJsonWithCodec(point.codec(), point));
        buf.writeNullable(this.resultPedestal, (buf1, point) -> buf1.writeJsonWithCodec(point.codec(), point));
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
        this.sourcePedestals = buf.readList(buf1 -> buf1.readJsonWithCodec(SulfuricFluxEmitterSelectedPoint.CODEC));
        this.targetPedestal = buf.readNullable(buf1 -> buf1.readJsonWithCodec(SulfuricFluxEmitterSelectedPoint.CODEC));
        this.resultPedestal = buf.readNullable(buf1 -> buf1.readJsonWithCodec(SulfuricFluxEmitterSelectedPoint.CODEC));
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player, NetworkEvent.Context context) {
        Level level = player.level();

        this.sourcePedestals.forEach(point -> point.setLevel(level));
        if(this.targetPedestal != null)
            this.targetPedestal.setLevel(level);
        if(this.resultPedestal != null)
            this.resultPedestal.setLevel(level);

        BlockEntity blockEntity = level.getBlockEntity(this.blockPos);
        if (blockEntity instanceof SulfuricFluxEmitterBlockEntity sulfuricFluxEmitter){
            sulfuricFluxEmitter.setSelectedPointsClient(this.sourcePedestals, this.targetPedestal, this.resultPedestal);
        }

        BlockRegistry.SULFURIC_FLUX_EMITTER.get().interactionBehaviour().showStatus(level, this.blockPos, player);
    }
}
