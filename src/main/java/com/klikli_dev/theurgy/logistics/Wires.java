package com.klikli_dev.theurgy.logistics;

import com.klikli_dev.modonomicon.util.Codecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Wires extends SavedData {

    public static final String ID = "theurgy:wires";
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
     * Maps sections to the connections contained in them.
     * Client only - not needed on server.
     */
    private final Map<SectionPos, List<Wire>> sectionToWireConnectionsView = new Object2ObjectOpenHashMap<>();

    /**
     * Store all wire connections.
     * Server only - not needed on client.
     */
    private final Set<Wire> wires = new ObjectOpenHashSet<>();

    private final boolean isClient;

    public Wires(boolean isClient) {
        this.isClient = isClient;
    }

    public Wires(Set<Wire> wires, boolean isClient) {
        this(isClient);
        this.wires.addAll(wires);
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

    public void addWire(Wire wire) {
        if (this.isClient) {
            //TODO: a wire can cross more than 2 sections.
            //      -> we need to add all sections not just the start and end point

            var from = SectionPos.of(wire.from());
            var to = SectionPos.of(wire.to());

            this.add(from, wire);
            this.add(to, wire);

            //a wire can cross more than the start/end section it is in, so we need to add all sections it crosses
            var points = WireSlackHelper.getInterpolatedPoints(wire.from().getCenter(), wire.to().getCenter());
            for( int i = 0; i < points.length; i++){
                this.add(SectionPos.of(points[i]), wire);
            }

            //finally add it to the renderer
            WireRenderer.get().wires.add(wire);
        } else {
            this.wires.add(wire);
        }
    }

    private void add(SectionPos pos, Wire connection) {
        this.sectionToWireConnectionsView.computeIfAbsent(pos, $ -> new ArrayList<>()).add(connection);
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.put(NBT_TAG, CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElseThrow());
        return pCompoundTag;
    }
}
