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
import io.netty.handler.codec.EncoderException;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

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
        if (this.getLevel().getGameTime() % TICK_INTERVAL != 0)
            return; //slow tick

        if (this.selectedPoints.isEmpty())
            return;

        var selectedPoint = this.selectedPoints.get(0); //we only have one target point
        if (!this.getSelectionBehaviour().isValid(selectedPoint)) {
            return;
        }

        if (this.mercuryFluxStorage.getEnergyStored() >= FLUX_PER_HEAT) {
            var heatReceiver = this.level.getCapability(CapabilityRegistry.HEAT_RECEIVER, selectedPoint.getBlockPos(), null);

            if (heatReceiver.getIsHotUntil() > this.getLevel().getGameTime() + TICK_INTERVAL)
                return; //target block is still hot until next tick so do nothing

            this.mercuryFluxStorage.extractEnergy(FLUX_PER_HEAT, false);
            heatReceiver.setHotUntil(this.getLevel().getGameTime() + HEAT_TARGET_FOR_TICKS);

            Networking.sendToTracking(this.getLevel().getChunkAt(this.getBlockPos()), new MessageShowCaloricFlux(this.getBlockPos(), selectedPoint.getBlockPos(), this.getBlockState().getValue(CaloricFluxEmitterBlock.FACING)));
        }
    }


    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("mercuryFluxStorage", this.mercuryFluxStorage.serializeNBT());
        pTag.put("selectedPoints", Util.getOrThrow(CaloricFluxEmitterSelectedPoint.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.selectedPoints), (e) -> new EncoderException("Failed to encode: " + e + " " + this.selectedPoints)));
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("mercuryFluxStorage"))
            //get instead of getCompound here because the storage serializes as int tag
            this.mercuryFluxStorage.deserializeNBT(pTag.get("mercuryFluxStorage"));

        if (pTag.contains("selectedPoints")) {
            this.selectedPoints = Util.getOrThrow(CaloricFluxEmitterSelectedPoint.LIST_CODEC.parse(NbtOps.INSTANCE, pTag.get("selectedPoints")), (e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("selectedPoints")));
        }
    }

    public SelectionBehaviour<CaloricFluxEmitterSelectedPoint> getSelectionBehaviour() {
        return BlockRegistry.CALORIC_FLUX_EMITTER.get().selectionBehaviour();
    }

    public void setSelectedPoints(List<CaloricFluxEmitterSelectedPoint> selectedPoints) {
        this.selectedPoints = selectedPoints;
        this.selectedPoints.removeIf(p -> !p.getBlockPos().closerThan(this.getBlockPos(), this.getSelectionBehaviour().getBlockRange()));
        this.setChanged();
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
