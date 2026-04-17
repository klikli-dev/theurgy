// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.behaviour.selection.SelectionBehaviour;
import com.klikli_dev.theurgy.content.capability.SimpleMercuryFluxHandler;
import com.klikli_dev.theurgy.content.capability.SimpleTheurgyEnergyHandler;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.util.NetworkTagHelper;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MercuryFluxEmitterBlockEntity extends BlockEntity {

    public static final int CAPACITY = 1000;
    public static final int FLUX_PER_TRANSFER = 10;
    public static final int TICK_INTERVAL = 20;

    public MercuryFluxEmitterMercuryFluxHandler mercuryFluxHandler;
    public MercuryFluxEmitterEnergyStorage energyStorage;
    protected MercuryFluxEmitterMercuryFluxBehaviour mercuryFluxEmissionBehaviour;
    protected MercuryFluxEmitterEnergyBehaviour energyEmissionBehaviour;

    protected List<MercuryFluxEmitterSelectedPoint> selectedPoints;

    public MercuryFluxEmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.MERCURY_FLUX_EMITTER.get(), pPos, pBlockState);

        this.mercuryFluxHandler = new MercuryFluxEmitterMercuryFluxHandler(CAPACITY);
        this.energyStorage = new MercuryFluxEmitterEnergyStorage(CAPACITY);
        this.mercuryFluxEmissionBehaviour = new MercuryFluxEmitterMercuryFluxBehaviour(this);
        this.energyEmissionBehaviour = new MercuryFluxEmitterEnergyBehaviour(this);
        this.selectedPoints = new ArrayList<>();
    }

    public SelectionBehaviour<MercuryFluxEmitterSelectedPoint> getSelectionBehaviour() {
        return BlockRegistry.MERCURY_FLUX_EMITTER.get().selectionBehaviour();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.selectedPoints.forEach(point -> point.setLevel(this.getLevel()));
    }

    public void tickServer() {
        this.mercuryFluxEmissionBehaviour.tickServer();
        this.energyEmissionBehaviour.tickServer();
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput mercuryFluxOutput = output.child("mercuryFluxHandler");
        this.mercuryFluxHandler.serialize(mercuryFluxOutput);
        if (mercuryFluxOutput.isEmpty()) {
            output.discard("mercuryFluxHandler");
        }

        ValueOutput energyOutput = output.child("energyStorage");
        this.energyStorage.serialize(energyOutput);
        if (energyOutput.isEmpty()) {
            output.discard("energyStorage");
        }

        output.store("selectedPoints", MercuryFluxEmitterSelectedPoint.LIST_CODEC, this.selectedPoints);
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        input.child("mercuryFluxHandler").ifPresent(this.mercuryFluxHandler::deserialize);
        input.child("energyStorage").ifPresent(this.energyStorage::deserialize);
        this.selectedPoints = input.read("selectedPoints", MercuryFluxEmitterSelectedPoint.LIST_CODEC).orElseGet(ArrayList::new);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return NetworkTagHelper.write(pRegistries, this::writeNetwork);
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
        this.selectedPoints.forEach(point -> point.setLevel(this.getLevel()));
    }

    public void writeNetwork(ValueOutput output) {
        output.store("selectedPoints", MercuryFluxEmitterSelectedPoint.LIST_CODEC, this.selectedPoints);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter pComponentInput) {
        super.applyImplicitComponents(pComponentInput);

        if (pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()) != null) {
            this.mercuryFluxHandler.set(pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()));
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder pComponents) {
        super.collectImplicitComponents(pComponents);

        pComponents.set(DataComponentRegistry.MERCURY_FLUX_STORAGE, this.mercuryFluxHandler.getAmountAsInt());
    }

    public void setSelectedPoints(List<MercuryFluxEmitterSelectedPoint> selectedPoints) {
        this.selectedPoints = new ArrayList<>(selectedPoints);
        this.selectedPoints.forEach(point -> point.setLevel(this.getLevel()));
        this.selectedPoints.removeIf(p -> !p.getBlockPos().closerThan(this.getBlockPos(), this.getSelectionBehaviour().getBlockRange()));
        this.setChanged();
    }

    /**
     * client-side variant that does no checks
     */
    public void setSelectedPointsClient(List<MercuryFluxEmitterSelectedPoint> selectedPoints) {
        this.selectedPoints = new ArrayList<>(selectedPoints);
        this.selectedPoints.forEach(point -> point.setLevel(this.getLevel()));
    }

    public List<MercuryFluxEmitterSelectedPoint> getSelectedPoints() {
        return this.selectedPoints;
    }

    public class MercuryFluxEmitterMercuryFluxHandler extends SimpleMercuryFluxHandler {

        public MercuryFluxEmitterMercuryFluxHandler(int capacity) {
            super(capacity);
        }

        @Override
        protected void onEnergyChanged(int previousAmount) {
            MercuryFluxEmitterBlockEntity.this.setChanged();
        }
    }

    public class MercuryFluxEmitterEnergyStorage extends SimpleTheurgyEnergyHandler {

        public MercuryFluxEmitterEnergyStorage(int capacity) {
            super(capacity);
        }

        @Override
        protected void onEnergyChanged(int previousAmount) {
            MercuryFluxEmitterBlockEntity.this.setChanged();
        }
    }
}
