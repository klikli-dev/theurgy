// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercurycapacitor;

import com.klikli_dev.theurgy.content.capability.SimpleMercuryHandler;
import com.klikli_dev.theurgy.content.capability.MercuryFluxHandler;
import com.klikli_dev.theurgy.content.item.mode.SideModeSetter;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.util.ValueIOUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class MercuryCapacitorBlockEntity extends BlockEntity implements SideModeSetter {

    /**
     * 10x the capacity of the MercuryCatalyst (500,000 instead of 50,000)
     */
    public static final int CAPACITY = 1000 * 50 * 10;

    public static final int PUSH_TICK_INTERVAL = 20;
    public static final int PUSH_RATE_PER_SIDE_PER_TICK = 2;

    public MercuryCapacitorMercuryFluxHandler mercuryFluxHandler;

    /**
     * Pre-constructed side-aware storage wrappers for each direction.
     */
    private final Map<Direction, MercuryFluxHandler> sideAwareStorages = new EnumMap<>(Direction.class);

    /**
     * Side configuration for each direction. Default is NONE (no interaction).
     */
    private final Map<Direction, SideMode> sideModes = new EnumMap<>(Direction.class);

    public MercuryCapacitorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.MERCURY_CAPACITOR.get(), pPos, pBlockState);

        this.mercuryFluxHandler = new MercuryCapacitorMercuryFluxHandler(CAPACITY);

        // Pre-construct side-aware storage wrappers
        for (var direction : Direction.values()) {
            this.sideAwareStorages.put(direction, new SideAwareMercuryFluxHandler(this.mercuryFluxHandler, direction));
        }

        // Default: TOP and BOTTOM are OUTPUT (push flux up/down), other sides are INPUT (receive flux)
        this.sideModes.put(Direction.UP, SideMode.OUTPUT);
        this.sideModes.put(Direction.DOWN, SideMode.OUTPUT);
        this.sideModes.put(Direction.NORTH, SideMode.INPUT);
        this.sideModes.put(Direction.SOUTH, SideMode.INPUT);
        this.sideModes.put(Direction.EAST, SideMode.INPUT);
        this.sideModes.put(Direction.WEST, SideMode.INPUT);
    }

    /**
     * Get the mode for a specific side.
     */
    public SideMode getSideMode(Direction direction) {
        return this.sideModes.getOrDefault(direction, SideMode.NONE);
    }

    /**
     * Returns a mercury flux storage wrapper that respects side mode for receiving.
     * If side is null, returns the full storage (for internal use).
     */
    public MercuryFluxHandler getMercuryFluxHandler(@Nullable Direction side) {
        if (side == null) {
            return this.mercuryFluxHandler;
        }
        return this.sideAwareStorages.getOrDefault(side, this.mercuryFluxHandler);
    }

    /**
     * Wrapper that enforces side mode on receive operations.
     */
    private class SideAwareMercuryFluxHandler implements MercuryFluxHandler {
        private final MercuryFluxHandler delegate;
        private final Direction side;

        public SideAwareMercuryFluxHandler(MercuryFluxHandler delegate, Direction side) {
            this.delegate = delegate;
            this.side = side;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            var mode = MercuryCapacitorBlockEntity.this.getSideMode(this.side);
            // Only allow receiving if side is INPUT or BOTH
            if (mode != SideMode.INPUT && mode != SideMode.BOTH) {
                return 0;
            }
            return this.delegate.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            var mode = MercuryCapacitorBlockEntity.this.getSideMode(this.side);
            if (mode != SideMode.OUTPUT && mode != SideMode.BOTH) {
                return 0;
            }
            return this.delegate.extractEnergy(maxExtract, simulate);
        }

        @Override
        public int getEnergyStored() {
            return this.delegate.getEnergyStored();
        }

        @Override
        public void setEnergyStored(int energy) {
            this.delegate.setEnergyStored(energy);
        }

        @Override
        public int getMaxEnergyStored() {
            return this.delegate.getMaxEnergyStored();
        }

        @Override
        public boolean canExtract() {
            var mode = MercuryCapacitorBlockEntity.this.getSideMode(this.side);
            return (mode == SideMode.OUTPUT || mode == SideMode.BOTH) && this.delegate.canExtract();
        }

        @Override
        public boolean canReceive() {
            var mode = MercuryCapacitorBlockEntity.this.getSideMode(this.side);
            // Only allow receiving if side is INPUT or BOTH
            return (mode == SideMode.INPUT || mode == SideMode.BOTH) && this.delegate.canReceive();
        }
    }

    /**
     * Set the mode for a specific side.
     */
    public void setSideMode(Direction direction, SideMode mode) {
        this.sideModes.put(direction, mode);
        this.setChanged();
        this.sendBlockUpdated();
    }

    /**
     * Cycle to the next mode for a specific side (for wand interaction).
     */
    public SideMode cycleSideMode(Direction direction) {
        var current = this.getSideMode(direction);
        var next = switch (current) {
            case NONE -> SideMode.INPUT;
            case INPUT -> SideMode.OUTPUT;
            case OUTPUT -> SideMode.BOTH;
            case BOTH -> SideMode.NONE;
        };
        this.setSideMode(direction, next);
        return next;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return ValueIOUtils.serialize(pRegistries, this::writeNetwork);
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        this.readNetwork(input);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ValueInput input) {
        this.readNetwork(input);
    }

    public void readNetwork(ValueInput input) {
        input.child("MercuryFluxHandler").ifPresent(value -> {
            this.mercuryFluxHandler.deserialize(value);
            if (this.level != null) {
                this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_IMMEDIATE);
            }
        });

        input.child("sideModes").ifPresent(child -> {
            for (var direction : Direction.values()) {
                var modeOrdinal = child.getInt(direction.name()).orElse(0);
                // Clamp to valid ordinal range to prevent ArrayIndexOutOfBoundsException from corrupted NBT
                var safeOrdinal = Math.clamp(modeOrdinal, 0, SideMode.values().length - 1);
                this.sideModes.put(direction, SideMode.values()[safeOrdinal]);
            }
        });
    }

    public void writeNetwork(ValueOutput output) {
        ValueOutput fluxOutput = output.child("MercuryFluxHandler");
        this.mercuryFluxHandler.serialize(fluxOutput);
        if (fluxOutput.isEmpty()) {
            output.discard("MercuryFluxHandler");
        }

        ValueOutput sideModesOutput = output.child("sideModes");
        for (var direction : Direction.values()) {
            var mode = this.sideModes.getOrDefault(direction, SideMode.NONE);
            sideModesOutput.putInt(direction.name(), mode.ordinal());
        }
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide())
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public void tickServer() {
        if (this.getLevel().getGameTime() % PUSH_TICK_INTERVAL == 0) {
            if (this.getBlockState().getValue(BlockStateProperties.ENABLED)) {
                this.pushMercuryFlux();
            }
        }
    }

    public void tickClient() {
        // Glow particle is now rendered by MercuryCapacitorRenderer instead of spawning particles
        // This method is kept for potential future client-side logic
    }

    protected void pushMercuryFlux() {
        // Collect only sides that have OUTPUT or BOTH mode
        var directions = Direction.allShuffled(this.getLevel().getRandom());
        var targets = new ArrayList<MercuryFluxHandler>();

        for (var direction : directions) {
            var mode = this.getSideMode(direction);
            if (mode != SideMode.OUTPUT && mode != SideMode.BOTH) {
                continue;
            }

            var fluxStorage = this.level.getCapability(CapabilityRegistry.MERCURY_FLUX_HANDLER, this.getBlockPos().relative(direction), direction.getOpposite());
            if (fluxStorage != null) {
                targets.add(fluxStorage);
            }
        }

        if (targets.isEmpty()) {
            return;
        }

        // Calculate how much to push to each target (scale by number of targets to maintain throughput)
        int totalToPush = this.mercuryFluxHandler.extractEnergy(PUSH_RATE_PER_SIDE_PER_TICK * PUSH_TICK_INTERVAL * targets.size(), true);
        if (totalToPush <= 0) {
            return;
        }

        int perTarget = totalToPush / targets.size();
        int remainder = totalToPush % targets.size();

        // Distribute evenly to all targets
        for (int i = 0; i < targets.size(); i++) {
            int amount = perTarget + (i < remainder ? 1 : 0);
            if (amount <= 0) continue;

            var received = targets.get(i).receiveEnergy(amount, false);
            this.mercuryFluxHandler.extractEnergy(received, false);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput fluxOutput = output.child("MercuryFluxHandler");
        this.mercuryFluxHandler.serialize(fluxOutput);
        if (fluxOutput.isEmpty()) {
            output.discard("MercuryFluxHandler");
        }
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        input.child("MercuryFluxHandler").ifPresent(this.mercuryFluxHandler::deserialize);

        input.child("sideModes").ifPresent(child -> {
            for (var direction : Direction.values()) {
                var modeOrdinal = child.getInt(direction.name()).orElse(0);
                // Clamp to valid ordinal range to prevent ArrayIndexOutOfBoundsException from corrupted NBT
                var safeOrdinal = Math.clamp(modeOrdinal, 0, SideMode.values().length - 1);
                this.sideModes.put(direction, SideMode.values()[safeOrdinal]);
            }
        });
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter pComponentInput) {
        super.applyImplicitComponents(pComponentInput);

        if (pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()) != null)
            //noinspection DataFlowIssue
            this.mercuryFluxHandler.setEnergyStored(pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder pComponents) {
        super.collectImplicitComponents(pComponents);

        pComponents.set(DataComponentRegistry.MERCURY_FLUX_STORAGE, this.mercuryFluxHandler.getEnergyStored());
    }

    public class MercuryCapacitorMercuryFluxHandler extends SimpleMercuryHandler {

        public static final int UPDATE_THRESHOLD = 1000;
        private int lastUpdateLevel;

        public MercuryCapacitorMercuryFluxHandler(int capacity) {
            super(capacity);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            var received = super.receiveEnergy(maxReceive, simulate);

            if (received > 0) {
                MercuryCapacitorBlockEntity.this.setChanged();
                this.trySendBlockUpdated();
            }

            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            var extracted = super.extractEnergy(maxExtract, simulate);

            if (extracted > 0) {
                MercuryCapacitorBlockEntity.this.setChanged();
                this.trySendBlockUpdated();
            }

            return extracted;
        }

        public void trySendBlockUpdated() {
            var currentLevel = this.getEnergyStored();
            if (Math.abs(this.lastUpdateLevel - currentLevel) > UPDATE_THRESHOLD) {
                this.lastUpdateLevel = currentLevel;
                MercuryCapacitorBlockEntity.this.sendBlockUpdated();
            }
        }
    }
}
