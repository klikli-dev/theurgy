// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercuryfluxemitter;

import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageShowMercuryFlux;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class MercuryFluxEmitterMercuryFluxBehaviour {
    private final MercuryFluxEmitterBlockEntity blockEntity;
    public MercuryFluxEmitterMercuryFluxBehaviour(MercuryFluxEmitterBlockEntity blockEntity) { this.blockEntity = blockEntity; }
    public void tickServer() {
        var level = this.blockEntity.getLevel(); if (level == null || level.getGameTime() % MercuryFluxEmitterBlockEntity.TICK_INTERVAL != 0) return; if (!this.blockEntity.getBlockState().getValue(BlockStateProperties.ENABLED)) return; if (this.blockEntity.selectedPoints.isEmpty()) return; var selectedPoint = this.blockEntity.selectedPoints.getFirst(); if (!this.blockEntity.getSelectionBehaviour().isValid(selectedPoint)) return; if (this.blockEntity.mercuryFluxHandler.getAmountAsInt() < MercuryFluxEmitterBlockEntity.FLUX_PER_TRANSFER) return; var target = level.getCapability(CapabilityRegistry.MERCURY_FLUX_HANDLER, selectedPoint.getBlockPos(), selectedPoint.getBlockState(), null, null); if (target == null) return; try (Transaction tx = Transaction.openRoot()) { int extracted = this.blockEntity.mercuryFluxHandler.extract(MercuryFluxEmitterBlockEntity.FLUX_PER_TRANSFER, tx); if (extracted <= 0) return; int inserted = target.insert(extracted, tx); if (inserted <= 0) return; int remainder = extracted - inserted; if (remainder > 0 && this.blockEntity.mercuryFluxHandler.insert(remainder, tx) != remainder) return; tx.commit(); if (level instanceof ServerLevel serverLevel) { Networking.sendToTracking(serverLevel, ChunkPos.containing(this.blockEntity.getBlockPos()), new MessageShowMercuryFlux(this.blockEntity.getBlockPos(), selectedPoint.getBlockPos(), this.blockEntity.getBlockState().getValue(MercuryFluxEmitterBlock.FACING))); } } }
}
