// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercuryfluxemitter;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class MercuryFluxEmitterEnergyBehaviour {
    public static final int ENERGY_PER_TRANSFER = MercuryFluxEmitterBlockEntity.FLUX_PER_TRANSFER;
    private final MercuryFluxEmitterBlockEntity blockEntity;

    public MercuryFluxEmitterEnergyBehaviour(MercuryFluxEmitterBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public void tickServer() {
        var level = this.blockEntity.getLevel();
        if (level == null || level.getGameTime() % MercuryFluxEmitterBlockEntity.TICK_INTERVAL != 0) return;
        if (!this.blockEntity.getBlockState().getValue(BlockStateProperties.ENABLED)) return;
        if (this.blockEntity.selectedPoints.isEmpty()) return;
        var selectedPoint = this.blockEntity.selectedPoints.getFirst();
        if (!this.blockEntity.getSelectionBehaviour().isValid(selectedPoint)) return;
        if (this.blockEntity.energyStorage.getAmountAsInt() < ENERGY_PER_TRANSFER) return;
        int amount = ENERGY_PER_TRANSFER;
        EnergyHandler target = level.getCapability(Capabilities.Energy.BLOCK, selectedPoint.getBlockPos(), selectedPoint.getBlockState(), null, null);
        if (target == null) return;
        try (Transaction tx = Transaction.openRoot()) {
            int extracted = this.blockEntity.energyStorage.extract(amount, tx);
            if (extracted <= 0) return;
            int inserted = target.insert(extracted, tx);
            if (inserted <= 0) return;
            int remainder = extracted - inserted;
            if (remainder > 0 && this.blockEntity.energyStorage.insert(remainder, tx) != remainder) return;
            tx.commit();
        }
    }
}
