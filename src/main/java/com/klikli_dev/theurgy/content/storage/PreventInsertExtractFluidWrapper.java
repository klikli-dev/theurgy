// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public class PreventInsertExtractFluidWrapper implements ResourceHandler<FluidResource> {
    protected final ResourceHandler<FluidResource> compose;

    public PreventInsertExtractFluidWrapper(ResourceHandler<FluidResource> compose) {
        this.compose = compose;
    }

    @Override
    public int size() {
        return this.compose.size();
    }

    @Override
    public FluidResource getResource(int index) {
        return this.compose.getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        return this.compose.getAmountAsLong(index);
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        return this.compose.getCapacityAsLong(index, resource);
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return false;
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
        return 0;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
        return 0;
    }
}
