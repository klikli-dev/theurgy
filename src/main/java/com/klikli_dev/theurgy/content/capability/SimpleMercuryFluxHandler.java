// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.capability;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;


/**
 * Copy of SimpleEnergyHandler, separate to prevent conversion to/from FE.
 */
public class SimpleMercuryFluxHandler implements MercuryFluxHandler, ValueIOSerializable {
    protected int energy;
    protected int capacity;
    protected int maxInsert;
    protected int maxExtract;

    private final EnergyJournal energyJournal = new EnergyJournal();

    public SimpleMercuryFluxHandler(int capacity) {
        this(capacity, capacity);
    }

    public SimpleMercuryFluxHandler(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public SimpleMercuryFluxHandler(int capacity, int maxInsert, int maxExtract) {
        this(capacity, maxInsert, maxExtract, 0);
    }

    public SimpleMercuryFluxHandler(int capacity, int maxInsert, int maxExtract, int energy) {
        TransferPreconditions.checkNonNegative(capacity);
        TransferPreconditions.checkNonNegative(maxInsert);
        TransferPreconditions.checkNonNegative(maxExtract);
        TransferPreconditions.checkNonNegative(energy);

        this.capacity = capacity;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
        this.energy = energy;
    }

    @Override
    public void serialize(ValueOutput output) {
        output.putInt("energy", energy);
    }

    @Override
    public void deserialize(ValueInput input) {
        energy = Math.max(0, input.getIntOr("energy", 0));
    }

    public void set(int amount) {
        TransferPreconditions.checkNonNegative(amount);

        if (this.energy != amount) {
            int previousAmount = this.energy;
            this.energy = amount;
            onEnergyChanged(previousAmount);
        }
    }

    protected void onEnergyChanged(int previousAmount) {}

    @Override
    public long getAmountAsLong() {
        return this.energy;
    }

    @Override
    public long getCapacityAsLong() {
        return this.capacity;
    }

    @Override
    public int insert(int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonNegative(amount);

        int inserted = Math.min(capacity - energy, Math.min(amount, maxInsert));
        if (inserted > 0) {
            energyJournal.updateSnapshots(transaction);
            energy += inserted;
            return inserted;
        }

        return 0;
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonNegative(amount);

        int extracted = Math.min(energy, Math.min(amount, maxExtract));
        if (extracted > 0) {
            energyJournal.updateSnapshots(transaction);
            energy -= extracted;
            return extracted;
        }

        return 0;
    }

    private class EnergyJournal extends SnapshotJournal<Integer> {
        @Override
        protected Integer createSnapshot() {
            return energy;
        }

        @Override
        protected void revertToSnapshot(Integer snapshot) {
            energy = snapshot;
        }

        @Override
        protected void onRootCommit(Integer originalState) {
            int previousAmount = originalState;
            if (energy != previousAmount) {
                onEnergyChanged(previousAmount);
            }
        }
    }
}
