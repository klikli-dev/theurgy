// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.logistics;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.Traverser;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.behaviour.logistics.*;
import com.klikli_dev.theurgy.util.TheurgyExtraCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
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
    private final Set<GlobalPos> graphNodes = new ObjectOpenHashSet<>();
    private final Map<GlobalPos, LogisticsNetwork> blockPosToNetwork = new Object2ObjectOpenHashMap<>();
    private final Map<GlobalPos, WeakReference<LeafNodeBehaviour<?, ?>>> cachedLeafNodes = new Object2ObjectOpenHashMap<>();
    /**
     * If true, leaf node lookups will be cached. This is useful if you access a lot of nodes in a short period of time.
     */
    private boolean useLeafNodeCache = false;

    /**
     * If true, the network's leaf node caches will be rebuilt automatically after a network change.
     * This should be true in most cases, but can be disabled if you want to handle it manually, e.g. for bulk operations.
     */
    private boolean useAutomaticNetworkCacheRebuild = true;

    //TODO: can we somehow handle offline modifications? eg worldedit
    //      check if node exists on chunk load and kick out all GlobalPos that no longer represent a node block
    //      that does not handle if a DIFFERENT node was placed which may cause issues with our capability key system
    //      -> or we can just require admins that do that to do it live? :)
    //      the kicking of non-nodes might make sense though

    public Logistics() {
        this(GRAPH_SUPPLIER.get());
    }

    public Logistics(MutableGraph<GlobalPos> graph) {
        this.graph = graph;
        this.rebuildGraph();
    }

    public static Logistics load(CompoundTag pCompoundTag, HolderLookup.Provider pRegistries) {
        return CODEC.parse(pRegistries.createSerializationContext(NbtOps.INSTANCE), pCompoundTag.get(NBT_TAG)).result().orElseThrow();
    }

    private static MinecraftServer server() {
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

    public void disableAutomaticNetworkCacheRebuild() {
        this.useAutomaticNetworkCacheRebuild = false;
    }

    public void enableAutomaticNetworkCacheRebuild() {
        this.useAutomaticNetworkCacheRebuild = true;
    }

    public LogisticsNetwork getNetwork(GlobalPos pos) {
        return this.blockPosToNetwork.get(pos);
    }

    /**
     * Gets the leaf node at the given position if it is in the desired mode ( or null ).
     * Call enableLeafNodeCache() before calling this to enable caching if you plan to access a lot of nodes in short periods of time.
     */
    public LeafNodeBehaviour<?, ?> getLeafNode(GlobalPos pos, LeafNodeMode mode) {
        return this.getLeafNode(pos, mode, (BlockCapability<?, ?>) null);
    }


    /**
     * Gets the leaf node at the given position ( or null ).
     * Call enableLeafNodeCache() before calling this to enable caching if you plan to access a lot of nodes in short periods of time.
     */
    public LeafNodeBehaviour<?, ?> getLeafNode(GlobalPos pos) {
        return this.getLeafNode(pos, (BlockCapability<?, ?>) null);
    }

    /**
     * Gets the leaf node at the given position if it is in the desired mode ( or null ).
     * Call enableLeafNodeCache() before calling this to enable caching if you plan to access a lot of nodes in short periods of time.
     */
    public <T, C> LeafNodeBehaviour<T, C> getLeafNode(GlobalPos pos, LeafNodeMode mode, BlockCapability<T, C> capability) {
        var node = this.getLeafNode(pos, capability);
        if (node != null && node.mode() == mode) {
            return node;
        }
        return null;
    }


    public <T, C> LeafNodeBehaviour<T, C> getLeafNode(GlobalPos pos, BlockCapability<T, C> capability) {
        //first check leaf node cache
        LeafNodeBehaviour<T, C> result = null;
        if (this.useLeafNodeCache) {
            var weakRef = this.cachedLeafNodes.get(pos);
            if (weakRef != null) {
                var temp = weakRef.get();
                if (temp != null && (capability == null || temp.capabilityType().equals(capability))) {
                    //noinspection unchecked -> we know it is the right type because we check!
                    result = (LeafNodeBehaviour<T, C>) temp;
                }
                if (result == null) { //clean up cache if needed.
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
            if (blockEntity instanceof HasLeafNodeBehaviour<?, ?> hasLeafNode && (capability == null || hasLeafNode.leafNode().capabilityType().equals(capability))) {
                //noinspection unchecked -> we know it is the right type because we check!
                result = (LeafNodeBehaviour<T, C>) hasLeafNode.leafNode();
            }

            if (result != null && this.useLeafNodeCache) {
                this.cachedLeafNodes.put(pos, new WeakReference<>(result));
            }
        }

        return result;
    }

    /**
     * Returns true if the given position is a logistics node.
     */
    public boolean isLogisticsNode(GlobalPos pos) {
        var level = server().getLevel(pos.dimension());
        if (level == null) {
            return false;
        }

        var blockEntity = level.getBlockEntity(pos.pos());
        return blockEntity instanceof LogisticsNode;
    }

    /**
     * Adds a leaf node to the graph.
     * Will call onLoadInsertNode or onLoadExtractNode methods on the network for you.
     * Will also create a network if none exists yet, as leaf nodes need a network to be in.
     *
     * @param leafNode the leaf node
     */
    public LogisticsNetwork add(LeafNodeBehaviour<?, ?> leafNode) {
        var pos = leafNode.globalPos();
        this.add(pos);
        var network = this.blockPosToNetwork.get(pos);
        if (network != null) {
            network.addLeafNode(leafNode);
        }
        return network;
    }


    /**
     * Removes a leaf node from the graph.
     *
     * @param leafNode    the leaf node
     * @param permanently true if the block was destroyed, false if it was just unloaded.
     */
    public void remove(LeafNodeBehaviour<?, ?> leafNode, boolean permanently) {
        //if a leaf node is unloaded we remove it as a leaf node, but leave the "normal" node behind.
        //this way it stays known to the network and can re-add itself when loaded
        //otherwise we would have the problem that a leaf node would have to know which network it is in to register correctly
        //further, a leaf node can theoretically also connect other nodes, so unload should not destroy the network!
        //only if it was destroyed = permanently removed we remove the normal node too

        var network = this.blockPosToNetwork.get(leafNode.globalPos());
        if (network != null) {
            network.removeLeafNode(leafNode);
        }

        if (permanently) {
            this.remove(leafNode.globalPos());
        }
    }

    /**
     * Adds a single node to the graph.
     * Needs to be called both for internal nodes and leaf nodes.
     * The latter will automatically call it from its respective add() method.
     * Will re-create existing networks if the node was already present on a previous load.
     */
    public LogisticsNetwork add(GlobalPos node) {
        var isNew = this.graphNodes().add(node);
        if (isNew) {
            //if new we just add it, there will be no network
            this.graph().addNode(node);
            this.setDirty();
            return null;
        } else {
            //if not new it was previously added and may have been connected before, so we query the graph.
            //this causes network re-creation after a world load.
            var neighbors = this.graph().adjacentNodes(node);
            for (GlobalPos neighbor : neighbors) {
                this.add(node, neighbor);
            }
        }
        return this.blockPosToNetwork.get(node);
    }

    /**
     * Remove the given node. Naturally also removes the connections to it.
     * If this was the last connection between two or more parts of a network it will be split.
     */
    public void remove(GlobalPos destroyedBlock) {
        //This is a bit trickier than just removing an edge, because it can theoretically create multiple networks.
        if(!this.graphNodes().contains(destroyedBlock)){
            return;
        }

        //first query the neighbors, because after removal we can't
        var neighbors = this.graph().adjacentNodes(destroyedBlock);
        this.graph().removeNode(destroyedBlock);
        this.graphNodes().remove(destroyedBlock);
        this.setDirty();

        var oldNetwork = this.blockPosToNetwork.get(destroyedBlock);
        if (oldNetwork != null) {
            oldNetwork.removeNode(destroyedBlock);
            this.blockPosToNetwork.remove(destroyedBlock);
        }

        //now we need to detect the splits, and avoid unecessary iterations.
        List<LogisticsNetwork> splitNetworks = new ArrayList<>();

        for (GlobalPos neighbor : neighbors) {
            boolean isNeighborInSplitNetwork = splitNetworks.stream()
                    .anyMatch(network -> network.nodes().contains(neighbor));

            if (!isNeighborInSplitNetwork) {
                var newNetwork = this.buildNetwork(neighbor, this.getConnected(neighbor), (node) -> {
                });
                splitNetworks.add(newNetwork);
            }
        }

        // Logistics networks also need to re-notify all their nodes to update the cache.
        // As long as all new networks reset and rebuild fully that is probably pretty easy.
        // Rebuild caches on all!
        if (this.useAutomaticNetworkCacheRebuild) {
            splitNetworks.forEach(LogisticsNetwork::rebuildCaches);
        }
    }

    /**
     * Adds an edge/connection to the network.
     * This will add the nodes if they are not present yet.
     * Will create a network if none exists yet and handles merging.
     */
    public LogisticsNetwork add(GlobalPos a, GlobalPos b) {
        this.graph().putEdge(a, b);
        this.graphNodes().add(a);
        this.graphNodes().add(b);
        this.setDirty();

        LogisticsNetwork network;
        var netA = this.blockPosToNetwork.get(a);
        var netB = this.blockPosToNetwork.get(b);
        if (netA == null && netB == null) {
            //create network
            network = new LogisticsNetwork();
        } else if (netA == null) {
            //add to network B
            //this one and the next if elegantly avoid a merge if a single node is connected to a network
            //a single node does not create a network.
            network = netB;
        } else if (netB == null) {
            //add to network A
            network = netA;
        } else if (netA != netB) {
            //merge networks
            if (netA.nodes().size() == 1) //in case netB is also 1 that is fine.
                network = this.mergeSingle(netB, netA);
            else if (netB.nodes().size() == 1)
                network = this.mergeSingle(netA, netB);
            else
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

    /**
     * Remove the connection between two nodes.
     * If this was the last connection between two parts of a network it will be split into two.
     * This does NOT remove the nodes.
     */
    public void remove(GlobalPos a, GlobalPos b) {
        this.graph().removeEdge(a, b);
        this.setDirty();

        var connectedA = new ObjectOpenHashSet<GlobalPos>();
        this.getConnected(a).forEach(connectedA::add);
        if (connectedA.contains(b)) {
            //no split
            return;
        }

        //split detected

        var networkA = this.buildNetwork(a, connectedA, (node) -> {
        });
        var networkB = this.buildNetwork(b, this.getConnected(b), (node) -> {
        }); //no need to build a set as we only iterate once

        //now rebuild the leaf node caches
        if (this.useAutomaticNetworkCacheRebuild) {
            networkA.rebuildCaches();
            networkB.rebuildCaches();
        }
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag, HolderLookup.Provider pRegistries) {
        pCompoundTag.put(NBT_TAG, CODEC.encodeStart(pRegistries.createSerializationContext(NbtOps.INSTANCE), this).result().orElseThrow());
        return pCompoundTag;
    }

    private MutableGraph<GlobalPos> graph() {
        return this.graph;
    }

    private Set<GlobalPos> graphNodes() {
        return this.graphNodes;
    }

    private Iterable<GlobalPos> getConnected(GlobalPos start) {
        var traverser = Traverser.forGraph(this.graph());
        if (this.graphNodes().contains(start))
            return traverser.breadthFirst(start);

        return List.of();
    }

    /**
     * Rebuilds the entire graph, re-creating networks.
     * It does not rebuild the leaf node caches.
     */
    private void rebuildGraph() {
        this.graphNodes.clear();
        this.blockPosToNetwork.clear();
        for (var node : this.graph().nodes()) {
            //skip nodes we already handled
            if (this.graphNodes.contains(node)) {
                continue;
            }

            //now get all nodes connected to this one and create a network for them
            var connected = this.getConnected(node);

            this.buildNetwork(node, connected, this.graphNodes::add);
            //last param: we add both the root node and the connected nodes to graphNodes to ensure we don't do unnecessary double-checks
        }
    }

    private LogisticsNetwork merge(LogisticsNetwork a, LogisticsNetwork b) {
        //create new network that combines the old two
        var result = new LogisticsNetwork();
        result.merge(a);
        result.merge(b);

        //add new network to mapping
        //this also replaces the old network as each node is overwritten
        result.nodes().forEach(pos -> this.blockPosToNetwork.put(pos, result));

        if (this.useAutomaticNetworkCacheRebuild) {
            result.rebuildCaches(); //just rebuild cache on the new network as that will reset all nodes anyway
        }

        return result;
    }

    /**
     * Merges a network with only one node into a larger network.
     * This means it will never perform a cache rebuild.
     * Instead, in the case of a leaf being the added one it being added to the network will fire the necessary event.
     */
    private LogisticsNetwork mergeSingle(LogisticsNetwork network, LogisticsNetwork singleNodeNetwork) {
        network.merge(singleNodeNetwork);
        return network;
    }

    /**
     * Builds a logistics network from a root node and it's connected nodes.
     *
     * @param rootNode    the root node. This has no special meaning, it is just the first one we query.
     * @param connected   the connected nodes,
     * @param onNodeAdded a callback that is called for each node that is added to the network.
     *                    This can be used to ensure nodes are not handled twice, e.g. in a full graph rebuild.
     */
    private LogisticsNetwork buildNetwork(GlobalPos rootNode, Iterable<GlobalPos> connected, Consumer<GlobalPos> onNodeAdded) {
        var network = new LogisticsNetwork();

        //add the root node
        network.addNode(rootNode);
        this.blockPosToNetwork.put(rootNode, network);
        onNodeAdded.accept(rootNode);

        //now add all other nodes
        connected.forEach(c -> {
            network.addNode(c);
            this.blockPosToNetwork.put(c, network);
            onNodeAdded.accept(c);
        });

        return network;
    }

}
