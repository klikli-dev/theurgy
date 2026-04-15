// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.capability.DefaultMercuryFluxStorage;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.util.ValueIOUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MercuryFluxEmitterBlockEntity extends BlockEntity {

    public static final int CAPACITY = 1000;
    public static final int FLUX_PER_TRANSFER = 10;
    public static final int TICK_INTERVAL = 20;

    public MercuryFluxStorage mercuryFluxStorage;

    protected List<MercuryFluxEmitterSelectedPoint> selectedPoints;

    public MercuryFluxEmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.MERCURY_FLUX_EMITTER.get(), pPos, pBlockState);

        this.mercuryFluxStorage = new MercuryFluxStorage(CAPACITY);
        this.selectedPoints = new ArrayList<>();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.selectedPoints.forEach(point -> point.setLevel(this.getLevel()));
    }

    public void tickServer() {
        if (Objects.requireNonNull(this.getLevel()).getGameTime() % TICK_INTERVAL != 0) {
            return;
        }

        if (this.selectedPoints.isEmpty()) {
            return;
        }

        var selectedPoint = this.selectedPoints.getFirst();
        if (!this.getLevel().isLoaded(selectedPoint.getBlockPos())) {
            return;
        }

        // Get the source block (the block we are attached to)
        var sourcePos = this.getBlockPos().relative(this.getBlockState().getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING).getOpposite());
        
        // Check if source block has mercury flux capability
        var sourceFluxHandler = this.getLevel().getCapability(
                CapabilityRegistry.MERCURY_FLUX_HANDLER,
                sourcePos,
                this.getLevel().getBlockState(sourcePos),
                null,
                null
        );

        // Try to receive flux from source if we have space
        if (sourceFluxHandler != null && this.mercuryFluxStorage.getMaxEnergyStored() - this.mercuryFluxStorage.getEnergyStored() >= FLUX_PER_TRANSFER) {
            int extracted = sourceFluxHandler.extractEnergy(FLUX_PER_TRANSFER, false);
            if (extracted > 0) {
                this.mercuryFluxStorage.receiveEnergy(extracted, false);
            }
        }

        // Transfer to target if we have flux
        if (this.mercuryFluxStorage.getEnergyStored() >= FLUX_PER_TRANSFER) {
            var targetFluxHandler = this.getLevel().getCapability(
                    CapabilityRegistry.MERCURY_FLUX_HANDLER,
                    selectedPoint.getBlockPos(),
                    this.getLevel().getBlockState(selectedPoint.getBlockPos()),
                    null,
                    null
            );

            if (targetFluxHandler != null && targetFluxHandler.getMaxEnergyStored() - targetFluxHandler.getEnergyStored() >= FLUX_PER_TRANSFER) {
                int extracted = this.mercuryFluxStorage.extractEnergy(FLUX_PER_TRANSFER, false);
                if (extracted > 0) {
                    targetFluxHandler.receiveEnergy(extracted, false);
                }
            }
        }
    }

    public void tickClient() {
        // No client-side ticks needed for now
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput storageOutput = output.child("mercuryFluxStorage");
        this.mercuryFluxStorage.serialize(storageOutput);
        if (storageOutput.isEmpty()) {
            output.discard("mercuryFluxStorage");
        }

        output.store("selectedPoints", MercuryFluxEmitterSelectedPoint.LIST_CODEC, this.selectedPoints);
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        input.child("mercuryFluxStorage").ifPresent(this.mercuryFluxStorage::deserialize);
        this.selectedPoints = input.read("selectedPoints", MercuryFluxEmitterSelectedPoint.LIST_CODEC).orElseGet(ArrayList::new);
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
        this.selectedPoints = input.read("selectedPoints", MercuryFluxEmitterSelectedPoint.LIST_CODEC).orElseGet(ArrayList::new);
    }

    public void writeNetwork(ValueOutput output) {
        output.store("selectedPoints", MercuryFluxEmitterSelectedPoint.LIST_CODEC, this.selectedPoints);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter pComponentInput) {
        super.applyImplicitComponents(pComponentInput);

        if (pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()) != null) {
            this.mercuryFluxStorage.setEnergyStored(pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()));
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder pComponents) {
        super.collectImplicitComponents(pComponents);

        pComponents.set(DataComponentRegistry.MERCURY_FLUX_STORAGE, this.mercuryFluxStorage.getEnergyStored());
    }

    public void setSelectedPoints(List<MercuryFluxEmitterSelectedPoint> selectedPoints) {
        this.selectedPoints = selectedPoints;
        this.setChanged();
    }

    public void setSelectedPointsClient(List<MercuryFluxEmitterSelectedPoint> selectedPoints) {
        this.selectedPoints = selectedPoints;
    }

    public List<MercuryFluxEmitterSelectedPoint> getSelectedPoints() {
        return this.selectedPoints;
    }

    public class MercuryFluxStorage extends DefaultMercuryFluxStorage {

        public MercuryFluxStorage(int capacity) {
            super(capacity);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            var received = super.receiveEnergy(maxReceive, simulate);

            if (received > 0) {
                MercuryFluxEmitterBlockEntity.this.setChanged();
            }

            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            var extracted = super.extractEnergy(maxExtract, simulate);

            if (extracted > 0) {
                MercuryFluxEmitterBlockEntity.this.setChanged();
            }

            return extracted;
        }
    }
}
