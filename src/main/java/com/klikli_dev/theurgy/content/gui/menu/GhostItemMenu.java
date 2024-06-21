// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public abstract class GhostItemMenu<T> extends MenuBase<T> implements IClearableMenu {

    public IItemHandlerModifiable ghostInventory;

    protected GhostItemMenu(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    protected GhostItemMenu(MenuType<?> type, int id, Inventory inv, T contentHolder) {
        super(type, id, inv, contentHolder);
    }

    protected abstract IItemHandlerModifiable createGhostInventory();

    protected abstract boolean allowRepeats();

    @Override
    protected void initAndReadInventory(T contentHolder) {
        this.ghostInventory = this.createGhostInventory();
    }

    @Override
    public void clearContents() {
        for (int i = 0; i < this.ghostInventory.getSlots(); i++)
            this.ghostInventory.setStackInSlot(i, ItemStack.EMPTY);
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, Slot slotIn) {
        return slotIn.container == this.playerInventory;
    }

    @Override
    public boolean canDragTo(@NotNull Slot slotIn) {
        if (this.allowRepeats())
            return true;
        return slotIn.container == this.playerInventory;
    }

    @Override
    public void clicked(int slotId, int dragType, @NotNull ClickType clickTypeIn, @NotNull Player player) {
        if (slotId < 36) {
            super.clicked(slotId, dragType, clickTypeIn, player);
            return;
        }
        if (clickTypeIn == ClickType.THROW)
            return;

        ItemStack held = this.getCarried();
        int slot = slotId - 36;
        if (clickTypeIn == ClickType.CLONE) {
            if (player.isCreative() && held.isEmpty()) {
                ItemStack stackInSlot = this.ghostInventory.getStackInSlot(slot)
                        .copy();
                stackInSlot.setCount(stackInSlot.getMaxStackSize());
                this.setCarried(stackInSlot);
                return;
            }
            return;
        }

        ItemStack insert;
        if (held.isEmpty()) {
            insert = ItemStack.EMPTY;
        } else {
            insert = held.copy();
            insert.setCount(1);
        }
        this.ghostInventory.setStackInSlot(slot, insert);
        this.getSlot(slotId).setChanged();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        if (index < 36) {
            ItemStack stackToInsert = this.playerInventory.getItem(index);
            for (int i = 0; i < this.ghostInventory.getSlots(); i++) {
                ItemStack stack = this.ghostInventory.getStackInSlot(i);
                if (!this.allowRepeats() && ItemStack.isSameItemSameComponents(stack, stackToInsert))
                    break;
                if (stack.isEmpty()) {
                    ItemStack copy = stackToInsert.copy();
                    copy.setCount(1);
                    this.ghostInventory.insertItem(i, copy, false);
                    this.getSlot(i + 36).setChanged();
                    break;
                }
            }
        } else {
            this.ghostInventory.extractItem(index - 36, 1, false);
            this.getSlot(index).setChanged();
        }
        return ItemStack.EMPTY;
    }

}
