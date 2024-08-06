package com.klikli_dev.theurgy.content.storage;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * A FluidHandler that wraps a list of fluid handlers, and conditionally returns one of them.
 */
public class SelectFluidHandlerWrapper implements IFluidHandler {

    protected final IFluidHandler[] handlers;
    protected Supplier<Integer> selector;

    public SelectFluidHandlerWrapper(Supplier<Integer> selector, IFluidHandler... handlers) {
        this.handlers = handlers;
        this.selector = selector;
    }

    @Override
    public int getTanks() {
        return this.handlers[this.selector.get()].getTanks();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return this.handlers[this.selector.get()].getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.handlers[this.selector.get()].getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return this.handlers[this.selector.get()].isFluidValid(tank, stack);
    }

    @Override
    public int fill(@NotNull FluidStack resource, @NotNull FluidAction action) {
        return this.handlers[this.selector.get()].fill(resource, action);
    }

    @Override
    public @NotNull FluidStack drain(@NotNull FluidStack resource, @NotNull FluidAction action) {
        return this.handlers[this.selector.get()].drain(resource, action);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, @NotNull FluidAction action) {
        return this.handlers[this.selector.get()].drain(maxDrain, action);
    }
}
