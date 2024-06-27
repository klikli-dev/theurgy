// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.gui.menu.IClearableMenu;
import com.klikli_dev.theurgy.network.Message;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;


public class MessageClearMenu implements Message {
    public static final MessageClearMenu INSTANCE = new MessageClearMenu();

    public static final Type<MessageClearMenu> TYPE = new Type<>(Theurgy.loc("clear_menu"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageClearMenu> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private MessageClearMenu() {
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        if (player.containerMenu instanceof IClearableMenu menu)
            menu.clearContents();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
