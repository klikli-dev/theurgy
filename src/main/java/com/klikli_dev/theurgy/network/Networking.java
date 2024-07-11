// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.network.messages.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class Networking {

    public static void register(final RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(Theurgy.MODID);

        //to server
        registrar.playToServer(MessageSetDivinationResult.TYPE, MessageSetDivinationResult.STREAM_CODEC, MessageHandler::handle);
        registrar.playToServer(MessageSetMode.TYPE, MessageSetMode.STREAM_CODEC, MessageHandler::handle);
        registrar.playToServer(MessageCaloricFluxEmitterSelection.TYPE, MessageCaloricFluxEmitterSelection.STREAM_CODEC, MessageHandler::handle);
        registrar.playToServer(MessageSulfuricFluxEmitterSelection.TYPE, MessageSulfuricFluxEmitterSelection.STREAM_CODEC, MessageHandler::handle);
        registrar.playToServer(MessageClearMenu.TYPE, MessageClearMenu.STREAM_CODEC, MessageHandler::handle);
        registrar.playToServer(MessageSetListFilterScreenOption.TYPE, MessageSetListFilterScreenOption.STREAM_CODEC, MessageHandler::handle);
        registrar.playToServer(MessageOnLeftClickEmpty.TYPE, MessageOnLeftClickEmpty.STREAM_CODEC, MessageHandler::handle);

        //to client
        registrar.playToClient(MessageRequestCaloricFluxEmitterSelection.TYPE, MessageRequestCaloricFluxEmitterSelection.STREAM_CODEC, MessageHandler::handle);
        registrar.playToClient(MessageRequestSulfuricFluxEmitterSelection.TYPE, MessageRequestSulfuricFluxEmitterSelection.STREAM_CODEC, MessageHandler::handle);
        registrar.playToClient(MessageShowCaloricFlux.TYPE, MessageShowCaloricFlux.STREAM_CODEC, MessageHandler::handle);
        registrar.playToClient(MessageShowSulfuricFluxEmitterStatus.TYPE, MessageShowSulfuricFluxEmitterStatus.STREAM_CODEC, MessageHandler::handle);
        registrar.playToClient(MessageShowLogisticsNodeStatus.TYPE, MessageShowLogisticsNodeStatus.STREAM_CODEC, MessageHandler::handle);
        registrar.playToClient(MessageAddWires.TYPE, MessageAddWires.STREAM_CODEC, MessageHandler::handle);
        registrar.playToClient(MessageRemoveWires.TYPE, MessageRemoveWires.STREAM_CODEC, MessageHandler::handle);
    }


    public static <T extends Message> void sendTo(ServerPlayer player, T message) {
        PacketDistributor.sendToPlayer(player, message);
    }

    public static <T extends Message> void sendToTracking(ServerLevel level, ChunkPos chunkPos, T message) {
        PacketDistributor.sendToPlayersTrackingChunk(level, chunkPos, message);
    }

    public static <T extends Message> void sendToServer(T message) {
        PacketDistributor.sendToServer(message);
    }
}
