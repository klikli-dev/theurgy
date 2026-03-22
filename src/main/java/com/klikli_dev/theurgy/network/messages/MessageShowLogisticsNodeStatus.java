// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import com.klikli_dev.theurgy.network.Message;
import com.klikli_dev.theurgy.util.TheurgyExtraStreamCodecs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageShowLogisticsNodeStatus implements Message {
    public static final Type<MessageShowLogisticsNodeStatus> TYPE = new Type<>(Theurgy.loc("show_logistics_node_status"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageShowLogisticsNodeStatus> STREAM_CODEC =
            StreamCodec.composite(
                    TheurgyExtraStreamCodecs.pair(BlockPos.STREAM_CODEC, ByteBufCodecs.INT).apply(ByteBufCodecs.list()),
                    (m) -> m.blockPos,
                    MessageShowLogisticsNodeStatus::new
            );

    private final List<Pair<BlockPos, Integer>> blockPos;

    public MessageShowLogisticsNodeStatus(List<Pair<BlockPos, Integer>> blockPos) {
        this.blockPos = blockPos;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        Level level = player.level();

        for (var target : this.blockPos) {
            Outliner.get().showAABB(target, Shapes.block().bounds().move(target.getFirst()), (int) (20 * 2.5))
                    .colored(target.getSecond())
                    .lineWidth(1 / 16f);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
