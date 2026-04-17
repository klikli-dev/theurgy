// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.capability;

import com.klikli_dev.theurgy.util.NBTSerializable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;


/**
 * Copy of EnergyStorage, separate to prevent conversion to/from FE
 */
public class SimpleMercuryFluxHandler implements MercuryFluxHandler, NBTSerializable<Tag>, ValueIOSerializable {
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public SimpleMercuryFluxHandler(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public SimpleMercuryFluxHandler(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public SimpleMercuryFluxHandler(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public SimpleMercuryFluxHandler(int capacity, int maxReceive, int maxExtract, int energy) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0, Math.min(capacity, energy));
    }

    public int insert(int amount) {
        if (amount <= 0 || !this.canReceive()) return 0;

        int energyInserted = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, amount));
        int previous = this.energy;
        this.energy += energyInserted;
        this.onEnergyChanged(previous);
        return energyInserted;
    }

    public int extract(int amount) {
        if (amount <= 0 || !this.canExtract()) return 0;

        int energyExtracted = Math.min(this.energy, Math.min(this.maxExtract, amount));
        int previous = this.energy;
        this.energy -= energyExtracted;
        this.onEnergyChanged(previous);
        return energyExtracted;
    }

    // Transaction-aware variants (interface methods)
    @Override
    public int insert(int amount, TransactionContext transaction) {
        // Simple implementation that ignores transactional snapshots and behaves eagerly.
        // We keep the old behavior to minimize changes; callers will open transactions around multi-step flows.
        return this.insert(amount);
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        return this.extract(amount);
    }

    /**
     * NOTE: simulation helpers removed. Callers should use getAmountAsInt()/getCapacityAsInt()
     * and account for transfer limits themselves when necessary.
     */

    public int getEnergyStored() {
        return this.energy;
    }

    public void setEnergyStored(int energy) {
        this.energy = Math.max(0, Math.min(this.capacity, energy));
    }

    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public long getAmountAsLong() {
        return this.energy;
    }

    @Override
    public long getCapacityAsLong() {
        return this.capacity;
    }

    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    /**
     * Hook invoked when energy changes. Subclasses may override to react (e.g. mark BE changed).
     * The parameter is the previous amount before the change.
     */
    protected void onEnergyChanged(int previousAmount) {
        // default: no-op
    }

    @Override
    public Tag serializeNBT(HolderLookup.Provider pRegistries) {
        return IntTag.valueOf(this.getEnergyStored());
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider pRegistries, Tag nbt) {
        if (!(nbt instanceof IntTag(int value)))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.energy = value;
    }

    @Override
    public void serialize(ValueOutput output) {
        output.putInt("energy", this.energy);
        output.putInt("capacity", this.capacity);
        output.putInt("maxReceive", this.maxReceive);
        output.putInt("maxExtract", this.maxExtract);
    }

    @Override
    public void deserialize(ValueInput input) {
        this.capacity = input.getIntOr("capacity", this.capacity);
        this.maxReceive = input.getIntOr("maxReceive", this.maxReceive);
        this.maxExtract = input.getIntOr("maxExtract", this.maxExtract);
        this.energy = Math.max(0, Math.min(this.capacity, input.getIntOr("energy", 0)));
    }
}
