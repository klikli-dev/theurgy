package com.klikli_dev.theurgy.logistics;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.Traverser;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.util.TheurgyExtraCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class Logistics extends SavedData {

    public static final Supplier<MutableGraph<BlockPos>> GRAPH_SUPPLIER = () -> GraphBuilder.undirected().allowsSelfLoops(false).build();
    public static final String ID = "theurgy.logistics";
    public static final Codec<Logistics> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TheurgyExtraCodecs.graph(BlockPos.CODEC, GRAPH_SUPPLIER).fieldOf("graph").forGetter(Logistics::graph)
    ).apply(instance, Logistics::new));
    private static final String NBT_TAG = "theurgy:logistics";
    private static Logistics cachedLogistics;
    private final MutableGraph<BlockPos> graph;


    public Logistics() {
        this(GRAPH_SUPPLIER.get());
    }

    public Logistics(MutableGraph<BlockPos> graph) {
        this.graph = graph;
    }

    public static Logistics load(CompoundTag pCompoundTag) {
        return CODEC.parse(NbtOps.INSTANCE, pCompoundTag.get(NBT_TAG)).result().orElseThrow();
    }

    public static Logistics get() {
        if (cachedLogistics == null) {
            var server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                var logistics = server.overworld().getDataStorage().computeIfAbsent(
                        new SavedData.Factory<>(Logistics::new, Logistics::load, DataFixTypes.LEVEL),
                        Logistics.ID
                );

                cachedLogistics = logistics;
            } else {
                var logistics = new Logistics(); //handle client side access gracefully
                Theurgy.LOGGER.warn("Logistics accessed client side, this should not happen!");
                cachedLogistics = logistics;
            }
        }
        return cachedLogistics;
    }

    /**
     * If the overworld is unloaded we are leaving the world, so we have to reset the cached data.
     * It will be reloaded at the first access after a new world is loaded.
     */
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof Level level && level.dimension() == Level.OVERWORLD) {
            cachedLogistics = null;
        }
    }

    public MutableGraph<BlockPos> graph() {
        return this.graph;
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.put(NBT_TAG, CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElseThrow());
        return pCompoundTag;
    }

    public Iterable<BlockPos> getConnected(BlockPos start) {
        var traverser = Traverser.forGraph(this.graph());
        if (this.graph().nodes().contains(start))
            return traverser.breadthFirst(start);

        return List.of();
    }

    public void add(BlockPos node) {
        this.graph().addNode(node);
    }

    public void add(BlockPos a, BlockPos b) {
        this.graph().putEdge(a, b);
    }

    public void remove(BlockPos a, BlockPos b) {
        this.graph().removeEdge(a, b);
    }

    public void remove(BlockPos destroyedBlock) {
        this.graph().removeNode(destroyedBlock);
    }
}
