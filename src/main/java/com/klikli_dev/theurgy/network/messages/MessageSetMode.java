// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.DivinationRodItem;
import com.klikli_dev.theurgy.content.item.ModeItem;
import com.klikli_dev.theurgy.network.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;


public class MessageSetMode implements Message {

    public static final ResourceLocation ID = Theurgy.loc("set_mode");

    public byte shift;

    public MessageSetMode(FriendlyByteBuf buf) {
        this.decode(buf);
    }

    public MessageSetMode(int shift) {
        this.shift = (byte) shift;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeByte(this.shift);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.shift = buf.readByte();
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof ModeItem modeItem) {
            modeItem.changeMode(player, stack, this.shift);
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
