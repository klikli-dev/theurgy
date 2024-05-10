// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import com.klikli_dev.theurgy.network.Message;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.List;

public class MessageShowLogisticsNodeStatus implements Message {

    public static final ResourceLocation ID = Theurgy.loc("show_logistics_node_status");

    private List<Pair<BlockPos, Integer>> blockPos;

    public MessageShowLogisticsNodeStatus(List<Pair<BlockPos, Integer>> blockPos) {
        this.blockPos = blockPos;
    }

    public MessageShowLogisticsNodeStatus(FriendlyByteBuf buf) {
        this.decode(buf);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(this.blockPos, (buffer, pair) -> {
            buffer.writeBlockPos(pair.getFirst());
            buffer.writeInt(pair.getSecond());
        });
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.blockPos = buf.readList((buffer) -> {
            BlockPos pos = buffer.readBlockPos();
            int status = buffer.readInt();
            return Pair.of(pos, status);
        });
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        Level level = player.level();

        for (var target : this.blockPos) {
            Outliner.get().showAABB(target, Shapes.block().bounds().move(target.getFirst()), (int)(20 * 2.5))
                    .colored(target.getSecond())
                    .lineWidth(1 / 16f);
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
