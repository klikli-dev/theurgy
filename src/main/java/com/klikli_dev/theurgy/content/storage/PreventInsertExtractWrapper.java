// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.storage;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

/**
 * Wrapper for IItemHandlerModifiable that prevents insertion of items.
 */
public class PreventInsertExtractWrapper implements IItemHandlerModifiable {
    protected final IItemHandlerModifiable compose;

    public PreventInsertExtractWrapper(IItemHandlerModifiable compose) {
        this.compose = compose;
    }

    @Override
    public int getSlots() {
        return this.compose.getSlots();
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(int slot) {
        return this.compose.getStackInSlot(slot);
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.compose.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.compose.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }

}
