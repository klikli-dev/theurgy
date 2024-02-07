// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.network.messages.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class Networking {

    public static void register(final RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(Theurgy.MODID);

        registrar.play(MessageSetDivinationResult.ID, MessageSetDivinationResult::new, MessageHandler::handle);
        registrar.play(MessageSetMode.ID, MessageSetMode::new, MessageHandler::handle);
        registrar.play(MessageCaloricFluxEmitterSelection.ID, MessageCaloricFluxEmitterSelection::new, MessageHandler::handle);
        registrar.play(MessageSulfuricFluxEmitterSelection.ID, MessageSulfuricFluxEmitterSelection::new, MessageHandler::handle);
        registrar.play(MessageLogisticsNodeSelection.ID, MessageLogisticsNodeSelection::new, MessageHandler::handle);

        registrar.play(MessageRequestCaloricFluxEmitterSelection.ID, MessageRequestCaloricFluxEmitterSelection::new, MessageHandler::handle);
        registrar.play(MessageRequestSulfuricFluxEmitterSelection.ID, MessageRequestSulfuricFluxEmitterSelection::new, MessageHandler::handle);
        registrar.play(MessageRequestLogisticsNodeSelection.ID, MessageRequestLogisticsNodeSelection::new, MessageHandler::handle);
        registrar.play(MessageShowCaloricFlux.ID, MessageShowCaloricFlux::new, MessageHandler::handle);
        registrar.play(MessageShowSulfuricFluxEmitterStatus.ID, MessageShowSulfuricFluxEmitterStatus::new, MessageHandler::handle);
        registrar.play(MessageAddWires.ID, MessageAddWires::new, MessageHandler::handle);
        registrar.play(MessageRemoveWires.ID, MessageRemoveWires::new, MessageHandler::handle);
    }


    public static <T extends Message> void sendTo(ServerPlayer player, T message) {
        PacketDistributor.PLAYER.with(player).send(message);
    }

    public static <T extends Message> void sendToServer(T message) {
        PacketDistributor.SERVER.noArg().send(message);
    }

    public static <T extends Message> void sendToTracking(Entity entity, T message) {
        PacketDistributor.TRACKING_ENTITY.with(entity).send(message);
    }

    public static <T extends Message> void sendToTracking(LevelChunk chunk, T message) {
        PacketDistributor.TRACKING_CHUNK.with(chunk).send(message);
    }

    public static <T extends Message> void sendToDimension(ResourceKey<Level> dimensionKey, T message) {
        PacketDistributor.DIMENSION.with(dimensionKey).send(message);
    }
}
