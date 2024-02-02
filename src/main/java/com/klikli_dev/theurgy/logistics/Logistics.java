package com.klikli_dev.theurgy.logistics;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.Traverser;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.util.TheurgyExtraCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class Logistics extends SavedData {

    public static final Supplier<MutableGraph<GlobalPos>> GRAPH_SUPPLIER = () -> GraphBuilder.undirected().allowsSelfLoops(false).build();
    public static final String ID = "theurgy.logistics";
    public static final Codec<Logistics> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TheurgyExtraCodecs.graph(GlobalPos.CODEC, GRAPH_SUPPLIER).fieldOf("graph").forGetter(Logistics::graph)
    ).apply(instance, Logistics::new));
    private static final String NBT_TAG = "theurgy:logistics";
    private static Logistics cachedLogistics;
    private final MutableGraph<GlobalPos> graph;
    private final Map<GlobalPos, LogisticsNetwork> blockPosToNetwork = new Object2ObjectOpenHashMap<>();


    public Logistics() {
        this(GRAPH_SUPPLIER.get());
    }

    public Logistics(MutableGraph<GlobalPos> graph) {
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

    public <T, C> void onCapabilityInvalidated(GlobalPos pos, LogisticsInserterNode<T, C> leafNode) {
        //TODO: notify the extractor nodes and call onTargetRemovedFromGraph
    }

    public MutableGraph<GlobalPos> graph() {
        return this.graph;
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.put(NBT_TAG, CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElseThrow());
        return pCompoundTag;
    }

    public Iterable<GlobalPos> getConnected(GlobalPos start) {
        var traverser = Traverser.forGraph(this.graph());
        if (this.graph().nodes().contains(start))
            return traverser.breadthFirst(start);

        return List.of();
    }

    public void add(GlobalPos node) {
        this.graph().addNode(node);
    }

    /**
     * Adds a leaf node to the graph.
     * @param node the position of the leaf node
     * @param leafNode the leaf node
     * @param connectedTo the position of the node this leaf node is connected to.
     *                    If the node is not connected to any other node yet then this should not be called yet.
     */
    public void addLeafNode(GlobalPos node, LogisticsLeafNode<?, ?> leafNode, GlobalPos connectedTo) {
        var network = this.add(node, connectedTo);
        this.blockPosToNetwork.put(node, network);
        network.addLeafNode(node, leafNode);
    }

    public LogisticsNetwork add(GlobalPos a, GlobalPos b) {
        this.graph().putEdge(a, b);

        LogisticsNetwork network;
        var netA = this.blockPosToNetwork.get(a);
        var netB = this.blockPosToNetwork.get(b);
        if (netA == null && netB == null) {
            //create network
            network = new LogisticsNetwork();
        } else if (netA == null) {
            //add to network B
            network = netB;
        } else if (netB == null) {
            //add to network A
            network = netA;
        } else if (netA != netB) {
            //merge networks
            network = this.merge(netA, netB);
        } else {
            //already in the same network .. so we just choose A
            network = netA;
        }

        network.addNode(a);
        network.addNode(b);

        this.blockPosToNetwork.put(a, network);
        this.blockPosToNetwork.put(b, network);

        return network;
    }

    private LogisticsNetwork merge(LogisticsNetwork a, LogisticsNetwork b) {
        //create new network that combines the old two
        var result = new LogisticsNetwork();
        result.merge(a);
        result.merge(b);

        //add new network to mapping
        result.nodes().forEach(pos -> this.blockPosToNetwork.put(pos, result));

        //remove old networks from mapping
        a.nodes().forEach(pos -> this.blockPosToNetwork.remove(pos, result));
        b.nodes().forEach(pos -> this.blockPosToNetwork.remove(pos, result));

        return result;
    }

    public void remove(GlobalPos a, GlobalPos b) {
        //TODO: detect network splits
        //      here we must rely on the graph and just rebuild both fully
        //TODO: might be safe to do async? But game could further modify state ... so probably not
        this.graph().removeEdge(a, b);
    }

    public void remove(GlobalPos destroyedBlock) {
        this.graph().removeNode(destroyedBlock);
        //TODO: update the network, detect network splits
    }
}
