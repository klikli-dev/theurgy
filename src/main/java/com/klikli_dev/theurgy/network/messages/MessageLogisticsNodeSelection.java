// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.logisticsnode.LogisticsNodeSelectedPoint;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.network.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class MessageLogisticsNodeSelection implements Message {

    public static final ResourceLocation ID = Theurgy.loc("logistics_node_selection");

    private List<LogisticsNodeSelectedPoint> selectedPoints;
    private BlockPos blockPos;

    public MessageLogisticsNodeSelection(BlockPos blockPos, List<LogisticsNodeSelectedPoint> selectedPoints) {
        this.blockPos = blockPos;
        this.selectedPoints = new ArrayList<>(selectedPoints);
    }

    public MessageLogisticsNodeSelection(FriendlyByteBuf buf) {
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
        this.selectedPoints = buf.readList(buf1 -> buf1.readJsonWithCodec(LogisticsNodeSelectedPoint.CODEC));
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        Level level = player.level();

        if (level == null || !level.isLoaded(this.blockPos))
            return;

        this.selectedPoints.forEach(point -> Logistics.get().add(this.blockPos, point.getBlockPos()));
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
