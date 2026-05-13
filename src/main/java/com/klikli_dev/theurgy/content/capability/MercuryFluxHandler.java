// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.capability;

import com.google.common.primitives.Ints;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/**
 * Copy of EnergyHandler, separate to prevent conversion to/from FE.
 * <p>
 * Mirrors the NeoForge EnergyHandler interface with long-based amount/capacity
 * and transaction-based insert/extract methods.
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
        return Ints.saturatedCast(this.getAmountAsLong());
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
        return Ints.saturatedCast(this.getCapacityAsLong());
    }

    /**
     * Inserts up to the given amount of energy into the handler.
     *
     * <p>Changes to the handler are made in the context of a transaction.
     *
     * @param amount      The maximum amount of energy to insert. <strong>Must be non-negative.</strong>
     * @param transaction The transaction that this operation is part of.
     * @return The amount that was inserted. Between {@code 0} (inclusive) and {@code amount} (inclusive).
     */
    int insert(int amount, TransactionContext transaction);

    /**
     * Extracts up to the given amount of energy from the handler.
     *
     * <p>Changes to the handler are made in the context of a transaction.
     *
     * @param amount      The maximum amount of energy to extract. <strong>Must be non-negative.</strong>
     * @param transaction The transaction that this operation is part of.
     * @return The amount that was extracted. Between {@code 0} (inclusive) and {@code amount} (inclusive).
     */
    int extract(int amount, TransactionContext transaction);
}
