package com.klikli_dev.theurgy.content.logistics;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.Traverser;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class Logistics {
    private static final Logistics instance = new Logistics();
    private final MutableGraph<BlockPos> graph;
    private final Traverser<BlockPos> traverser;
    public LogisticsSavedData savedData;

    public Logistics() {
        this.graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
        this.traverser = Traverser.forGraph(this.graph);
    }

    public static Logistics get() {
        return instance;
    }

    public Iterable<BlockPos> getConnected(BlockPos start) {
        if(this.graph.nodes().contains(start))
            return this.traverser.breadthFirst(start);

        return List.of();
    }

    public void add(BlockPos a, BlockPos b) {
        this.graph.putEdge(a, b);
    }

    public void remove(BlockPos a, BlockPos b) {
        this.graph.removeEdge(a, b);
    }

    public void remove(BlockPos destroyedBlock) {
        this.graph.removeNode(destroyedBlock);
    }

    /**
     * Gets the current server instance.
     * Uses ServerLifecycleHooks.getCurrentServer(), which should be safe in this context for both client and server.
     */
    public MinecraftServer server() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    private LogisticsSavedData savedData() {
        if (this.savedData == null) {
            this.savedData = this.server().overworld().getDataStorage().computeIfAbsent(
                    new SavedData.Factory<>(LogisticsSavedData::new, LogisticsSavedData::load, DataFixTypes.LEVEL),
                    LogisticsSavedData.ID
            );
        }

        return this.savedData;
    }

    /**
     * We have to reset save data on unload to handle world changes
     * (mainly on the client, server usually just shuts down and restarts, if it ever changes the main level)
     * -> unloaded overworld means we completely left the game towards the main menu.
     */
    public void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof Level level && level.dimension() == Level.OVERWORLD) {
            this.savedData = null;
        }
    }
}
