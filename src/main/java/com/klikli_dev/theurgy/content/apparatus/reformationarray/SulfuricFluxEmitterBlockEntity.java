// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.selection.SelectionBehaviour;
import com.klikli_dev.theurgy.content.capability.MercuryFluxHandler;
import com.klikli_dev.theurgy.content.capability.SimpleMercuryFluxHandler;
import com.klikli_dev.theurgy.content.entity.FollowProjectile;
import com.klikli_dev.theurgy.content.recipe.input.ReformationArrayRecipeInput;
import com.klikli_dev.theurgy.content.render.Color;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.util.EntityUtil;
import com.klikli_dev.theurgy.util.NetworkTagHelper;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class
SulfuricFluxEmitterBlockEntity extends BlockEntity {

    public static final int CAPACITY = 1000;
    public SulfuricFluxEmitterMercuryFluxHandler mercuryFluxHandler;

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

        this.mercuryFluxHandler = new SulfuricFluxEmitterMercuryFluxHandler(CAPACITY);

        this.checkValidMultiblockOnNextQuery = true;

        this.sourcePedestals = new ArrayList<>();
        this.sourcePedestalsWithContents = new ArrayList<>();

        this.craftingBehaviour = new ReformationArrayCraftingBehaviour(this, () -> this.ItemHandlerRecipeInput, () -> null, this::getOutputInventory, () -> (MercuryFluxHandler) this.mercuryFluxHandler);
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
                .map(e -> (SettableItemStorage) e.inputInventory)
                .toList();

        this.onSourcePedestalContentChange(null); //only call it once as we don't need to call it on each


        this.ItemHandlerRecipeInput = new ReformationArrayRecipeInput(sourceInventories, targetPedestalBlockEntity.inputInventory, this.mercuryFluxHandler);
    }

    public void onDisassembleMultiblock() {
        this.ItemHandlerRecipeInput = null;
    }

    public SettableItemStorage getOutputInventory() {
        var pos = this.resultPedestal.getBlockPos();

        if (!this.level.isLoaded(pos))
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
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput mercuryFluxOutput = output.child("mercuryFluxHandler");
        this.mercuryFluxHandler.serialize(mercuryFluxOutput);
        if (mercuryFluxOutput.isEmpty()) {
            output.discard("mercuryFluxHandler");
        }

        output.store("sourcePedestals", SulfuricFluxEmitterSelectedPoint.LIST_CODEC, this.sourcePedestals);
        output.storeNullable("targetPedestal", SulfuricFluxEmitterSelectedPoint.CODEC, this.targetPedestal);
        output.storeNullable("resultPedestal", SulfuricFluxEmitterSelectedPoint.CODEC, this.resultPedestal);

        this.craftingBehaviour.saveAdditional(output);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        input.child("mercuryFluxHandler").ifPresent(value -> this.mercuryFluxHandler.deserialize(value));
        this.sourcePedestals = new ArrayList<>(input.read("sourcePedestals", SulfuricFluxEmitterSelectedPoint.LIST_CODEC).orElseGet(ArrayList::new));
        this.targetPedestal = input.read("targetPedestal", SulfuricFluxEmitterSelectedPoint.CODEC).orElse(null);
        this.resultPedestal = input.read("resultPedestal", SulfuricFluxEmitterSelectedPoint.CODEC).orElse(null);

        this.craftingBehaviour.loadAdditional(input);
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
        this.sourcePedestalsWithContents = new ArrayList<>(input.read("sourcePedestalsWithContents", SulfuricFluxEmitterSelectedPoint.LIST_CODEC).orElseGet(ArrayList::new));
        this.targetPedestal = input.read("targetPedestal", SulfuricFluxEmitterSelectedPoint.CODEC).orElse(null);
        this.resultPedestal = input.read("resultPedestal", SulfuricFluxEmitterSelectedPoint.CODEC).orElse(null);
        this.sourcePedestalsWithContents.forEach(point -> point.setLevel(this.getLevel()));
        if (this.targetPedestal != null) this.targetPedestal.setLevel(this.getLevel());
        if (this.resultPedestal != null) this.resultPedestal.setLevel(this.getLevel());
        this.craftingBehaviour.readNetwork(input);
    }

    public void writeNetwork(ValueOutput output) {
        output.store("sourcePedestalsWithContents", SulfuricFluxEmitterSelectedPoint.LIST_CODEC, this.sourcePedestalsWithContents);
        output.storeNullable("targetPedestal", SulfuricFluxEmitterSelectedPoint.CODEC, this.targetPedestal);
        output.storeNullable("resultPedestal", SulfuricFluxEmitterSelectedPoint.CODEC, this.resultPedestal);
        this.craftingBehaviour.writeNetwork(output);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter pComponentInput) {
        super.applyImplicitComponents(pComponentInput);

        if (pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()) != null)
            this.mercuryFluxHandler.set(pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder pComponents) {
        super.collectImplicitComponents(pComponents);

        pComponents.set(DataComponentRegistry.MERCURY_FLUX_STORAGE, this.mercuryFluxHandler.getAmountAsInt());
    }

    public SelectionBehaviour<SulfuricFluxEmitterSelectedPoint> getSelectionBehaviour() {
        return BlockRegistry.SULFURIC_FLUX_EMITTER.get().selectionBehaviour();
    }

    public void setSelectedPoints(List<SulfuricFluxEmitterSelectedPoint> sourcePedestals, SulfuricFluxEmitterSelectedPoint targetPedestal, SulfuricFluxEmitterSelectedPoint resultPedestal) {
        var range = this.getSelectionBehaviour().getBlockRange();

        this.sourcePedestals.clear();
        this.sourcePedestals.addAll(sourcePedestals);
        this.sourcePedestals.forEach(point -> point.setLevel(this.getLevel()));
        this.sourcePedestals.removeIf(p -> !p.getBlockPos().closerThan(this.getBlockPos(), range));

        this.targetPedestal = targetPedestal == null ? null : targetPedestal.getBlockPos().closerThan(this.getBlockPos(), range) ? targetPedestal : null;
        this.resultPedestal = resultPedestal == null ? null : resultPedestal.getBlockPos().closerThan(this.getBlockPos(), range) ? resultPedestal : null;
        if (this.targetPedestal != null) this.targetPedestal.setLevel(this.getLevel());
        if (this.resultPedestal != null) this.resultPedestal.setLevel(this.getLevel());

        this.checkValidMultiblockOnNextQuery = true;

        this.setChanged();
    }

    /**
     * client-side variant that does no checks
     */
    public void setSelectedPointsClient(List<SulfuricFluxEmitterSelectedPoint> sourcePedestals, SulfuricFluxEmitterSelectedPoint targetPedestal, SulfuricFluxEmitterSelectedPoint resultPedestal) {
        this.sourcePedestals.clear();
        this.sourcePedestals.addAll(sourcePedestals);
        this.sourcePedestals.forEach(point -> point.setLevel(this.getLevel()));

        this.targetPedestal = targetPedestal;
        this.resultPedestal = resultPedestal;
        if (this.targetPedestal != null) this.targetPedestal.setLevel(this.getLevel());
        if (this.resultPedestal != null) this.resultPedestal.setLevel(this.getLevel());
    }

    public void onTargetPedestalContentChange(ReformationTargetPedestalBlockEntity pedestal) {
        this.hasTargetItem = !ItemUtil.getStack(pedestal.inputInventory, 0).isEmpty();
    }

    public void onSourcePedestalContentChange(ReformationSourcePedestalBlockEntity pedestal) {
        this.sourcePedestalsWithContents.clear();
        this.hasSourceItems = this.sourcePedestals.stream().map(p -> new Pair<>(p, this.level.getBlockEntity(p.getBlockPos())))
                .filter(p -> p.getSecond() instanceof ReformationSourcePedestalBlockEntity)
                .map(p -> new Pair<>(p.getFirst(), (ReformationSourcePedestalBlockEntity) p.getSecond()))
                .filter(p -> !ItemUtil.getStack(p.getSecond().inputInventory, 0).isEmpty())
                .peek(p -> this.sourcePedestalsWithContents.add(p.getFirst()))
                .count() > 0; //need to count to force the stream to run on all elements. findAny() would only run on one.
        this.setChanged();
    }

    public static class DistHelper {

        static void sendTargetProjectile(SulfuricFluxEmitterBlockEntity emitter) {
            var normal = Vec3.atLowerCornerOf(emitter.getBlockState().getValue(BlockStateProperties.FACING).getUnitVec3i());
            var from = Vec3.atCenterOf(emitter.getBlockPos()).subtract(normal.scale(0.5));
            var to = Vec3.atCenterOf(emitter.targetPedestal.getBlockPos()).add(0, 0.5, 0);

            if (emitter.level.isLoaded(BlockPos.containing(to)) && emitter.level.isLoaded(BlockPos.containing(from)) && emitter.level.isClientSide()) {
                FollowProjectile projectile = new FollowProjectile(emitter.level, from, to, new Color(0xffffff, false), new Color(0x0000ff, false), 0.1f, 0.3f, (targetProjectile) -> {
                    DistHelper.sendSourceProjectiles(targetProjectile, emitter);
                });
                projectile.setDeltaMovement(normal.scale(0.3f));

                EntityUtil.spawnEntityClientSide(emitter.level, projectile, true);
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

                    EntityUtil.spawnEntityClientSide(emitter.level, projectile, true);
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

                EntityUtil.spawnEntityClientSide(emitter.level, projectile, true);
            }
        }
    }

    public class SulfuricFluxEmitterMercuryFluxHandler extends SimpleMercuryFluxHandler {

        public SulfuricFluxEmitterMercuryFluxHandler(int capacity) {
            super(capacity);
        }

        @Override
        protected void onEnergyChanged(int previousAmount) {
            SulfuricFluxEmitterBlockEntity.this.setChanged();
        }
    }
}
