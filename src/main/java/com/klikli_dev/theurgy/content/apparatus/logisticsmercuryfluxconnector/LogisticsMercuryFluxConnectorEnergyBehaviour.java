// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsmercuryfluxconnector;

import com.klikli_dev.theurgy.content.behaviour.logistics.InserterNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeMode;
import com.klikli_dev.theurgy.content.capability.SimpleTheurgyEnergyHandler;
import com.klikli_dev.theurgy.logistics.Logistics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LogisticsMercuryFluxConnectorEnergyBehaviour extends InserterNodeBehaviour<EnergyHandler, @Nullable Direction> {
    public static final int DEFAULT_TRANSFER_RATE = 100;
    public static final int TRANSFER_EVERY_N_TICKS = 20;
    public static final int BUFFER_CAPACITY = 1000;

    private final int slowTickRandomOffset = (int) (Math.random() * TRANSFER_EVERY_N_TICKS);
    private boolean enabled = true;
    private Direction directionOverride = null;
    private final EnergyBuffer buffer;

    public LogisticsMercuryFluxConnectorEnergyBehaviour(BlockEntity blockEntity) {
        super(blockEntity, Capabilities.Energy.BLOCK);
        this.buffer = new EnergyBuffer(BUFFER_CAPACITY);
    }

    public EnergyHandler buffer() {
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

        this.targetCapabilities.forEach(c -> this.onCapabilityInvalidated(c.pos(), true));
        this.rebuildTargetCapabilities();
        this.targetCapabilities.forEach(this::notifyTargetCapabilityCacheCreated);
    }

    public Direction directionOverride() {
        return this.directionOverride;
    }

    protected void onEnabledChanged() {
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
        return LeafNodeMode.INSERT;
    }

    @Override
    public @Nullable Direction getTargetContext(BlockPos targetPos) {
        return this.directionOverride != null
                ? this.directionOverride
                : this.blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
    }

    public void tickServer() {
        if (!this.enabled || this.buffer.getAmountAsInt() <= 0) {
            return;
        }

        if ((this.slowTickRandomOffset + this.blockEntity.getLevel().getGameTime()) % TRANSFER_EVERY_N_TICKS != 0) {
            return;
        }

        var network = Logistics.get().getNetwork(this.globalPos());
        if (network == null) {
            return;
        }

        Set<GlobalPos> otherNodes = network.getLeafNodes(this.capabilityType(), this.frequency());
        List<EnergyHandler> sinks = new ArrayList<>();

        for (var other : otherNodes) {
            if (other.equals(this.globalPos())) {
                continue;
            }

            var otherNode = Logistics.get().getLeafNode(other, LeafNodeMode.INSERT, this.capabilityType());
            if (!(otherNode instanceof LogisticsMercuryFluxConnectorEnergyBehaviour otherConnector) || !otherConnector.enabled()) {
                continue;
            }

            var otherTargetCaps = otherConnector.availableTargetCapabilities();
            if (otherTargetCaps.isEmpty()) {
                continue;
            }

            var sinkCap = otherTargetCaps.getFirst().getCapability();
            if (sinkCap != null && this.canAcceptEnergy(sinkCap)) {
                sinks.add(sinkCap);
            }
        }

        if (sinks.isEmpty()) {
            return;
        }

        int totalToPush = Math.min(this.buffer.getAmountAsInt(), DEFAULT_TRANSFER_RATE * sinks.size());
        int perTarget = totalToPush / sinks.size();
        int distributedRemainder = totalToPush % sinks.size();

        for (int i = 0; i < sinks.size(); i++) {
            int amount = perTarget + (i < distributedRemainder ? 1 : 0);
            if (amount <= 0) {
                continue;
            }

            try (Transaction tx = Transaction.openRoot()) {
                int extracted = this.buffer.extract(amount, tx);
                if (extracted > 0) {
                    int inserted = sinks.get(i).insert(extracted, tx);
                    int returnedAmount = extracted - inserted;
                    if (returnedAmount > 0) {
                        this.buffer.insert(returnedAmount, tx);
                    }
                    tx.commit();
                }
            }
        }
    }

    private boolean canAcceptEnergy(EnergyHandler sinkCap) {
        try (Transaction tx = Transaction.openRoot()) {
            return sinkCap.insert(1, tx) > 0;
        }
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput bufferOutput = output.child("energyBuffer");
        this.buffer.serialize(bufferOutput);
        if (bufferOutput.isEmpty()) {
            output.discard("energyBuffer");
        }
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.child("energyBuffer").ifPresent(this.buffer::deserialize);
    }

    private static class EnergyBuffer extends SimpleTheurgyEnergyHandler {
        private EnergyBuffer(int capacity) {
            super(capacity, BUFFER_CAPACITY, DEFAULT_TRANSFER_RATE);
        }
    }
}
