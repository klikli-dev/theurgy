// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class MonitoredFluidTank extends FluidTank {
    public MonitoredFluidTank(int capacity) {
        super(capacity);
    }

    public MonitoredFluidTank(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }

    /**
     * Called when the content type of the tank changes, that means, a different Fluid, or a change between Empty and Non-Empty.
     * Mere changes of fluid amount do not trigger this.
     */
    protected void onContentTypeChanged(FluidStack oldStack, FluidStack newStack) {

    }

    protected void onSetFluid(FluidStack oldStack, FluidStack newStack, boolean isSameFluid) {

    }
    protected void onFill(FluidStack oldStack, FluidStack newStack, FluidStack toInsert, int accepted, int remainingInSource) {

    }

    protected void onDrain(FluidStack oldStack, FluidStack newStack, FluidStack extracted) {

    }

    @Override
    public int fill(FluidStack toInsert, FluidAction action) {
        if (action != FluidAction.SIMULATE) {
            var oldStack = this.getFluid().copy();

            var accepted = super.fill(toInsert, action);

            var newStack = this.getFluid();

            this.onFill(oldStack, newStack, toInsert, accepted, toInsert.getAmount() - accepted);

            if (oldStack.getFluid() != newStack.getFluid() || oldStack.isEmpty() != newStack.isEmpty()) {
                this.onContentTypeChanged(oldStack, newStack);
            }

            return accepted;
        }
        return super.fill(toInsert, action);
    }

    //amount of fluid removed
    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        if (action != FluidAction.SIMULATE) {
            var oldStack = this.getFluid().copy();
            var extracted =  super.drain(resource, action);
            var newStack = this.getFluid();

            this.onDrain(oldStack, newStack, extracted);

            if (oldStack.getFluid() != newStack.getFluid() || oldStack.isEmpty() != newStack.isEmpty()) {
                this.onContentTypeChanged(oldStack, newStack);
            }

            return extracted;
        }
        return super.drain(resource, action);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        if (action != FluidAction.SIMULATE) {
            var oldStack = this.getFluid().copy();
            var extracted =  super.drain(maxDrain, action);
            var newStack = this.getFluid();

            this.onDrain(oldStack, newStack, extracted);

            if (oldStack.getFluid() != newStack.getFluid() || oldStack.isEmpty() != newStack.isEmpty()) {
                this.onContentTypeChanged(oldStack, newStack);
            }

            return extracted;
        }
        return super.drain(maxDrain, action);
    }

    @Override
    public void setFluid(FluidStack newStack) {
        var oldStack = this.getFluid();

        boolean sameFluid = newStack.isFluidEqual(oldStack);

        super.setFluid(newStack);

        this.onSetFluid(oldStack, newStack, sameFluid);
        if(!sameFluid || oldStack.isEmpty() != newStack.isEmpty()){
            this.onContentTypeChanged(oldStack, newStack);
        }
    }
}
