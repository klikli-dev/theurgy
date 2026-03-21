// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import java.util.function.Supplier;

/**
 * A FluidHandler that wraps a list of fluid handlers, and conditionally returns one of them.
 */
public class SelectFluidHandlerWrapper implements ResourceHandler<FluidResource> {

    protected final ResourceHandler<FluidResource>[] handlers;
    protected Supplier<Integer> selector;

    @SafeVarargs
    public SelectFluidHandlerWrapper(Supplier<Integer> selector, ResourceHandler<FluidResource>... handlers) {
        this.handlers = handlers;
        this.selector = selector;
    }

    @Override
    public int size() {
        return this.handlers[this.selector.get()].size();
    }

    @Override
    public FluidResource getResource(int index) {
        return this.handlers[this.selector.get()].getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        return this.handlers[this.selector.get()].getAmountAsLong(index);
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        return this.handlers[this.selector.get()].getCapacityAsLong(index, resource);
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return this.handlers[this.selector.get()].isValid(index, resource);
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
        return this.handlers[this.selector.get()].insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
        return this.handlers[this.selector.get()].extract(index, resource, amount, transaction);
    }
}
