package com.klikli_dev.theurgy.content.behaviour;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class PreventInsertExtractFluidWrapper implements IFluidHandler {
    protected final IFluidHandler compose;

    public PreventInsertExtractFluidWrapper(IFluidHandler compose) {
        this.compose = compose;
    }

    @Override
    public int getTanks() {
        return this.compose.getTanks();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return this.compose.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.compose.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        return FluidStack.EMPTY;
    }
}
