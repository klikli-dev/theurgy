// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter;

import com.klikli_dev.theurgy.content.behaviour.selection.SelectionBehaviour;
import com.klikli_dev.theurgy.content.capability.SimpleMercuryFluxHandler;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageShowCaloricFlux;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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

    public CaloricFluxEmitterMercuryFluxHandler mercuryFluxHandler;

    protected List<CaloricFluxEmitterSelectedPoint> selectedPoints;

    public CaloricFluxEmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CALORIC_FLUX_EMITTER.get(), pPos, pBlockState);

        this.mercuryFluxHandler = new CaloricFluxEmitterMercuryFluxHandler(CAPACITY);

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

        if (this.mercuryFluxHandler.getAmountAsInt() >= FLUX_PER_HEAT) {
            var heatReceiver = Objects.requireNonNull(this.level).getCapability(CapabilityRegistry.HEAT_RECEIVER, selectedPoint.getBlockPos(), selectedPoint.getBlockState(), null, null);

            if (!Objects.requireNonNull(heatReceiver).readyToReceive())
                return;

            if (heatReceiver.getIsHotUntil() > this.getLevel().getGameTime() + TICK_INTERVAL)
                return; //target block is still hot until next tick so do nothing

            try (net.neoforged.neoforge.transfer.transaction.Transaction tx = net.neoforged.neoforge.transfer.transaction.Transaction.openRoot()) {
                this.mercuryFluxHandler.extract(FLUX_PER_HEAT, tx);
                tx.commit();
            }
            heatReceiver.setHotUntil(this.getLevel().getGameTime() + HEAT_TARGET_FOR_TICKS);

            Networking.sendToTracking((ServerLevel) this.getLevel(), ChunkPos.containing(this.getBlockPos()), new MessageShowCaloricFlux(this.getBlockPos(), selectedPoint.getBlockPos(), this.getBlockState().getValue(CaloricFluxEmitterBlock.FACING)));
        }
    }


    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput storageOutput = output.child("mercuryFluxHandler");
        this.mercuryFluxHandler.serialize(storageOutput);
        if (storageOutput.isEmpty()) {
            output.discard("mercuryFluxHandler");
        }

        output.store("selectedPoints", CaloricFluxEmitterSelectedPoint.LIST_CODEC, this.selectedPoints);
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        input.child("mercuryFluxHandler").ifPresent(this.mercuryFluxHandler::deserialize);
        this.selectedPoints = input.read("selectedPoints", CaloricFluxEmitterSelectedPoint.LIST_CODEC).orElseGet(ArrayList::new);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter pComponentInput) {
        super.applyImplicitComponents(pComponentInput);

        if (pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()) != null)
            //noinspection DataFlowIssue
            this.mercuryFluxHandler.setEnergyStored(pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder pComponents) {
        super.collectImplicitComponents(pComponents);

        pComponents.set(DataComponentRegistry.MERCURY_FLUX_STORAGE, this.mercuryFluxHandler.getAmountAsInt());
    }

    public SelectionBehaviour<CaloricFluxEmitterSelectedPoint> getSelectionBehaviour() {
        return BlockRegistry.CALORIC_FLUX_EMITTER.get().selectionBehaviour();
    }

    public void setSelectedPoints(List<CaloricFluxEmitterSelectedPoint> selectedPoints) {
        this.selectedPoints = selectedPoints;
        this.selectedPoints.forEach(point -> point.setLevel(this.getLevel()));
        this.selectedPoints.removeIf(p -> !p.getBlockPos().closerThan(this.getBlockPos(), this.getSelectionBehaviour().getBlockRange()));
        this.setChanged();
    }

    /**
     * client-side variant that does no checks
     */
    public void setSelectedPointsClient(List<CaloricFluxEmitterSelectedPoint> selectedPoints) {
        this.selectedPoints = selectedPoints;
        this.selectedPoints.forEach(point -> point.setLevel(this.getLevel()));
    }

    public class CaloricFluxEmitterMercuryFluxHandler extends SimpleMercuryFluxHandler {

        public CaloricFluxEmitterMercuryFluxHandler(int capacity) {
            super(capacity);
        }

        @Override
        public int insert(int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
            var received = super.insert(amount, transaction);

            if (received > 0) {
                CaloricFluxEmitterBlockEntity.this.setChanged();
            }

            return received;
        }

        @Override
        public int extract(int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
            var extracted = super.extract(amount, transaction);

            if (extracted > 0) {
                CaloricFluxEmitterBlockEntity.this.setChanged();
            }

            return extracted;
        }
    }
}
