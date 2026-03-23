// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class MonitoredFluidTank extends FluidStacksResourceHandler {
    protected final Predicate<FluidStack> validator;

    public MonitoredFluidTank(int capacity) {
        this(capacity, e -> true);
    }

    public MonitoredFluidTank(int capacity, Predicate<FluidStack> validator) {
        super(1, capacity);
        this.validator = validator;
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

    protected void onContentsChanged() {

    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return resource.isEmpty() || this.isFluidValid(resource.toStack(FluidType.BUCKET_VOLUME));
    }

    @Override
    protected void onContentsChanged(int index, FluidStack previousContents) {
        this.onContentsChanged();
    }

    @Override
    public void deserialize(ValueInput input) {
        this.setFluid(input.read("Fluid", FluidStack.CODEC).orElse(FluidStack.EMPTY));
    }

    @Override
    public void serialize(ValueOutput output) {
        if (!this.isEmpty()) {
            output.store("Fluid", FluidStack.CODEC, this.getFluid());
        }
    }

    public boolean isFluidValid(FluidStack stack) {
        return this.validator.test(stack);
    }

    public int getCapacity() {
        return this.capacity;
    }

    public FluidStack getFluid() {
        return FluidStorageHelper.getFluidInTank(this, 0);
    }

    public void setFluid(FluidStack newStack) {
        var oldStack = this.getFluid().copy();

        boolean sameFluid = FluidStack.isSameFluidSameComponents(newStack, oldStack);

        super.set(0, FluidResource.of(newStack), newStack.getAmount());

        this.onSetFluid(oldStack, newStack, sameFluid);
        if (!sameFluid || oldStack.isEmpty() != newStack.isEmpty()) {
            this.onContentTypeChanged(oldStack, newStack);
        }
    }

    public int getFluidAmount() {
        return this.getAmountAsInt(0);
    }

    public int fill(FluidStack toInsert, boolean simulate) {
        if (!simulate) {
            var oldStack = this.getFluid().copy();
            var accepted = FluidStorageHelper.fill(this, toInsert, false);
            var newStack = this.getFluid();

            this.onFill(oldStack, newStack, toInsert, accepted, toInsert.getAmount() - accepted);

            if (oldStack.getFluid() != newStack.getFluid() || oldStack.isEmpty() != newStack.isEmpty()) {
                this.onContentTypeChanged(oldStack, newStack);
            }

            return accepted;
        }

        return FluidStorageHelper.fill(this, toInsert, true);
    }

    //amount of fluid removed
    public @NotNull FluidStack drain(FluidStack resource, boolean simulate) {
        if (!simulate) {
            var oldStack = this.getFluid().copy();
            var extracted = FluidStorageHelper.drain(this, resource, false);
            var newStack = this.getFluid();

            this.onDrain(oldStack, newStack, extracted);

            if (oldStack.getFluid() != newStack.getFluid() || oldStack.isEmpty() != newStack.isEmpty()) {
                this.onContentTypeChanged(oldStack, newStack);
            }

            return extracted;
        }

        return FluidStorageHelper.drain(this, resource, true);
    }

    public @NotNull FluidStack drain(int maxDrain, boolean simulate) {
        if (!simulate) {
            var oldStack = this.getFluid().copy();
            var extracted = FluidStorageHelper.drain(this, maxDrain, false);
            var newStack = this.getFluid();

            this.onDrain(oldStack, newStack, extracted);

            if (oldStack.getFluid() != newStack.getFluid() || oldStack.isEmpty() != newStack.isEmpty()) {
                this.onContentTypeChanged(oldStack, newStack);
            }

            return extracted;
        }

        return FluidStorageHelper.drain(this, maxDrain, true);
    }

    public boolean isEmpty() {
        return this.getFluid().isEmpty();
    }

    public int getSpace() {
        return Math.max(0, this.capacity - this.getFluidAmount());
    }
}
