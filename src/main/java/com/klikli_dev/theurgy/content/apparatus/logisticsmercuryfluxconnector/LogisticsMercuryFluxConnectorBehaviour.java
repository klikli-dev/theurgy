// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsmercuryfluxconnector;

import com.klikli_dev.theurgy.content.behaviour.logistics.InserterNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeMode;
import com.klikli_dev.theurgy.content.capability.MercuryFluxStorage;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * A leaf node behaviour that exposes a mercury flux handler (from the attached block) to the logistics network,
 * and balances flux between connected blocks on tick.
 * <p>
 * Unlike item/fluid inserters and extractors, there is no inserter/extractor split — the block decides whether
 * it pushes or pulls based on relative energy levels. This mirrors how NeoForge energy (FE) works: two blocks
 * are connected by attaching a connector to both, and the source pushes if it has more energy than the target.
 */
public class LogisticsMercuryFluxConnectorBehaviour extends InserterNodeBehaviour<MercuryFluxStorage, @Nullable Direction> {

    public static final int DEFAULT_TRANSFER_RATE = 100;
    public static final int TRANSFER_EVERY_N_TICKS = 20; // 1 second

    private final int slowTickRandomOffset = (int) (Math.random() * TRANSFER_EVERY_N_TICKS);
    private boolean enabled = true;
    private Direction directionOverride = null;

    public LogisticsMercuryFluxConnectorBehaviour(BlockEntity blockEntity) {
        super(blockEntity, CapabilityRegistry.MERCURY_FLUX_HANDLER);
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
        // The target is the block the connector is attached to (opposite to FACING)
        return this.directionOverride != null ? this.directionOverride :
                this.blockEntity.getBlockState().getValue(BlockStateProperties.FACING).getOpposite();
    }

    /**
     * Called every server tick to balance flux between this connector's attached block and other
     * mercury flux handlers in the same network.
     */
    public void tickServer() {
        if (!this.enabled) return;

        // Slow tick to avoid processing every tick
        if ((this.slowTickRandomOffset + this.blockEntity.getLevel().getGameTime()) % TRANSFER_EVERY_N_TICKS != 0)
            return;

        // Get the flux handler of the block we're attached to
        var targetCaps = this.availableTargetCapabilities();
        if (targetCaps.isEmpty()) return;

        // Get the first (and usually only) target capability - the block we're attached to
        var localCap = targetCaps.getFirst().getCapability();
        if (localCap == null) return;

        // Find all other mercury flux connectors in the same network and frequency
        var network = Logistics.get().getNetwork(this.globalPos());
        if (network == null) return;

        Set<GlobalPos> otherNodes = network.getLeafNodes(this.capabilityType(), this.frequency());
        if (otherNodes.isEmpty()) return;

        for (var other : otherNodes) {
            if (other.equals(this.globalPos())) continue;

            var otherNode = Logistics.get().getLeafNode(other, LeafNodeMode.INSERT, this.capabilityType());
            if (otherNode == null || !(otherNode instanceof LogisticsMercuryFluxConnectorBehaviour otherConnector))
                continue;

            var otherTargetCaps = otherConnector.availableTargetCapabilities();
            if (otherTargetCaps.isEmpty()) continue;

            var otherCap = otherTargetCaps.getFirst().getCapability();
            if (otherCap == null) continue;

            this.balanceFlux(localCap, otherCap);
        }
    }

    /**
     * Balances mercury flux between two handlers by transferring from the one with more
     * to the one with less, up to DEFAULT_TRANSFER_RATE.
     */
    protected void balanceFlux(MercuryFluxStorage a, MercuryFluxStorage b) {
        int storedA = a.getEnergyStored();
        int storedB = b.getEnergyStored();

        if (storedA == storedB) return;

        MercuryFluxStorage source;
        MercuryFluxStorage target;

        if (storedA > storedB) {
            source = a;
            target = b;
        } else {
            source = b;
            target = a;
        }

        // Transfer from source to target
        int toTransfer = Math.min(DEFAULT_TRANSFER_RATE, source.getEnergyStored());
        if (toTransfer <= 0) return;

        int accepted = target.receiveEnergy(toTransfer, true);
        if (accepted <= 0) return;

        int extracted = source.extractEnergy(accepted, false);
        target.receiveEnergy(extracted, false);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
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
