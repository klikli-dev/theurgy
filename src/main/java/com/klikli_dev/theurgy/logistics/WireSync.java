// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.logistics;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageAddWires;
import com.klikli_dev.theurgy.network.messages.MessageRemoveWires;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class WireSync {
    private static final WireSync instance = new WireSync();

    private final Map<UUID, PlayerWireState> playerWireStates = new Object2ObjectOpenHashMap<>();
    private final SetMultimap<Wire, UUID> wireToWatchingPlayers = HashMultimap.create();

    public static WireSync get() {
        return instance;
    }

    private static final class PlayerWireState {
        private final Set<ChunkPos> watchedChunks = new ObjectOpenHashSet<>();
        private final Object2IntOpenHashMap<Wire> watchedWireCounts = new Object2IntOpenHashMap<>();
        private final Set<Wire> pendingRemovals = new ObjectOpenHashSet<>();
    }

    /**
     * Needs to be called BEFORE the wire has been removed from the serverside wire multimaps
     */
    public void sendRemoveWireToWatchingPlayers(ServerLevel level, Wire wire) {
        var players = this.wireToWatchingPlayers.get(wire);
        var message = new MessageRemoveWires(Set.of(wire));

        for (var playerUUID : players) {
            var player = level.getServer().getPlayerList().getPlayer(playerUUID);
            if (player != null) {
                Networking.sendTo(player, message);
            }
        }
    }

    /**
     * Needs to be called AFTER the wire has been added to the serverside wire multimaps
     */
    public void sendAddWireToWatchingPlayers(ServerLevel level, Wire wire) {
        var players = this.wireToWatchingPlayers.get(wire);
        var message = new MessageAddWires(Set.of(wire));

        for (var playerUUID : players) {
            var player = level.getServer().getPlayerList().getPlayer(playerUUID);
            if (player != null) {
                Networking.sendTo(player, message);
            }
        }
    }

    private void watchChunk(ServerPlayer player, ChunkPos chunkPos) {
        PlayerWireState state = this.playerWireStates.computeIfAbsent(player.getUUID(), k -> new PlayerWireState());

        if (!state.watchedChunks.add(chunkPos)) {
            return;
        }

        Set<Wire> wiresToAdd = new ObjectOpenHashSet<>();
        for (Wire wire : Wires.get(player.level()).getWires(chunkPos)) {
            if (state.watchedWireCounts.getInt(wire) == 0) {
                wiresToAdd.add(wire);
                this.wireToWatchingPlayers.put(wire, player.getUUID());
            }
            state.watchedWireCounts.addTo(wire, 1);
            state.pendingRemovals.remove(wire);
        }

        if (!wiresToAdd.isEmpty()) {
            Networking.sendTo(player, new MessageAddWires(wiresToAdd));
        }
    }

    private void unwatchChunk(ServerPlayer player, ChunkPos chunkPos) {
        PlayerWireState state = this.playerWireStates.get(player.getUUID());
        if (state == null) {
            return;
        }

        if (!state.watchedChunks.remove(chunkPos)) {
            return;
        }

        for (Wire wire : Wires.get(player.level()).getWires(chunkPos)) {
            int oldCount = state.watchedWireCounts.getInt(wire);
            if (oldCount <= 1) {
                state.watchedWireCounts.removeInt(wire);
                state.pendingRemovals.add(wire);
                this.wireToWatchingPlayers.remove(wire, player.getUUID());
            } else {
                state.watchedWireCounts.put(wire, oldCount - 1);
            }
        }
    }

    public void onChunkWatch(ChunkWatchEvent.Watch event) {
        this.watchChunk(event.getPlayer(), event.getPos());
    }

    public void onChunkUnWatch(ChunkWatchEvent.UnWatch event) {
        this.unwatchChunk(event.getPlayer(), event.getPos());
    }

    public void onServerTick(ServerTickEvent.Post event) {
        for (var iterator = this.playerWireStates.entrySet().iterator(); iterator.hasNext(); ) {
            var entry = iterator.next();
            PlayerWireState state = entry.getValue();
            if (state.pendingRemovals.isEmpty()) {
                continue;
            }

            ServerPlayer player = event.getServer().getPlayerList().getPlayer(entry.getKey());
            if (player != null) {
                Networking.sendTo(player, new MessageRemoveWires(new ObjectOpenHashSet<>(state.pendingRemovals)));
            }
            state.pendingRemovals.clear();
            if (state.watchedWireCounts.isEmpty() && state.watchedChunks.isEmpty()) {
                iterator.remove();
            }
        }
    }

    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerWireState state = this.playerWireStates.remove(event.getEntity().getUUID());
        if (state != null) {
            for (Wire wire : state.watchedWireCounts.keySet()) {
                this.wireToWatchingPlayers.remove(wire, event.getEntity().getUUID());
            }
        }
    }
}
