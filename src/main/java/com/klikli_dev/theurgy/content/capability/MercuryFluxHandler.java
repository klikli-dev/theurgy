// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.capability;

import com.google.common.primitives.Ints;

/**
 * Copy of EnergyHandler, separate to prevent conversion to/from FE.
 * 
 * Mirrors the NeoForge EnergyHandler interface.
 * Keeps the legacy API (receiveEnergy, extractEnergy) for compatibility while also
 * providing the new API (insert, extract, getAmount, getCapacity).
 */
public interface MercuryFluxHandler {
    /**
     * Returns the amount of energy currently stored, as a {@code long}.
     *
     * <p>The returned amount must be <strong>non-negative</strong>.
     *
     * @return the amount as a long
     * @see #getAmountAsInt()
     */
    long getAmountAsLong();

    /**
     * Returns the amount of energy currently stored, as an {@code int}.
     *
     * <p>This is a convenience method to clamp the amount to an {@code int}.
     *
     * @return the amount as an {@code int}
     */
    default int getAmountAsInt() {
        return Ints.saturatedCast(getAmountAsLong());
    }

    /**
     * Returns the capacity of the handler, irrespective of the current amount, as a {@code long}.
     *
     * <p>This function serves as a hint on the maximum amount the energy handler might contain,
     * for example the handler can be considered full if {@code amount >= capacity}.
     *
     * @return the capacity, as a long
     * @see #getCapacityAsInt()
     */
    long getCapacityAsLong();

    /**
     * Returns the capacity of the handler, irrespective of the current amount, as an {@code int}.
     *
     * <p>This is a convenience method to get the capacity clamped to an {@code int}.
     *
     * @return the capacity, as an {@code int}
     */
    default int getCapacityAsInt() {
        return Ints.saturatedCast(getCapacityAsLong());
    }

    /**
     * Inserts up to the given amount of energy into the handler.
     *
     * @param amount The maximum amount of energy to insert. <strong>Must be non-negative.</strong>
     * @return The amount that was inserted. Between {@code 0} (inclusive) and {@code amount} (inclusive).
     */
    default int insert(int amount) {
        return receiveEnergy(amount, false);
    }

    /**
     * Extracts up to the given amount of energy from the handler.
     *
     * @param amount The maximum amount of energy to extract. <strong>Must be non-negative.</strong>
     * @return The amount that was extracted. Between {@code 0} (inclusive) and {@code amount} (inclusive).
     */
    default int extract(int amount) {
        return extractEnergy(amount, false);
    }

    // Legacy API - implementations should implement these
    
    /**
     * Adds energy to the storage. Returns quantity of energy that was accepted.
     *
     * @param maxReceive Maximum amount of energy to be inserted.
     * @param simulate   If TRUE, the insertion will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) accepted by the storage.
     */
    int receiveEnergy(int maxReceive, boolean simulate);

    /**
     * Removes energy from the storage. Returns quantity of energy that was removed.
     *
     * @param maxExtract Maximum amount of energy to be extracted.
     * @param simulate   If TRUE, the extraction will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) extracted from the storage.
     */
    int extractEnergy(int maxExtract, boolean simulate);

    /**
     * Returns the amount of energy currently stored.
     */
    default int getEnergyStored() {
        return getAmountAsInt();
    }

    /**
     * Sets the amount of energy stored.
     */
    default void setEnergyStored(int energy) {
        throw new UnsupportedOperationException("Default implementation does not support setEnergyStored");
    }

    /**
     * Returns the maximum amount of energy that can be stored.
     */
    default int getMaxEnergyStored() {
        return getCapacityAsInt();
    }
}
