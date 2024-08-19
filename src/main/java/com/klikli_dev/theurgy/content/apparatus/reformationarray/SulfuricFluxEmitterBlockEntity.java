// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.selection.SelectionBehaviour;
import com.klikli_dev.theurgy.content.capability.DefaultMercuryFluxStorage;
import com.klikli_dev.theurgy.content.entity.FollowProjectile;
import com.klikli_dev.theurgy.content.recipe.input.ReformationArrayRecipeInput;
import com.klikli_dev.theurgy.content.render.Color;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.util.EntityUtil;
import com.mojang.datafixers.util.Pair;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class
SulfuricFluxEmitterBlockEntity extends BlockEntity {

    public static final int CAPACITY = 1000;
    public MercuryFluxStorage mercuryFluxStorage;

    public boolean isValidMultiblock;
    protected List<SulfuricFluxEmitterSelectedPoint> sourcePedestals;
    protected List<SulfuricFluxEmitterSelectedPoint> sourcePedestalsWithContents;
    protected SulfuricFluxEmitterSelectedPoint targetPedestal;
    protected SulfuricFluxEmitterSelectedPoint resultPedestal;
    protected CraftingBehaviour<?, ?, ?> craftingBehaviour;
    protected boolean checkValidMultiblockOnNextQuery;
    protected boolean hasSourceItems;
    protected boolean hasTargetItem;
    private ReformationArrayRecipeInput ItemHandlerRecipeInput;

    public SulfuricFluxEmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.SULFURIC_FLUX_EMITTER.get(), pPos, pBlockState);

        this.mercuryFluxStorage = new MercuryFluxStorage(CAPACITY);

        this.checkValidMultiblockOnNextQuery = true;

        this.sourcePedestals = new ArrayList<>();
        this.sourcePedestalsWithContents = new ArrayList<>();

        this.craftingBehaviour = new ReformationArrayCraftingBehaviour(this, () -> this.ItemHandlerRecipeInput, () -> null, this::getOutputInventory, () -> this.mercuryFluxStorage);
    }

    public void removeResultPedestal(ReformationResultPedestalBlockEntity pedestal) {
        this.isValidMultiblock = false;
        this.onDisassembleMultiblock();
    }

    public void removeTargetPedestal(ReformationTargetPedestalBlockEntity pedestal) {
        this.isValidMultiblock = false;
        this.hasTargetItem = false;
        this.onDisassembleMultiblock();
    }

    public void removeSourcePedestal(ReformationSourcePedestalBlockEntity pedestal) {
        //check if any valid source pedestals remain.
        var hasRemainingPedestals = this.sourcePedestals.stream()
                .filter(p -> !p.getBlockPos().equals(pedestal.getBlockPos())) //skip the one that is being removed
                .filter(p -> this.level.isLoaded(p.getBlockPos()))
                .map(p -> this.level.getBlockEntity(p.getBlockPos()))
                .anyMatch(e -> e instanceof ReformationSourcePedestalBlockEntity);

        //if not, we don't have a valid multiblock anymore
        if (!hasRemainingPedestals) {
            this.isValidMultiblock = false;
            this.hasSourceItems = false;
            this.onDisassembleMultiblock();
        } else {
            //force update of source pedestals with contents
            this.onSourcePedestalContentChange(null);
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

        this.isValidMultiblock = true; //set to true, then set to false if any of the checks fail

        if (this.targetPedestal != null) {
            var targetPedestalBlockEntity = this.level.getBlockEntity(this.targetPedestal.getBlockPos());
            if (!(targetPedestalBlockEntity instanceof ReformationTargetPedestalBlockEntity)) {
                this.isValidMultiblock = false;
            }
        } else {
            this.isValidMultiblock = false;
        }

        if (this.resultPedestal != null) {
            var resultPedestalBlockEntity = this.level.getBlockEntity(this.resultPedestal.getBlockPos());
            if (!(resultPedestalBlockEntity instanceof ReformationResultPedestalBlockEntity)) {
                this.isValidMultiblock = false;
            }
        } else {
            this.isValidMultiblock = false;
        }

        var hasSourcePedestals = false;
        for (var sourcePedestal : this.sourcePedestals) {
            var sourcePedestalBlockEntity = this.level.getBlockEntity(sourcePedestal.getBlockPos());
            if (sourcePedestalBlockEntity instanceof ReformationSourcePedestalBlockEntity) {
                hasSourcePedestals = true;
            }
        }

        this.sourcePedestalsWithContents.clear();
        if (!hasSourcePedestals)
            this.isValidMultiblock = false;
        else {
            //now force rebuilding the source pedestals with contents
            this.onSourcePedestalContentChange(null);
        }

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
        targetPedestalBlockEntity.setSulfuricFluxEmitter(this);
        this.onTargetPedestalContentChange(targetPedestalBlockEntity);

        var resultPedestalBlockEntity = (ReformationResultPedestalBlockEntity) this.level.getBlockEntity(this.resultPedestal.getBlockPos());
        resultPedestalBlockEntity.setSulfuricFluxEmitter(this);

        var sourceInventories = this.sourcePedestals.stream()
                .map(p -> this.level.getBlockEntity(p.getBlockPos()))
                .filter(e -> e instanceof ReformationSourcePedestalBlockEntity) //filter out potentially (currently) null ones
                .map(e -> (ReformationSourcePedestalBlockEntity) e)
                .peek(e -> e.setSulfuricFluxEmitter(this))
                .map(e -> e.inputInventory)
                .map(e -> (IItemHandlerModifiable) e)
                .toList();

        this.onSourcePedestalContentChange(null); //only call it once as we don't need to call it on each


        this.ItemHandlerRecipeInput = new ReformationArrayRecipeInput(sourceInventories, targetPedestalBlockEntity.inputInventory, this.mercuryFluxStorage);
    }

    public void onDisassembleMultiblock() {
        this.ItemHandlerRecipeInput = null;
    }

    public IItemHandlerModifiable getOutputInventory() {
        var pos = this.resultPedestal.getBlockPos();

        if(!this.level.isLoaded(pos))
            return null;

        var blockEntity = this.level.getBlockEntity(pos);

        if (blockEntity instanceof ReformationResultPedestalBlockEntity pedestal) {
            return pedestal.outputInventory;
        }
        return null;
    }

    public void tickServer() {
        boolean hasInput = this.isValidMultiblock() && this.hasSourceItems && this.hasTargetItem;
        this.craftingBehaviour.tickServer(true, hasInput);
    }

    public void tickClient() {
        if (this.craftingBehaviour.isProcessing()) {
            if (this.level.getGameTime() % 40 == 0) {
                DistHelper.sendTargetProjectile(this);
            }
        }
    }

    @Override
    public void onLoad() {
        if (this.targetPedestal != null)
            this.targetPedestal.setLevel(this.level);
        if (this.resultPedestal != null)
            this.resultPedestal.setLevel(this.level);
        this.sourcePedestals.forEach(point -> point.setLevel(this.getLevel()));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);

        pTag.put("mercuryFluxStorage", this.mercuryFluxStorage.serializeNBT(pRegistries));
        pTag.put("sourcePedestals", SulfuricFluxEmitterSelectedPoint.LIST_CODEC.encodeStart(pRegistries.createSerializationContext(NbtOps.INSTANCE), this.sourcePedestals).getOrThrow((e) -> new EncoderException("Failed to encode: " + e + " " + this.sourcePedestals)));

        if (this.targetPedestal != null)
            pTag.put("targetPedestal", SulfuricFluxEmitterSelectedPoint.CODEC.encodeStart(pRegistries.createSerializationContext(NbtOps.INSTANCE), this.targetPedestal).getOrThrow((e) -> new EncoderException("Failed to encode: " + e + " " + this.targetPedestal)));

        if (this.resultPedestal != null)
            pTag.put("resultPedestal", SulfuricFluxEmitterSelectedPoint.CODEC.encodeStart(pRegistries.createSerializationContext(NbtOps.INSTANCE), this.resultPedestal).getOrThrow((e) -> new EncoderException("Failed to encode: " + e + " " + this.resultPedestal)));

        this.craftingBehaviour.saveAdditional(pTag, pRegistries);

    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        if (pTag.contains("mercuryFluxStorage"))
            //get instead of getCompound here because the storage serializes as int tag
            this.mercuryFluxStorage.deserializeNBT(pRegistries, pTag.get("mercuryFluxStorage"));

        if (pTag.contains("sourcePedestals")) {
            this.sourcePedestals.clear();
            this.sourcePedestals.addAll(SulfuricFluxEmitterSelectedPoint.LIST_CODEC.parse(pRegistries.createSerializationContext(NbtOps.INSTANCE), pTag.get("sourcePedestals")).getOrThrow((e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("sourcePedestals"))));
        }

        if (pTag.contains("targetPedestal")) {
            this.targetPedestal = SulfuricFluxEmitterSelectedPoint.CODEC.parse(pRegistries.createSerializationContext(NbtOps.INSTANCE), pTag.get("targetPedestal")).getOrThrow((e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("targetPedestal")));
        }

        if (pTag.contains("resultPedestal")) {
            this.resultPedestal = SulfuricFluxEmitterSelectedPoint.CODEC.parse(pRegistries.createSerializationContext(NbtOps.INSTANCE), pTag.get("resultPedestal")).getOrThrow((e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("resultPedestal")));
        }

        this.craftingBehaviour.loadAdditional(pTag, pRegistries);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        var tag = new CompoundTag();
        this.writeNetwork(tag, pRegistries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider pRegistries) {
        this.readNetwork(tag, pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider pRegistries) {
        var tag = packet.getTag();
        if (tag != null) {
            this.readNetwork(tag, pRegistries);
        }
    }

    public void readNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {

        if (pTag.contains("sourcePedestalsWithContents")) {
            this.sourcePedestalsWithContents.clear();
            this.sourcePedestalsWithContents.addAll(SulfuricFluxEmitterSelectedPoint.LIST_CODEC.parse(pRegistries.createSerializationContext(NbtOps.INSTANCE), pTag.get("sourcePedestalsWithContents")).getOrThrow((e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("sourcePedestalsWithContents"))));
        }

        if (pTag.contains("targetPedestal")) {
            this.targetPedestal = SulfuricFluxEmitterSelectedPoint.CODEC.parse(pRegistries.createSerializationContext(NbtOps.INSTANCE), pTag.get("targetPedestal")).getOrThrow((e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("targetPedestal")));
        }

        if (pTag.contains("resultPedestal")) {
            this.resultPedestal = SulfuricFluxEmitterSelectedPoint.CODEC.parse(pRegistries.createSerializationContext(NbtOps.INSTANCE), pTag.get("resultPedestal")).getOrThrow((e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("resultPedestal")));
        }

        this.craftingBehaviour.readNetwork(pTag, pRegistries);
    }

    public void writeNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("sourcePedestalsWithContents", SulfuricFluxEmitterSelectedPoint.LIST_CODEC.encodeStart(pRegistries.createSerializationContext(NbtOps.INSTANCE), this.sourcePedestalsWithContents).getOrThrow((e) -> new EncoderException("Failed to encode: " + e + " " + this.sourcePedestalsWithContents)));

        if (this.targetPedestal != null)
            pTag.put("targetPedestal", SulfuricFluxEmitterSelectedPoint.CODEC.encodeStart(pRegistries.createSerializationContext(NbtOps.INSTANCE), this.targetPedestal).getOrThrow((e) -> new EncoderException("Failed to encode: " + e + " " + this.targetPedestal)));

        if (this.resultPedestal != null)
            pTag.put("resultPedestal", SulfuricFluxEmitterSelectedPoint.CODEC.encodeStart(pRegistries.createSerializationContext(NbtOps.INSTANCE), this.resultPedestal).getOrThrow((e) -> new EncoderException("Failed to encode: " + e + " " + this.resultPedestal)));

        this.craftingBehaviour.writeNetwork(pTag, pRegistries);
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput pComponentInput) {
        super.applyImplicitComponents(pComponentInput);

        if (pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE) != null)
            this.mercuryFluxStorage.setEnergyStored(pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder pComponents) {
        super.collectImplicitComponents(pComponents);

        pComponents.set(DataComponentRegistry.MERCURY_FLUX_STORAGE, this.mercuryFluxStorage.getEnergyStored());
    }

    public SelectionBehaviour<SulfuricFluxEmitterSelectedPoint> getSelectionBehaviour() {
        return BlockRegistry.SULFURIC_FLUX_EMITTER.get().selectionBehaviour();
    }

    public void setSelectedPoints(List<SulfuricFluxEmitterSelectedPoint> sourcePedestals, SulfuricFluxEmitterSelectedPoint targetPedestal, SulfuricFluxEmitterSelectedPoint resultPedestal) {
        var range = this.getSelectionBehaviour().getBlockRange();

        this.sourcePedestals.clear();
        this.sourcePedestals.addAll(sourcePedestals);
        this.sourcePedestals.removeIf(p -> !p.getBlockPos().closerThan(this.getBlockPos(), range));

        this.targetPedestal = targetPedestal == null ? null : targetPedestal.getBlockPos().closerThan(this.getBlockPos(), range) ? targetPedestal : null;
        this.resultPedestal = resultPedestal == null ? null : resultPedestal.getBlockPos().closerThan(this.getBlockPos(), range) ? resultPedestal : null;

        this.checkValidMultiblockOnNextQuery = true;

        this.setChanged();
    }

    /**
     * client-side variant that does no checks
     */
    public void setSelectedPointsClient(List<SulfuricFluxEmitterSelectedPoint> sourcePedestals, SulfuricFluxEmitterSelectedPoint targetPedestal, SulfuricFluxEmitterSelectedPoint resultPedestal) {
        this.sourcePedestals.clear();
        this.sourcePedestals.addAll(sourcePedestals);

        this.targetPedestal = targetPedestal;
        this.resultPedestal = resultPedestal;
    }

    public void onTargetPedestalContentChange(ReformationTargetPedestalBlockEntity pedestal) {
        this.hasTargetItem = !pedestal.inputInventory.getStackInSlot(0).isEmpty();
    }

    public void onSourcePedestalContentChange(ReformationSourcePedestalBlockEntity pedestal) {
        this.sourcePedestalsWithContents.clear();
        this.hasSourceItems = this.sourcePedestals.stream().map(p -> new Pair<>(p, this.level.getBlockEntity(p.getBlockPos())))
                .filter(p -> p.getSecond() instanceof ReformationSourcePedestalBlockEntity)
                .map(p -> new Pair<>(p.getFirst(), (ReformationSourcePedestalBlockEntity) p.getSecond()))
                .filter(p -> !p.getSecond().inputInventory.getStackInSlot(0).isEmpty())
                .peek(p -> this.sourcePedestalsWithContents.add(p.getFirst()))
                .count() > 0; //need to count to force the stream to run on all elements. findAny() would only run on one.
        this.setChanged();
    }

    public static class DistHelper {

        static void sendTargetProjectile(SulfuricFluxEmitterBlockEntity emitter) {
            var normal = Vec3.atLowerCornerOf(emitter.getBlockState().getValue(BlockStateProperties.FACING).getNormal());
            var from = Vec3.atCenterOf(emitter.getBlockPos()).subtract(normal.scale(0.5));
            var to = Vec3.atCenterOf(emitter.targetPedestal.getBlockPos()).add(0, 0.5, 0);

            if (emitter.level.isLoaded(BlockPos.containing(to)) && emitter.level.isLoaded(BlockPos.containing(from)) && emitter.level.isClientSide) {
                FollowProjectile projectile = new FollowProjectile(emitter.level, from, to, new Color(0xffffff, false), new Color(0x0000ff, false), 0.1f, 0.3f, (targetProjectile) -> {
                    DistHelper.sendSourceProjectiles(targetProjectile, emitter);
                });
                projectile.setDeltaMovement(normal.scale(0.3f));

                EntityUtil.spawnEntityClientSide(emitter.level, projectile);
            }
        }

        static void sendSourceProjectiles(FollowProjectile targetProjectile, SulfuricFluxEmitterBlockEntity emitter) {
            for (var sourcePedestal : emitter.sourcePedestalsWithContents) {
                var from = targetProjectile.position();
                var to = Vec3.atCenterOf(sourcePedestal.getBlockPos()).add(0, 0.7, 0);
                var normal = targetProjectile.to().subtract(targetProjectile.from()).normalize();

                if (emitter.level.isLoaded(BlockPos.containing(to)) && emitter.level.isLoaded(BlockPos.containing(from))) {
                    FollowProjectile projectile = new FollowProjectile(emitter.level, from, to, new Color(0x0000ff, false), new Color(0xFF00FF, false), 0.1f, 0.3f,
                            (sourceProjectile) -> {
                                DistHelper.sendResultProjectile(sourceProjectile, emitter);
                            });

                    //the scale is "force" with which the projectile starts moving in the direction of the normal
                    projectile.setDeltaMovement(normal.scale(0.3f));

                    EntityUtil.spawnEntityClientSide(emitter.level, projectile);
                }
            }
        }

        static void sendResultProjectile(FollowProjectile sourceProjectile, SulfuricFluxEmitterBlockEntity emitter) {
            var from = sourceProjectile.position();
            var to = Vec3.atCenterOf(emitter.resultPedestal.getBlockPos()).add(0, 0.7, 0);
            var normal = sourceProjectile.to().subtract(sourceProjectile.from()).normalize();

            if (emitter.level.isLoaded(BlockPos.containing(to)) && emitter.level.isLoaded(BlockPos.containing(from))) {
                FollowProjectile projectile = new FollowProjectile(emitter.level, from, to, new Color(0xAA08AA, false), new Color(0x00FF00, false), 0.1f, 1f);

                //the scale is "force" with which the projectile starts moving in the direction of the normal
                projectile.setDeltaMovement(normal.scale(0.3f));

                EntityUtil.spawnEntityClientSide(emitter.level, projectile);
            }
        }
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
