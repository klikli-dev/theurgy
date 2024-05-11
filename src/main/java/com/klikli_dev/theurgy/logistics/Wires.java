// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.logistics;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.klikli_dev.modonomicon.util.Codecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public class Wires extends SavedData {

    public static final String ID = "theurgy.wires";
    public static final Codec<Wires> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codecs.set(Wire.CODEC).fieldOf("wireConnections").forGetter(wires -> wires.wires),
            Codec.BOOL.fieldOf("isClient").forGetter(wires -> wires.isClient)
    ).apply(instance, Wires::new));
    private static final String NBT_TAG = "theurgy:wires";

    private static WeakReference<ServerLevel> cachedServerLevel = new WeakReference<>(null);
    private static WeakReference<Wires> cachedServerWires = new WeakReference<>(null);

    private static WeakReference<Level> cachedClientLevel = new WeakReference<>(null);
    private static WeakReference<Wires> cachedClientWires = new WeakReference<>(null);

    /**
     * Store all wire connections.
     * Server only - if we modify this on the logical client we get into trouble because this is a singleton - we'd remove them permanently
     */
    private final Set<Wire> wires = new ObjectOpenHashSet<>();

    /**
     * Store all wires per chunk to access for e.g. players watching a chunk.
     */
    private final SetMultimap<ChunkPos, Wire> chunkToWires = Multimaps.synchronizedSetMultimap(HashMultimap.create());
    /**
     * Also store reverse map, mainly to make removal from chunkToWires easier.
     */
    private final SetMultimap<Wire, ChunkPos> wiresToChunk = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    /**
     * Maps the wire start and end point to the wire.
     */
    private final SetMultimap<BlockPos, Wire> blockPosToWire = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    private final boolean isClient;

    public Wires(boolean isClient) {
        this.isClient = isClient;
    }

    public Wires(Set<Wire> wires, boolean isClient) {
        this(isClient);

        if(!isClient){
            this.wires.addAll(wires);

            //restore chunkToWires and wiresToChunk and blockPosToWire
            this.wires.forEach(wire -> {
                this.calculateChunkPosForWire(wire).forEach(chunkPos -> {
                    this.chunkToWires.put(chunkPos, wire);
                    this.wiresToChunk.put(wire, chunkPos);
                });

                this.blockPosToWire.put(wire.from(), wire);
                this.blockPosToWire.put(wire.to(), wire);
            });
        }

    }

    /**
     * Gets all wires connected to the given block pos
     */
    public Set<Wire> getWires(BlockPos pos){
        return this.blockPosToWire.get(pos);
    }

    /**
     * Gets all wires in a chunk
     */
    public Set<Wire> getWires(ChunkPos chunk){
        return this.chunkToWires.get(chunk);
    }

    /**
     * Gets all chunks a wire is in
     */
    public Set<ChunkPos> getChunks(Wire wire){
        return this.wiresToChunk.get(wire);
    }

    public static Wires get(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            if (cachedServerLevel.get() == level) {
                if (cachedServerWires.get() != null)
                    return cachedServerWires.get();
            }

            var wires = serverLevel.getDataStorage().computeIfAbsent(
                    new SavedData.Factory<>(() -> new Wires(false), Wires::load, DataFixTypes.LEVEL),
                    Wires.ID
            );

            cachedServerLevel = new WeakReference<>(serverLevel);
            cachedServerWires = new WeakReference<>(wires);

            return wires;
        } else {
            if (cachedClientLevel.get() == level) {
                if (cachedClientWires.get() != null)
                    return cachedClientWires.get();
            }

            //on client data is not stored in the world, so we make an empty
            //it will be filled via the chunk watch events that cause a sync from server
            var wires = new Wires(true);

            cachedClientLevel = new WeakReference<>(level);
            cachedClientWires = new WeakReference<>(wires);

            return wires;
        }
    }

    /**
     * We have to reset save data on unload to handle world changes
     * (mainly on the client, server usually just shuts down and restarts, if it ever changes the main level)
     * -> unloaded overworld means we completely left the game towards the main menu.
     */
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() == cachedServerLevel.get()) {
            cachedServerLevel = new WeakReference<>(null);
            cachedServerWires = new WeakReference<>(null);
        } else if (event.getLevel() == cachedClientLevel.get()) {
            cachedClientLevel = new WeakReference<>(null);
            cachedClientWires = new WeakReference<>(null);
            WireRenderer.get().wires.clear();
        }
    }

    public static Wires load(CompoundTag pCompoundTag) {
        return CODEC.parse(NbtOps.INSTANCE, pCompoundTag.get(NBT_TAG)).result().orElseThrow();
    }

    Stream<ChunkPos> calculateChunkPosForWire(Wire wire) {
        return Arrays.stream(WireSlackHelper.getInterpolatedPoints(wire.from().getCenter(), wire.to().getCenter())).map(pos -> new ChunkPos(
                SectionPos.blockToSectionCoord(pos.x()),
                SectionPos.blockToSectionCoord(pos.z())
        ));
    }

    public void addWire(Wire wire) {
        if (this.isClient) {
            //we just add it to the renderer's set.
            WireRenderer.get().wires.add(wire);
        } else {
            //add to our complete view of wires
            this.wires.add(wire);

            //then add to our per-chunk view
            this.calculateChunkPosForWire(wire).forEach(chunkPos -> {
                this.chunkToWires.put(chunkPos, wire);
                this.wiresToChunk.put(wire, chunkPos);
            });

            this.blockPosToWire.put(wire.from(), wire);
            this.blockPosToWire.put(wire.to(), wire);

            //needs to be called last because it relies on the new state
            WireSync.get().sendAddWireToWatchingPlayers(cachedServerLevel.get(), wire);
        }
        this.setDirty();
    }

    public int removeWiresFor(BlockPos pos){
        var wires = this.getWires(pos);
        var copy = new ArrayList<>(wires); //removeWires modifies the underlying set so we can't iterate on it
        copy.forEach(this::removeWire);
        return copy.size();
    }

    public void removeWire(Wire wire) {
        if (this.isClient) {
            //we just add it to the renderer's set.
            WireRenderer.get().wires.remove(wire);
        } else {
            //needs to be called first because it relies on the old state
            WireSync.get().sendRemoveWireToWatchingPlayers(cachedServerLevel.get(), wire);

            this.wires.remove(wire);
            this.blockPosToWire.remove(wire.from(), wire);
            this.blockPosToWire.remove(wire.to(), wire);

            Set<ChunkPos> chunks = this.wiresToChunk.get(wire);
            for (ChunkPos chunkPos : chunks) {
                this.chunkToWires.remove(chunkPos, wire);
            }
            this.wiresToChunk.removeAll(wire);
        }
        this.setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.put(NBT_TAG, CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElseThrow());
        return pCompoundTag;
    }
}
