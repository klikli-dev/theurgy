// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.mercurialwand.mode.MercurialWandItemMode;
import com.klikli_dev.theurgy.content.item.mercurialwand.mode.SelectDirectionMode;
import com.klikli_dev.theurgy.content.item.mode.ModeItem;
import com.klikli_dev.theurgy.network.Message;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;


public class MessageItemModeSelectDirection implements Message {

    public static final ResourceLocation ID = Theurgy.loc("select_direction");

    public Direction direction;

    public MessageItemModeSelectDirection(FriendlyByteBuf buf) {
        this.decode(buf);
    }

    public MessageItemModeSelectDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeByte(this.direction.ordinal());
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.direction = Direction.values()[buf.readByte()];
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof ModeItem modeItem) {
            //theoretically the current mode should be the select direction mode,
            // but if we run into some sort of race condition it is better to just get it directly
            var mode = (SelectDirectionMode) MercurialWandItemMode.Type.SELECT_DIRECTION.mode();
            mode.setDirection(stack, this.direction);
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
