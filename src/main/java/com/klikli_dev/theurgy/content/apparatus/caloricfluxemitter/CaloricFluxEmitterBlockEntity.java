// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter;

import com.klikli_dev.theurgy.content.behaviour.selection.SelectionBehaviour;
import com.klikli_dev.theurgy.content.capability.DefaultMercuryFluxStorage;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageShowCaloricFlux;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CaloricFluxEmitterBlockEntity extends BlockEntity {
    public static final int CAPACITY = 1000;
    //Coal burns for 200t. One coal = 1 mercury shard = 800 mercury flux.
    //we use 100 MF to power for 120t, so we are much more efficient than a brazier.
    public static final int FLUX_PER_HEAT = 100;
    public static final int HEAT_TARGET_FOR_TICKS = 20 * 6;
    public static final int TICK_INTERVAL = 20;

    public MercuryFluxStorage mercuryFluxStorage;

    protected List<CaloricFluxEmitterSelectedPoint> selectedPoints;

    public CaloricFluxEmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CALORIC_FLUX_EMITTER.get(), pPos, pBlockState);

        this.mercuryFluxStorage = new MercuryFluxStorage(CAPACITY);

        this.selectedPoints = new ArrayList<>();
    }

    @Override
    public void onLoad() {
        this.selectedPoints.forEach(point -> point.setLevel(this.getLevel()));
    }

    public void tickServer() {
        if (Objects.requireNonNull(this.getLevel()).getGameTime() % TICK_INTERVAL != 0)
            return; //slow tick

        if (!this.getBlockState().getValue(BlockStateProperties.ENABLED))
            return; //disabled with active redstone

        if (this.selectedPoints.isEmpty())
            return;

        var selectedPoint = this.selectedPoints.getFirst(); //we only have one target point
        if (!this.getSelectionBehaviour().isValid(selectedPoint)) {
            return;
        }

        if (this.mercuryFluxStorage.getEnergyStored() >= FLUX_PER_HEAT) {
            var heatReceiver = Objects.requireNonNull(this.level).getCapability(CapabilityRegistry.HEAT_RECEIVER, selectedPoint.getBlockPos(), selectedPoint.getBlockState(), null, null);

            if (!Objects.requireNonNull(heatReceiver).readyToReceive())
                return;

            if (heatReceiver.getIsHotUntil() > this.getLevel().getGameTime() + TICK_INTERVAL)
                return; //target block is still hot until next tick so do nothing

            this.mercuryFluxStorage.extractEnergy(FLUX_PER_HEAT, false);
            heatReceiver.setHotUntil(this.getLevel().getGameTime() + HEAT_TARGET_FOR_TICKS);

            Networking.sendToTracking((ServerLevel) this.getLevel(), new ChunkPos(this.getBlockPos()), new MessageShowCaloricFlux(this.getBlockPos(), selectedPoint.getBlockPos(), this.getBlockState().getValue(CaloricFluxEmitterBlock.FACING)));
        }
    }


    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);

        pTag.put("mercuryFluxStorage", this.mercuryFluxStorage.serializeNBT(pRegistries));
        pTag.put("selectedPoints", CaloricFluxEmitterSelectedPoint.LIST_CODEC.encodeStart(pRegistries.createSerializationContext(NbtOps.INSTANCE), this.selectedPoints).getOrThrow((e) -> new EncoderException("Failed to encode: " + e + " " + this.selectedPoints)));
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag pTag, @NotNull HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        if (pTag.contains("mercuryFluxStorage"))
            //get instead of getCompound here because the storage serializes as int tag
            //noinspection DataFlowIssue
            this.mercuryFluxStorage.deserializeNBT(pRegistries, pTag.get("mercuryFluxStorage"));

        if (pTag.contains("selectedPoints")) {
            this.selectedPoints = CaloricFluxEmitterSelectedPoint.LIST_CODEC.parse(pRegistries.createSerializationContext(NbtOps.INSTANCE), pTag.get("selectedPoints")).getOrThrow((e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("selectedPoints")));
        }
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.@NotNull DataComponentInput pComponentInput) {
        super.applyImplicitComponents(pComponentInput);

        if (pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE) != null)
            //noinspection DataFlowIssue
            this.mercuryFluxStorage.setEnergyStored(pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder pComponents) {
        super.collectImplicitComponents(pComponents);

        pComponents.set(DataComponentRegistry.MERCURY_FLUX_STORAGE, this.mercuryFluxStorage.getEnergyStored());
    }

    public SelectionBehaviour<CaloricFluxEmitterSelectedPoint> getSelectionBehaviour() {
        return BlockRegistry.CALORIC_FLUX_EMITTER.get().selectionBehaviour();
    }

    public void setSelectedPoints(List<CaloricFluxEmitterSelectedPoint> selectedPoints) {
        this.selectedPoints = selectedPoints;
        this.selectedPoints.removeIf(p -> !p.getBlockPos().closerThan(this.getBlockPos(), this.getSelectionBehaviour().getBlockRange()));
        this.setChanged();
    }

    /**
     * client-side variant that does no checks
     */
    public void setSelectedPointsClient(List<CaloricFluxEmitterSelectedPoint> selectedPoints) {
        this.selectedPoints = selectedPoints;
    }

    public class MercuryFluxStorage extends DefaultMercuryFluxStorage {

        public MercuryFluxStorage(int capacity) {
            super(capacity);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            var received = super.receiveEnergy(maxReceive, simulate);

            if (received > 0) {
                CaloricFluxEmitterBlockEntity.this.setChanged();
            }

            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            var extracted = super.extractEnergy(maxExtract, simulate);

            if (extracted > 0) {
                CaloricFluxEmitterBlockEntity.this.setChanged();
            }

            return extracted;
        }
    }
}
