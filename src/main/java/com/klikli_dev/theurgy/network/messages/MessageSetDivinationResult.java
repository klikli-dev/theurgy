/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.DivinationRodItem;
import com.klikli_dev.theurgy.network.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class MessageSetDivinationResult implements Message {

    public BlockPos pos;
    public byte distance;

    public MessageSetDivinationResult(FriendlyByteBuf buf) {
        this.decode(buf);
    }

    public MessageSetDivinationResult(BlockPos pos, float distance) {
        this.pos = pos;
        this.distance = (byte) Math.min(256, distance);
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player,
                                 NetworkEvent.Context context) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof DivinationRodItem) {
            var tag = stack.getOrCreateTag();
            tag.putFloat(TheurgyConstants.Nbt.Divination.DISTANCE, this.distance);
            if(this.pos != null) {
                tag.putLong(TheurgyConstants.Nbt.Divination.POS, this.pos.asLong());
            }
            player.inventoryMenu.broadcastChanges();
        }
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.pos != null);
        if (this.pos != null) {
            buf.writeBlockPos(this.pos);
        }
        buf.writeByte(this.distance);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            this.pos = buf.readBlockPos();
        }
        this.distance = buf.readByte();
    }

}
