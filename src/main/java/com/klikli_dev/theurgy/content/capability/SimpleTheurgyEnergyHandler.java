// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.capability;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class SimpleTheurgyEnergyHandler implements EnergyHandler, ValueIOSerializable {
    protected int energy;
    protected int capacity;
    protected int maxInsert;
    protected int maxExtract;

    private final EnergyJournal energyJournal = new EnergyJournal();

    public SimpleTheurgyEnergyHandler(int capacity) {
        this(capacity, capacity);
    }

    public SimpleTheurgyEnergyHandler(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public SimpleTheurgyEnergyHandler(int capacity, int maxInsert, int maxExtract) {
        this(capacity, maxInsert, maxExtract, 0);
    }

    public SimpleTheurgyEnergyHandler(int capacity, int maxInsert, int maxExtract, int energy) {
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
        output.putInt("energy", this.energy);
    }

    @Override
    public void deserialize(ValueInput input) {
        this.energy = Math.max(0, input.getIntOr("energy", 0));
    }

    public void set(int amount) {
        TransferPreconditions.checkNonNegative(amount);

        if (this.energy != amount) {
            int previousAmount = this.energy;
            this.energy = amount;
            this.onEnergyChanged(previousAmount);
        }
    }

    protected void onEnergyChanged(int previousAmount) {
    }

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

        int inserted = Math.min(this.capacity - this.energy, Math.min(amount, this.maxInsert));
        if (inserted > 0) {
            this.energyJournal.updateSnapshots(transaction);
            this.energy += inserted;
            return inserted;
        }

        return 0;
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonNegative(amount);

        int extracted = Math.min(this.energy, Math.min(amount, this.maxExtract));
        if (extracted > 0) {
            this.energyJournal.updateSnapshots(transaction);
            this.energy -= extracted;
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
