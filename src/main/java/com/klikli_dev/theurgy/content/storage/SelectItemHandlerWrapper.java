package com.klikli_dev.theurgy.content.storage;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * An ItemHandler that wraps a list of item handlers, and conditionally returns one of them.
 */
public class SelectItemHandlerWrapper implements IItemHandlerModifiable {

    protected final IItemHandlerModifiable[] handlers;
    protected Supplier<Integer> selector;

    public SelectItemHandlerWrapper(Supplier<Integer> selector, IItemHandlerModifiable... handlers) {
        this.handlers = handlers;
        this.selector = selector;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.handlers[this.selector.get()].setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return this.handlers[this.selector.get()].getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return this.handlers[this.selector.get()].getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return this.handlers[this.selector.get()].insertItem(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.handlers[this.selector.get()].extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.handlers[this.selector.get()].getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.handlers[this.selector.get()].isItemValid(slot, stack);
    }
}
