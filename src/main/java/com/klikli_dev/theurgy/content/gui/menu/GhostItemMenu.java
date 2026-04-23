// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui.menu;

import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;

public abstract class GhostItemMenu<T> extends MenuBase<T> implements IClearableMenu {

    public SettableItemStorage ghostInventory;

    protected GhostItemMenu(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    protected GhostItemMenu(MenuType<?> type, int id, Inventory inv, T contentHolder) {
        super(type, id, inv, contentHolder);
    }

    protected abstract SettableItemStorage createGhostInventory();

    protected abstract boolean allowRepeats();

    @Override
    protected void initAndReadInventory(T contentHolder) {
        this.ghostInventory = this.createGhostInventory();
    }

    @Override
    public void clearContents() {
        for (int i = 0; i < this.ghostInventory.size(); i++)
            this.ghostInventory.set(i, ItemResource.of(ItemStack.EMPTY), 0);
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, Slot slotIn) {
        return slotIn.container == this.playerInventory;
    }

    @Override
    public boolean canDragTo(@NotNull Slot slotIn) {
        return true;
    }

    @Override
    public void clicked(int slotId, int dragType, @NotNull ContainerInput clickTypeIn, @NotNull Player player) {
        if (slotId < 36) {
            super.clicked(slotId, dragType, clickTypeIn, player);
            return;
        }
        if (clickTypeIn == ContainerInput.THROW)
            return;

        ItemStack held = this.getCarried();
        int slot = slotId - 36;
        if (clickTypeIn == ContainerInput.CLONE) {
            if (player.isCreative() && held.isEmpty()) {
                ItemStack stackInSlot = ItemUtil.getStack(this.ghostInventory, slot).copy();
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
        this.ghostInventory.set(slot, ItemResource.of(insert), insert.getCount());
        this.getSlot(slotId).setChanged();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        if (index < 36) {
            ItemStack stackToInsert = this.playerInventory.getItem(index);
            for (int i = 0; i < this.ghostInventory.size(); i++) {
                ItemStack stack = ItemUtil.getStack(this.ghostInventory, i);
                if (!this.allowRepeats() && ItemStack.isSameItemSameComponents(stack, stackToInsert))
                    break;
                if (stack.isEmpty()) {
                    ItemStack copy = stackToInsert.copy();
                    copy.setCount(1);
                    try (var tx = Transaction.openRoot()) {
                        this.ghostInventory.insert(ItemResource.of(copy), 1, tx);
                        tx.commit();
                    }
                    this.getSlot(i + 36).setChanged();
                    break;
                }
            }
        } else {
            try (var tx = Transaction.openRoot()) {
                this.ghostInventory.extract(ItemResource.of(ItemUtil.getStack(this.ghostInventory, index - 36)), 1, tx);
                tx.commit();
            }
            this.getSlot(index).setChanged();
        }
        return ItemStack.EMPTY;
    }

}
