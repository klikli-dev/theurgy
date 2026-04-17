// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsmercuryfluxconnector;

import com.klikli_dev.theurgy.content.behaviour.logistics.InserterNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeMode;
import com.klikli_dev.theurgy.content.capability.SimpleMercuryFluxHandler;
import com.klikli_dev.theurgy.content.capability.MercuryFluxHandler;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/**
 * A leaf node behaviour that acts as a mercury flux conduit in the logistics network.
 * <p>
 * Unlike item/fluid inserters and extractors, there is no inserter/extractor split.
 * The connector has its own internal buffer that source blocks (e.g., Mercury Catalyst) push
 * flux into. On tick, the connector forwards buffer contents to other connectors' attached
 * blocks (sinks) through the logistics network. This mirrors how NeoForge energy (FE) works:
 * the source block decides to push; the cable (connector) just forwards.
 * <p>
 * Flow: Source → Connector A buffer → (logistics network) → Connector B's attached block (sink)
 */
public class LogisticsMercuryFluxConnectorBehaviour extends InserterNodeBehaviour<MercuryFluxHandler, @Nullable Direction> {

    public static final int DEFAULT_TRANSFER_RATE = 100;
    public static final int TRANSFER_EVERY_N_TICKS = 20; // 1 second
    public static final int BUFFER_CAPACITY = 1000;

    private final int slowTickRandomOffset = (int) (Math.random() * TRANSFER_EVERY_N_TICKS);
    private boolean enabled = true;
    private Direction directionOverride = null;
    private final com.klikli_dev.theurgy.content.capability.SimpleMercuryFluxHandler buffer;

    public LogisticsMercuryFluxConnectorBehaviour(BlockEntity blockEntity) {
        super(blockEntity, CapabilityRegistry.MERCURY_FLUX_HANDLER);
        // High maxInsert so source blocks can fill the buffer quickly
        this.buffer = new com.klikli_dev.theurgy.content.capability.SimpleMercuryFluxHandler(BUFFER_CAPACITY, BUFFER_CAPACITY, DEFAULT_TRANSFER_RATE);
    }

    /**
     * Returns the connector's internal flux buffer.
     * This is exposed via capability registration so source blocks can push into it.
     */
    public MercuryFluxHandler buffer() {
        return this.buffer;
    }

    @Override
    public boolean enabled() {
        return this.enabled;
    }

    @Override
    public void enabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }

        this.enabled = enabled;
        this.onEnabledChanged();
    }

    public void directionOverride(Direction directionOverride) {
        this.directionOverride = directionOverride;

        //first notify the network to remove the old target capabilities
        this.targetCapabilities.forEach(c -> this.onCapabilityInvalidated(c.pos(), true));

        //then build the new capabilities
        this.rebuildTargetCapabilities();

        //then notify the network to add the new target capabilities
        this.targetCapabilities.forEach(this::notifyTargetCapabilityCacheCreated);
    }

    public Direction directionOverride() {
        return this.directionOverride;
    }

    protected void onEnabledChanged() {
        //a disabled logistics node is equivalent to one not existing
        if (this.enabled) {
            this.rebuildTargetCapabilities();
            Logistics.get().add(this);
        } else {
            this.targetCapabilities.forEach(c -> this.onCapabilityInvalidated(c.pos(), true));
            this.deactivateTargetCapabilities();
            Logistics.get().remove(this, false);
        }
    }

    @Override
    public LeafNodeMode mode() {
        // INSERT mode so that other connectors in the network can find us as targets
        return LeafNodeMode.INSERT;
    }

    @Override
    public @Nullable Direction getTargetContext(BlockPos targetPos) {
        // The target is the block the connector is attached to.
        // FACING points toward the attached block, so the context is FACING
        // (accessing the target from the direction the connector faces).
        return this.directionOverride != null ? this.directionOverride :
                this.blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
    }

    /**
     * Called every server tick to forward flux from this connector's buffer
     * to other connectors' attached blocks (sinks) in the logistics network.
     * <p>
     * This follows the conduit pattern: source blocks push into the connector's buffer,
     * and the connector forwards the buffer contents to remote sinks via the network.
     * <p>
     * Note: This currently iterates over all leaf nodes in the network, resulting in O(N²)
     * complexity for N connectors. This is acceptable for typical use cases (small networks),
     * but could be optimized in the future with a network-level controller or sink caching.
     */
    public void tickServer() {
        if (!this.enabled) return;
        if (this.buffer.getAmountAsInt() <= 0) return;

        // Slow tick to avoid processing every tick
        if ((this.slowTickRandomOffset + this.blockEntity.getLevel().getGameTime()) % TRANSFER_EVERY_N_TICKS != 0)
            return;

        // Collect all valid sink targets from other connectors in the same network
        var network = Logistics.get().getNetwork(this.globalPos());
        if (network == null) return;

        Set<GlobalPos> otherNodes = network.getLeafNodes(this.capabilityType(), this.frequency());
        List<MercuryFluxHandler> sinks = new ArrayList<>();

        for (var other : otherNodes) {
            if (other.equals(this.globalPos())) continue;

            var otherNode = Logistics.get().getLeafNode(other, LeafNodeMode.INSERT, this.capabilityType());
            if (otherNode == null || !(otherNode instanceof LogisticsMercuryFluxConnectorBehaviour otherConnector))
                continue;

            if (!otherConnector.enabled()) continue;

            // Get the MercuryFluxHandler of the block the other connector is attached to (the sink)
            var otherTargetCaps = otherConnector.availableTargetCapabilities();
            if (otherTargetCaps.isEmpty()) continue;

            var sinkCap = otherTargetCaps.getFirst().getCapability();
            if (sinkCap != null && sinkCap.getAmountAsLong() < sinkCap.getCapacityAsLong()) {
                sinks.add(sinkCap);
            }
        }

        if (sinks.isEmpty()) return;

        // Distribute buffer contents evenly among all sinks (same pattern as MercuryCapacitor)
        int totalToPush = Math.min(this.buffer.getAmountAsInt(), DEFAULT_TRANSFER_RATE * sinks.size());
        if (totalToPush <= 0) return;

        int perTarget = totalToPush / sinks.size();
        int remainder = totalToPush % sinks.size();

        for (int i = 0; i < sinks.size(); i++) {
            int amount = perTarget + (i < remainder ? 1 : 0);
            if (amount <= 0) continue;

            try (Transaction tx = Transaction.openRoot()) {
                int received = sinks.get(i).insert(amount, tx);
                if (received > 0) {
                    this.buffer.extract(received, tx);
                    tx.commit();
                }
            }
        }
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ValueOutput bufferOutput = output.child("buffer");
        this.buffer.serialize(bufferOutput);
        if (bufferOutput.isEmpty()) {
            output.discard("buffer");
        }
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.child("buffer").ifPresent(this.buffer::deserialize);
    }

    @Override
    public void writeNetwork(ValueOutput output) {
        super.writeNetwork(output);
        output.putBoolean("enabled", this.enabled);
        if (this.directionOverride != null)
            output.putInt("directionOverride", this.directionOverride.get3DDataValue());
    }

    @Override
    public void readNetwork(ValueInput input) {
        super.readNetwork(input);
        this.enabled = input.getBooleanOr("enabled", this.enabled);
        input.getInt("directionOverride").ifPresent(direction -> this.directionOverride = Direction.from3DDataValue(direction));
    }
}
