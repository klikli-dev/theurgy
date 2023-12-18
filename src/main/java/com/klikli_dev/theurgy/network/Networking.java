// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.network.messages.*;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.filters.VanillaPacketSplitter;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

public class Networking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Theurgy.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        register(MessageSetDivinationResult.class, NetworkDirection.PLAY_TO_SERVER);
        register(MessageCaloricFluxEmitterSelection.class, NetworkDirection.PLAY_TO_SERVER);
        register(MessageSulfuricFluxEmitterSelection.class, NetworkDirection.PLAY_TO_SERVER);

        register(MessageRequestCaloricFluxEmitterSelection.class, NetworkDirection.PLAY_TO_CLIENT);
        register(MessageRequestSulfuricFluxEmitterSelection.class, NetworkDirection.PLAY_TO_CLIENT);
        register(MessageShowCaloricFlux.class, NetworkDirection.PLAY_TO_CLIENT);
        register(MessageShowSulfuricFluxEmitterStatus.class, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T extends Message> void register(Class<T> clazz, NetworkDirection networkDirection){
       register(clazz, buf -> {
           try {
               return clazz.getConstructor(FriendlyByteBuf.class).newInstance(buf);
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
       }, networkDirection);
    }

    public static <T extends Message> void register(Class<T> clazz, Function<FriendlyByteBuf, T> decoder, NetworkDirection networkDirection){
        INSTANCE.registerMessage(nextID(),
                clazz,
                T::encode,
                decoder,
                MessageHandler::handle, Optional.of(networkDirection));
    }

    public static <T> void sendToSplit(ServerPlayer player, T message) {
        Packet<?> vanillaPacket = INSTANCE.toVanillaPacket(message, NetworkDirection.PLAY_TO_CLIENT);
        var packets = new ArrayList<Packet<?>>();
        VanillaPacketSplitter.appendPackets(ConnectionProtocol.PLAY, PacketFlow.CLIENTBOUND, vanillaPacket, packets);
        packets.forEach(player.connection::send);
    }

    public static <T> void sendTo(ServerPlayer player, T message) {
        if (player.connection == null) {
            //workaround for https://github.com/klikli-dev/modonomicon/issues/46 / https://github.com/klikli-dev/modonomicon/issues/62
            //we should never get here unless some other mod interferes with networking
            Theurgy.LOGGER.warn("Tried to send message of type {} to player without connection. Id: {}, Name: {}.", player.getStringUUID(), player.getName().getString(), message.getClass().getName());
            return;
        }
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <T> void sendToTracking(Entity entity, T message) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

    public static <T> void sendToTracking(LevelChunk chunk, T message) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
    }

    public static <T> void sendToDimension(ResourceKey<Level> dimensionKey, T message) {
        INSTANCE.send(PacketDistributor.DIMENSION.with(() -> dimensionKey), message);
    }

    public static <T> void sendToServer(T message) {
        INSTANCE.sendToServer(message);
    }
}
