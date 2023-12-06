package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.behaviour.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.interaction.SelectionBehaviour;
import com.klikli_dev.theurgy.content.capability.DefaultMercuryFluxStorage;
import com.klikli_dev.theurgy.content.recipe.wrapper.ReformationArrayRecipeWrapper;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SulfuricFluxEmitterBlockEntity extends BlockEntity {

    public static final int CAPACITY = 1000;
    public MercuryFluxStorage mercuryFluxStorage;

    public LazyOptional<MercuryFluxStorage> mercuryFluxStorageCapability;
    public boolean isValidMultiblock;
    protected List<SulfuricFluxEmitterSelectedPoint> sourcePedestals;
    protected SulfuricFluxEmitterSelectedPoint targetPedestal;
    protected SulfuricFluxEmitterSelectedPoint resultPedestal;
    protected CraftingBehaviour<?, ?, ?> craftingBehaviour;
    protected boolean checkValidMultiblockOnNextQuery;
    protected boolean hasSourceItems;
    protected boolean hasTargetItem;
    private ReformationArrayRecipeWrapper recipeWrapper;

    public SulfuricFluxEmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.SULFURIC_FLUX_EMITTER.get(), pPos, pBlockState);

        this.mercuryFluxStorage = new MercuryFluxStorage(CAPACITY);
        this.mercuryFluxStorageCapability = LazyOptional.of(() -> this.mercuryFluxStorage);

        this.checkValidMultiblockOnNextQuery = true;

        this.sourcePedestals = new ArrayList<>();

        this.craftingBehaviour = new ReformationArrayCraftingBehaviour(this, () -> this.recipeWrapper, () -> null, this::getOutputInventory, () -> this.mercuryFluxStorage);
    }

    public void removeResultPedestal(ReformationResultPedestalBlockEntity pedestal) {
        this.resultPedestal = null;
        this.isValidMultiblock = false;
        this.onDisassembleMultiblock();
    }

    public void removeTargetPedestal(ReformationTargetPedestalBlockEntity pedestal) {
        this.targetPedestal = null;
        this.isValidMultiblock = false;
        this.hasTargetItem = false;
        this.onDisassembleMultiblock();
    }

    public void removeSourcePedestal(ReformationSourcePedestalBlockEntity pedestal) {
        this.sourcePedestals.removeIf(p -> p.getBlockPos().equals(pedestal.getBlockPos()));
        if (this.sourcePedestals.isEmpty()) {
            this.isValidMultiblock = false;
            this.hasSourceItems = false;
            this.onDisassembleMultiblock();
        }
    }

    public boolean isValidMultiblock() {
        if (this.checkValidMultiblockOnNextQuery) {
            this.checkValidMultiblockOnNextQuery = false;
            this.validateMultiblock();
        }
        return this.isValidMultiblock;
    }

    public void validateMultiblock() {
        var wasValidMultiblock = this.isValidMultiblock;

        if (this.targetPedestal != null) {
            var targetPedestalBlockEntity = (ReformationTargetPedestalBlockEntity) this.level.getBlockEntity(this.targetPedestal.getBlockPos());
            if (targetPedestalBlockEntity == null) {
                this.targetPedestal = null;
                this.isValidMultiblock = false;
            }
        }
        if (this.resultPedestal != null) {
            var resultPedestalBlockEntity = (ReformationResultPedestalBlockEntity) this.level.getBlockEntity(this.resultPedestal.getBlockPos());
            if (resultPedestalBlockEntity == null) {
                this.resultPedestal = null;
                this.isValidMultiblock = false;
            }
        }

        var sourcesToRemove = new ArrayList<SulfuricFluxEmitterSelectedPoint>();
        for (var sourcePedestal : this.sourcePedestals) {
            var sourcePedestalBlockEntity = (ReformationSourcePedestalBlockEntity) this.level.getBlockEntity(sourcePedestal.getBlockPos());
            if (sourcePedestalBlockEntity == null) {
                sourcesToRemove.add(sourcePedestal);
            }
        }
        if(!sourcesToRemove.isEmpty())
            this.sourcePedestals.removeAll(sourcesToRemove);
        if (this.sourcePedestals.isEmpty())
            this.isValidMultiblock = false;

        if (wasValidMultiblock != this.isValidMultiblock) {
            if (this.isValidMultiblock) {
                this.onAssembleMultiblock();
            } else {
                this.onDisassembleMultiblock();
            }
        }
    }

    public void onAssembleMultiblock() {
        var targetPedestalBlockEntity = (ReformationTargetPedestalBlockEntity) this.level.getBlockEntity(this.targetPedestal.getBlockPos());

        var sourceInventories = this.sourcePedestals.stream()
                .map(p -> this.level.getBlockEntity(p.getBlockPos()))
                .map(e -> (ReformationSourcePedestalBlockEntity) e)
                .map(e -> e.inputInventory)
                .map(e -> (IItemHandlerModifiable) e)
                .toList();

        this.recipeWrapper = new ReformationArrayRecipeWrapper(sourceInventories, targetPedestalBlockEntity.inputInventory, this.mercuryFluxStorage);
    }

    public void onDisassembleMultiblock() {
        this.recipeWrapper = null;
    }

    public IItemHandlerModifiable getOutputInventory() {
        var pos = this.resultPedestal.getBlockPos();
        var blockEntity = this.level.getBlockEntity(pos);
        return (IItemHandlerModifiable) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).cast().resolve().get();
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
        //TODO: further, we should probably pause checks if that source + target do not produce a valid recipe
        //      we only re-check if one of the pedestal input changes.
        boolean hasInput = this.isValidMultiblock() && this.hasSourceItems && this.hasTargetItem;
        this.craftingBehaviour.tickServer(true, hasInput);

        //TODO: visuals!
        //      use isCrafting boolean, only switch it if we stop crafting
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

    public void onTargetPedestalContentChange(ReformationTargetPedestalBlockEntity pedestal){
        this.hasTargetItem = !pedestal.inputInventory.getStackInSlot(0).isEmpty();
    }

    public void onSourcePedestalContentChange(ReformationSourcePedestalBlockEntity pedestal){
        this.hasSourceItems = this.sourcePedestals.stream().map(p -> this.level.getBlockEntity(p.getBlockPos()))
                .filter(e -> e instanceof ReformationSourcePedestalBlockEntity)
                .map(e -> (ReformationSourcePedestalBlockEntity) e)
                .anyMatch(p -> !p.inputInventory.getStackInSlot(0).isEmpty());
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
