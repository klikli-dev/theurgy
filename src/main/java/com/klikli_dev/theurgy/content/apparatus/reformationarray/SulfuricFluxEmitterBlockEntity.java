package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.behaviour.interaction.SelectionBehaviour;
import com.klikli_dev.theurgy.content.capability.DefaultMercuryFluxStorage;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import io.netty.handler.codec.EncoderException;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SulfuricFluxEmitterBlockEntity extends BlockEntity {

    public static final int CAPACITY = 1000;
    public MercuryFluxStorage mercuryFluxStorage;

    public LazyOptional<MercuryFluxStorage> mercuryFluxStorageCapability;

    protected List<SulfuricFluxEmitterSelectedPoint> sourcePedestals;
    protected SulfuricFluxEmitterSelectedPoint targetPedestal;
    protected SulfuricFluxEmitterSelectedPoint resultPedestal;

    public SulfuricFluxEmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.SULFURIC_FLUX_EMITTER.get(), pPos, pBlockState);

        this.mercuryFluxStorage = new MercuryFluxStorage(CAPACITY);
        this.mercuryFluxStorageCapability = LazyOptional.of(() -> this.mercuryFluxStorage);

        this.sourcePedestals = new ArrayList<>();
    }

    @Override
    public void onLoad() {
        if (this.targetPedestal != null)
            this.targetPedestal.setLevel(this.level);
        if (this.resultPedestal != null)
            this.resultPedestal.setLevel(this.level);
        this.sourcePedestals.forEach(point -> point.setLevel(this.getLevel()));
    }

    public void tickServer() {
        //TODO: before starting, check if all pedestals are available
        //TODO: before creating result, check if all pedestals are available
        //TODO: if a pedestal is destroyed it should deregister itself? -> probably not because it does not know
        //      or we do it like incubator and DO store a reference?
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
        pTag.put("sourcePedestals", Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.sourcePedestals), (e) -> new EncoderException("Failed to encode: " + e + " " + this.sourcePedestals)));

        if (this.targetPedestal != null)
            pTag.put("targetPedestal", Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.CODEC.encodeStart(NbtOps.INSTANCE, this.targetPedestal), (e) -> new EncoderException("Failed to encode: " + e + " " + this.targetPedestal)));

        if (this.resultPedestal != null)
            pTag.put("resultPedestal", Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.CODEC.encodeStart(NbtOps.INSTANCE, this.resultPedestal), (e) -> new EncoderException("Failed to encode: " + e + " " + this.resultPedestal)));
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("mercuryFluxStorage"))
            //get instead of getCompound here because the storage serializes as int tag
            this.mercuryFluxStorage.deserializeNBT(pTag.get("mercuryFluxStorage"));

        if (pTag.contains("sourcePedestals")) {
            this.sourcePedestals = Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.LIST_CODEC.parse(NbtOps.INSTANCE, pTag.get("sourcePedestals")), (e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("sourcePedestals")));
        }

        if (pTag.contains("targetPedestal")) {
            this.targetPedestal = Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.CODEC.parse(NbtOps.INSTANCE, pTag.get("targetPedestal")), (e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("targetPedestal")));
        }

        if (pTag.contains("resultPedestal")) {
            this.resultPedestal = Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.CODEC.parse(NbtOps.INSTANCE, pTag.get("resultPedestal")), (e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("resultPedestal")));
        }
    }

    public SelectionBehaviour<SulfuricFluxEmitterSelectedPoint> getSelectionBehaviour() {
        return BlockRegistry.SULFURIC_FLUX_EMITTER.get().getSelectionBehaviour();
    }

    public void setSelectedPoints(List<SulfuricFluxEmitterSelectedPoint> sourcePedestals, SulfuricFluxEmitterSelectedPoint targetPedestal, SulfuricFluxEmitterSelectedPoint resultPedestal) {
        var range = this.getSelectionBehaviour().getBlockRange();

        this.sourcePedestals = sourcePedestals;
        this.sourcePedestals.removeIf(p -> !p.getBlockPos().closerThan(this.getBlockPos(), range));

        this.targetPedestal = targetPedestal == null ? null : targetPedestal.getBlockPos().closerThan(this.getBlockPos(), range) ? targetPedestal : null;
        this.resultPedestal = resultPedestal == null ? null : resultPedestal.getBlockPos().closerThan(this.getBlockPos(), range) ? resultPedestal : null;

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
                SulfuricFluxEmitterBlockEntity.this.setChanged();
            }

            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            var extracted = super.extractEnergy(maxExtract, simulate);

            if (extracted > 0) {
                SulfuricFluxEmitterBlockEntity.this.setChanged();
            }

            return extracted;
        }
    }
}
