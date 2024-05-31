// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.mode.ModeItem;
import com.klikli_dev.theurgy.network.Message;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class MessageSetMode implements Message {
    public static final Type<MessageSetMode> TYPE = new Type<>(Theurgy.loc("set_mode"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSetMode> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BYTE,
                    (m) -> m.shift,
                    MessageSetMode::new
            );


    public final byte shift;

    public MessageSetMode(int shift) {
        this((byte) shift);
    }

    public MessageSetMode(byte shift) {
        this.shift = shift;
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof ModeItem modeItem) {
            modeItem.changeMode(player, stack, this.shift);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
