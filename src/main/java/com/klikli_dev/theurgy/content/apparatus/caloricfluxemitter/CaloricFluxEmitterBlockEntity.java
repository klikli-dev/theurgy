package com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter;

import com.klikli_dev.theurgy.content.behaviour.interaction.SelectionBehaviour;
import com.klikli_dev.theurgy.content.capability.DefaultMercuryFluxStorage;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageShowCaloricFlux;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CaloricFluxEmitterBlockEntity extends BlockEntity {
    public static final int CAPACITY = 1000;
    public static final int FLUX_PER_HEAT = 100;
    public static final int HEAT_TARGET_FOR_TICKS = 20 * 6;
    public static final int TICK_INTERVAL = 20;

    public MercuryFluxStorage mercuryFluxStorage;

    public LazyOptional<MercuryFluxStorage> mercuryFluxStorageCapability;

    protected List<CaloricFluxEmitterSelectedPoint> selectedPoints;

    public CaloricFluxEmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CALORIC_FLUX_EMITTER.get(), pPos, pBlockState);


        this.mercuryFluxStorage = new MercuryFluxStorage(CAPACITY);
        this.mercuryFluxStorageCapability = LazyOptional.of(() -> this.mercuryFluxStorage);

        this.selectedPoints = new ArrayList<>();
    }

    public void tickServer() {
        if (this.getLevel().getGameTime() % TICK_INTERVAL != 0)
            return; //slow tick

        if (this.selectedPoints.isEmpty())
            return;

        var selectedPoint = this.selectedPoints.get(0); //we only have one target point
        if (!this.getSelectionBehaviour().isValid(selectedPoint)) {
            this.selectedPoints.remove(0);
            this.setChanged();
            return;
        }

        if (this.mercuryFluxStorage.getEnergyStored() >= FLUX_PER_HEAT) {
            var blockEntity = this.getLevel().getBlockEntity(selectedPoint.getBlockPos());
            var heatReceiver = blockEntity.getCapability(CapabilityRegistry.HEAT_RECEIVER).orElse(null);

            if (heatReceiver.getIsHotUntil() > this.getLevel().getGameTime() + TICK_INTERVAL)
                return; //target block is still hot until next tick so do nothing

            this.mercuryFluxStorage.extractEnergy(FLUX_PER_HEAT, false);
            heatReceiver.setHotUntil(this.getLevel().getGameTime() + HEAT_TARGET_FOR_TICKS);

            Networking.sendToTracking(this.getLevel().getChunkAt(this.getBlockPos()), new MessageShowCaloricFlux(this.getBlockPos(), selectedPoint.getBlockPos()));
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityRegistry.MERCURY_FLUX) {
            return this.mercuryFluxStorageCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        this.mercuryFluxStorageCapability.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("mercuryFluxStorage", this.mercuryFluxStorage.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("mercuryFluxStorage"))
            //get instead of getCompound here because the storage serializes as int tag
            this.mercuryFluxStorage.deserializeNBT(pTag.get("mercuryFluxStorage"));
    }

    public SelectionBehaviour<CaloricFluxEmitterSelectedPoint> getSelectionBehaviour() {
        return BlockRegistry.CALORIC_FLUX_EMITTER.get().getSelectionBehaviour();
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
