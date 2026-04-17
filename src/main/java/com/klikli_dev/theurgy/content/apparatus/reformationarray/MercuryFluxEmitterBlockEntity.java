// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.behaviour.selection.SelectionBehaviour;
import com.klikli_dev.theurgy.content.capability.SimpleMercuryFluxHandler;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageShowMercuryFlux;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.BlockRegistry;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
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
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class MercuryFluxEmitterBlockEntity extends BlockEntity {

    public static final int CAPACITY = 1000;
    public static final int FLUX_PER_TRANSFER = 10;
    public static final int TICK_INTERVAL = 20;

    public MercuryFluxEmitterMercuryFluxHandler mercuryFluxHandler;

    protected List<MercuryFluxEmitterSelectedPoint> selectedPoints;

public MercuryFluxEmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.MERCURY_FLUX_EMITTER.get(), pPos, pBlockState);

        this.mercuryFluxHandler = new MercuryFluxEmitterMercuryFluxHandler(CAPACITY);

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

        // Transfer to target if we have flux
        if (this.mercuryFluxHandler.getAmountAsInt() >= FLUX_PER_TRANSFER) {
            var targetPos = selectedPoint.getBlockPos();
            var targetState = selectedPoint.getBlockState();

            var targetFluxHandler = Objects.requireNonNull(this.level).getCapability(
                    CapabilityRegistry.MERCURY_FLUX_HANDLER,
                    targetPos,
                    targetState,
                    null,
                    null
            );

            if (targetFluxHandler == null)
                return;

            int accepted = Math.min(FLUX_PER_TRANSFER, targetFluxHandler.getCapacityAsInt() - targetFluxHandler.getAmountAsInt());
            if (accepted <= 0)
                return;

            try (net.neoforged.neoforge.transfer.transaction.Transaction tx = net.neoforged.neoforge.transfer.transaction.Transaction.openRoot()) {
                int extracted = this.mercuryFluxHandler.extract(accepted, tx);
                if (extracted > 0) {
                    int inserted = targetFluxHandler.insert(extracted, tx);
                    if (inserted <= 0) {
                        // nothing accepted, abort
                        return;
                    }
                    // commit the transfer
                    tx.commit();

                    Networking.sendToTracking((ServerLevel) this.getLevel(), ChunkPos.containing(this.getBlockPos()), new MessageShowMercuryFlux(this.getBlockPos(), selectedPoint.getBlockPos(), this.getBlockState().getValue(MercuryFluxEmitterBlock.FACING)));
                }
            }
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

        output.store("selectedPoints", MercuryFluxEmitterSelectedPoint.LIST_CODEC, this.selectedPoints);
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        input.child("mercuryFluxHandler").ifPresent(this.mercuryFluxHandler::deserialize);
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
        this.selectedPoints.forEach(point -> point.setLevel(this.getLevel()));
    }

    public void writeNetwork(ValueOutput output) {
        output.store("selectedPoints", MercuryFluxEmitterSelectedPoint.LIST_CODEC, this.selectedPoints);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter pComponentInput) {
        super.applyImplicitComponents(pComponentInput);

        if (pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()) != null) {
            this.mercuryFluxHandler.setEnergyStored(pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()));
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
        public int insert(int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
            var received = super.insert(amount, transaction);

            if (received > 0) {
                MercuryFluxEmitterBlockEntity.this.setChanged();
            }

            return received;
        }

        @Override
        public int extract(int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
            var extracted = super.extract(amount, transaction);

            if (extracted > 0) {
                MercuryFluxEmitterBlockEntity.this.setChanged();
            }

            return extracted;
        }
    }
}
