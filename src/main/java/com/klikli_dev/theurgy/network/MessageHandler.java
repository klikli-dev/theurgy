/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.network;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageHandler {

    public static <T extends Message> void handle(T message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ctx.get().enqueueWork(() -> {
                handleServer(message, ctx);
            });
        } else {
            ctx.get().enqueueWork(() -> {
                //separate class to avoid loading client code on server.
                ClientMessageHandler.handleClient(message, ctx);
            });
        }
        ctx.get().setPacketHandled(true);
    }

    public static <T extends Message> void handleServer(T message, Supplier<NetworkEvent.Context> ctx) {
        MinecraftServer server = ctx.get().getSender().level.getServer();
        message.onServerReceived(server, ctx.get().getSender(), ctx.get());
    }

    public static class ClientMessageHandler {

        public static <T extends Message> void handleClient(T message, Supplier<NetworkEvent.Context> ctx) {
            Minecraft minecraft = Minecraft.getInstance();
            message.onClientReceived(minecraft, minecraft.player, ctx.get());
        }
    }
}
