package com.klikli_dev.theurgy.logistics;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.Traverser;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.behaviour.logistics.HasLeafNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.InserterNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeMode;
import com.klikli_dev.theurgy.util.TheurgyExtraCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.lang.ref.WeakReference;
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
    protected Map<GlobalPos, WeakReference<LeafNodeBehaviour<?, ?>>> cachedLeafNodes = new Object2ObjectOpenHashMap<>();
    protected boolean useLeafNodeCache = false;

    public Logistics() {
        this(GRAPH_SUPPLIER.get());
    }

    public Logistics(MutableGraph<GlobalPos> graph) {
        this.graph = graph;
    }

    public static Logistics load(CompoundTag pCompoundTag) {
        return CODEC.parse(NbtOps.INSTANCE, pCompoundTag.get(NBT_TAG)).result().orElseThrow();
    }

    public static MinecraftServer server() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    public static Logistics get() {
        if (cachedLogistics == null) {
            var server = server();

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

    public void enableLeafNodeCache() {
        this.useLeafNodeCache = true;
    }

    public void disableLeafNodeCache() {
        this.useLeafNodeCache = false;
        this.cachedLeafNodes.clear();
    }

    /**
     * Gets the leaf node at the given position if it is in the desired mode ( or null ).
     * Call enableLeafNodeCache() before calling this to enable caching if you plan to access a lot of nodes in short periods of time.
     */
    public LeafNodeBehaviour<?, ?> getLeafNode(GlobalPos pos, LeafNodeMode mode) {
        var node = this.getLeafNode(pos);
        if (node != null && node.mode() == mode) {
            return node;
        }
        return null;
    }


    /**
     * Gets the leaf node at the given position ( or null ).
     * Call enableLeafNodeCache() before calling this to enable caching if you plan to access a lot of nodes in short periods of time.
     */
    public LeafNodeBehaviour<?, ?> getLeafNode(GlobalPos pos) {

        //first check leaf node cache
        LeafNodeBehaviour<?, ?> result = null;
        if (this.useLeafNodeCache) {
            var weakRef = this.cachedLeafNodes.get(pos);
            if (weakRef != null){
                result = weakRef.get();
                if(result == null){ //clean up cache if needed.
                    this.cachedLeafNodes.remove(pos);
                }
            }
        }

        //if not in cache, query world and store.
        if (result == null) {
            var level = server().getLevel(pos.dimension());
            if (level == null) {
                return null;
            }

            var blockEntity = level.getBlockEntity(pos.pos());
            if (blockEntity instanceof HasLeafNodeBehaviour<?, ?> hasLeafNode) {
                result = hasLeafNode.leafNode();
            }

            if (result != null && this.useLeafNodeCache) {
                this.cachedLeafNodes.put(pos, new WeakReference<>(result));
            }
        }

        return result;
    }

    public <T, C> void onCapabilityInvalidated(GlobalPos pos, InserterNodeBehaviour<T, C> leafNode) {
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

    public void removeLeafNode(LeafNodeBehaviour<?, ?> leafNode) {
        //TODO: Implement

        //TODO: if a leaf node is unloaded we remove it as a leaf node, but leaf the "normal" node behind.
        //      this way it stays known to the network and can re-add itself when loaded
        //      otherwise we would have the problem that a leaf node would have to know which network it is in to register correctly
    }

    /**
     * Adds a leaf node to the graph.
     *
     * @param leafNode    the leaf node
     * @param connectedTo the position of the node this leaf node is connected to.
     *                    If the node is not connected to any other node yet then this should not be called yet.
     */
    public void addLeafNode(LeafNodeBehaviour<?, ?> leafNode, GlobalPos connectedTo) {
        //TODO: we should not need a connectedTo
        //      in most cases the node is first added solo, and then a connection added later

        //TODO: a network merge should cause logistics networks to rebuild cache.
        //      we can just flush cache and rebuild for all nodes
        //      we can slightly improve performance by just calling the "add" functionality on the respective other half of the network
        //      but that is probably not worth it .. we can just leave a comment behind as a potential future improvement
        //      rebuildCaches only on the new merged network -> contains all nodes anyway


        //TODO: can we somehow handle offline modifications? eg worldedit
        //      check if node exists on chunk load and kick out all GlobalPos that no longer represent a node block
        //      that does not handle if a DIFFERENT node was placed which may cause issues with our capability key system
        //      -> or we can just require admins that do that to do it live? :)
        //      the kicking of non-nodes might make sense though
        var pos = leafNode.globalPos();
        var network = this.add(pos, connectedTo);
        this.blockPosToNetwork.put(pos, network);
        network.addLeafNode(leafNode);
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

        //TODO: logistics networks also need to re-notify all their nodes to update the cache.
        //      as long as both new networks reset and rebuild fully that is probably pretty easy.
        //      rebuildCaches on both!
        this.graph().removeEdge(a, b);
    }

    public void remove(GlobalPos destroyedBlock) {
        this.graph().removeNode(destroyedBlock);
        //TODO: update the network, detect network splits

        //TODO: logistics networks also need to re-notify all their nodes to update the cache.
        //      as long as both new networks reset and rebuild fully that is probably pretty easy.
        //      rebuildCaches on both!
    }
}
