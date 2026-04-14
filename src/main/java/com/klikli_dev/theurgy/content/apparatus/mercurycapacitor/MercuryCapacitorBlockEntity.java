// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercurycapacitor;

import com.klikli_dev.theurgy.content.capability.DefaultMercuryFluxStorage;
import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.content.particle.glow.GlowParticleProvider;
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

import java.util.List;


public class MercuryCapacitorBlockEntity extends BlockEntity {

    /**
     * 10x the capacity of the MercuryCatalyst (500,000 instead of 50,000)
     */
    public static final int CAPACITY = 500000;

    public static final int PUSH_TICK_INTERVAL = 20;
    public static final int PUSH_RATE_PER_TICK = 2;

    public MercuryCapacitorMercuryFluxStorage mercuryFluxStorage;

    public MercuryCapacitorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.MERCURY_CAPACITOR.get(), pPos, pBlockState);

        this.mercuryFluxStorage = new MercuryCapacitorMercuryFluxStorage(CAPACITY);
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
        input.child("mercuryFluxStorage").ifPresent(value -> {
            this.mercuryFluxStorage.deserialize(value);
            if (this.level != null) {
                this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_IMMEDIATE);
            }
        });
    }

    public void writeNetwork(ValueOutput output) {
        ValueOutput fluxOutput = output.child("mercuryFluxStorage");
        this.mercuryFluxStorage.serialize(fluxOutput);
        if (fluxOutput.isEmpty()) {
            output.discard("mercuryFluxStorage");
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
        //Spawn a glow particle at the center of the block, changing color based on fill level
        if (this.level != null && this.level.getRandom().nextFloat() < 0.07f) {
            float fillLevel = this.mercuryFluxStorage.getEnergyStored() / (float) this.mercuryFluxStorage.getMaxEnergyStored();
            int particleColor = MercuryCapacitorBlock.getParticleColorFromFillLevel(fillLevel);
            
            var pos = this.getBlockPos();
            this.level.addParticle(GlowParticleProvider.createOptions(
                    ParticleColor.fromInt(particleColor),
                    0.5f,
                    0.75f,
                    200), pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0, 0, 0);
        }
    }

    protected void pushMercuryFlux() {
        // Collect all valid flux handlers first
        var directions = Direction.allShuffled(this.getLevel().getRandom());
        var targets = new java.util.ArrayList<com.klikli_dev.theurgy.content.capability.MercuryFluxStorage>();
        
        for (var direction : directions) {
            var fluxStorage = this.level.getCapability(CapabilityRegistry.MERCURY_FLUX_HANDLER, this.getBlockPos().relative(direction), null);
            if (fluxStorage != null) {
                targets.add(fluxStorage);
            }
        }
        
        if (targets.isEmpty()) {
            return;
        }
        
        // Calculate how much to push to each target
        int totalToPush = this.mercuryFluxStorage.extractEnergy(PUSH_RATE_PER_TICK * PUSH_TICK_INTERVAL, true);
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
            this.mercuryFluxStorage.extractEnergy(received, false);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput fluxOutput = output.child("mercuryFluxStorage");
        this.mercuryFluxStorage.serialize(fluxOutput);
        if (fluxOutput.isEmpty()) {
            output.discard("mercuryFluxStorage");
        }
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        input.child("mercuryFluxStorage").ifPresent(this.mercuryFluxStorage::deserialize);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter pComponentInput) {
        super.applyImplicitComponents(pComponentInput);

        if (pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()) != null)
            //noinspection DataFlowIssue
            this.mercuryFluxStorage.setEnergyStored(pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder pComponents) {
        super.collectImplicitComponents(pComponents);

        pComponents.set(DataComponentRegistry.MERCURY_FLUX_STORAGE, this.mercuryFluxStorage.getEnergyStored());
    }

    public class MercuryCapacitorMercuryFluxStorage extends DefaultMercuryFluxStorage {

        public static final int UPDATE_THRESHOLD = 1000;
        private int lastUpdateLevel;

        public MercuryCapacitorMercuryFluxStorage(int capacity) {
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
