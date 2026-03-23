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

    public static int fill(@Nullable ResourceHandler<FluidResource> handler, FluidStack stack, boolean simulate) {
        if (handler == null || stack.isEmpty()) {
            return 0;
        }

        try (var tx = Transaction.openRoot()) {
            int inserted = handler.insert(FluidResource.of(stack), stack.getAmount(), tx);
            if (!simulate && inserted > 0) {
                tx.commit();
            }
            return inserted;
        }
    }

    public static FluidStack drain(@Nullable ResourceHandler<FluidResource> handler, FluidStack stack, boolean simulate) {
        if (handler == null || stack.isEmpty()) {
            return FluidStack.EMPTY;
        }

        var resource = FluidResource.of(stack);
        try (var tx = Transaction.openRoot()) {
            int extracted = handler.extract(resource, stack.getAmount(), tx);
            if (!simulate && extracted > 0) {
                tx.commit();
            }
            return extracted == 0 ? FluidStack.EMPTY : resource.toStack(extracted);
        }
    }

    public static FluidStack drain(@Nullable ResourceHandler<FluidResource> handler, int maxDrain, boolean simulate) {
        if (handler == null || maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        try (var tx = Transaction.openRoot()) {
            var extracted = ResourceHandlerUtil.extractFirst(handler, resource -> true, maxDrain, tx);
            if (extracted == null || extracted.amount() <= 0) {
                return FluidStack.EMPTY;
            }

            if (!simulate) {
                tx.commit();
            }
            return extracted.resource().toStack(extracted.amount());
        }
    }
}

