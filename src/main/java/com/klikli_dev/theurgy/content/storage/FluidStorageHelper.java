// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;

public final class FluidStorageHelper {
    private FluidStorageHelper() {
    }

    public static int getTanks(ResourceHandler<FluidResource> handler) {
        return handler.size();
    }

    public static FluidStack getFluidInTank(ResourceHandler<FluidResource> handler, int tank) {
        return FluidUtil.getStack(handler, tank);
    }

    public static int getTankCapacity(ResourceHandler<FluidResource> handler, int tank) {
        return handler.getCapacityAsInt(tank, FluidResource.EMPTY);
    }

    public static boolean isFluidValid(ResourceHandler<FluidResource> handler, int tank, FluidStack stack) {
        return stack.isEmpty() || handler.isValid(tank, FluidResource.of(stack));
    }

    /**
     * Inserts the given stack into the handler.
     *
     * @param transaction The transaction context for the operation. Passing in {@code null} will open a root
     *                    transaction that is committed at the end of this method. Passing in a transaction allows the
     *                    caller to decide whether to commit or abort the operation, and to simulate by never committing.
     * @return the amount that was inserted
     */
    public static int fill(@Nullable ResourceHandler<FluidResource> handler, FluidStack stack, @Nullable TransactionContext transaction) {
        if (handler == null || stack.isEmpty()) {
            return 0;
        }

        try (var tx = Transaction.open(transaction)) {
            int inserted = handler.insert(FluidResource.of(stack), stack.getAmount(), tx);
            tx.commit();
            return inserted;
        }
    }

    /**
     * Extracts the given stack from the handler.
     *
     * @param transaction The transaction context for the operation. Passing in {@code null} will open a root
     *                    transaction that is committed at the end of this method. Passing in a transaction allows the
     *                    caller to decide whether to commit or abort the operation, and to simulate by never committing.
     * @return the fluid that was extracted
     */
    public static FluidStack drain(@Nullable ResourceHandler<FluidResource> handler, FluidStack stack, @Nullable TransactionContext transaction) {
        if (handler == null || stack.isEmpty()) {
            return FluidStack.EMPTY;
        }

        var resource = FluidResource.of(stack);
        try (var tx = Transaction.open(transaction)) {
            int extracted = handler.extract(resource, stack.getAmount(), tx);
            if (extracted <= 0) {
                return FluidStack.EMPTY;
            }

            tx.commit();
            return resource.toStack(extracted);
        }
    }

    /**
     * Extracts up to the given amount of the first available fluid from the handler.
     *
     * @param transaction The transaction context for the operation. Passing in {@code null} will open a root
     *                    transaction that is committed at the end of this method. Passing in a transaction allows the
     *                    caller to decide whether to commit or abort the operation, and to simulate by never committing.
     * @return the fluid that was extracted
     */
    public static FluidStack drain(@Nullable ResourceHandler<FluidResource> handler, int maxDrain, @Nullable TransactionContext transaction) {
        if (handler == null || maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        var extracted = ResourceHandlerUtil.extractFirst(handler, resource -> true, maxDrain, transaction);
        return extracted == null ? FluidStack.EMPTY : extracted.resource().toStack(extracted.amount());
    }
}
