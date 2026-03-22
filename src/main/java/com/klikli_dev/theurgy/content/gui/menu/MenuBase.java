// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public abstract class MenuBase<T> extends AbstractContainerMenu {

    public Player player;
    public Inventory playerInventory;
    public T contentHolder;

    protected MenuBase(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(type, id);
        this.init(inv, this.createOnClient(extraData));
    }

    protected MenuBase(MenuType<?> type, int id, Inventory inv, T contentHolder) {
        super(type, id);
        this.init(inv, contentHolder);
    }

    protected void init(Inventory inv, T contentHolderIn) {
        this.player = inv.player;
        this.playerInventory = inv;
        this.contentHolder = contentHolderIn;
        this.initAndReadInventory(this.contentHolder);
        this.addSlots();
        this.broadcastChanges();
    }

    protected abstract T createOnClient(RegistryFriendlyByteBuf extraData);

    protected abstract void initAndReadInventory(T contentHolder);

    protected abstract void addSlots();

    protected abstract void saveData(T contentHolder);

    protected void addPlayerSlots(int x, int y) {
        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot)
            this.addSlot(new Slot(this.playerInventory, hotbarSlot, x + hotbarSlot * 18, y + 58));
        for (int row = 0; row < 3; ++row)
            for (int col = 0; col < 9; ++col)
                this.addSlot(new Slot(this.playerInventory, col + row * 9 + 9, x + col * 18, y + row * 18));
    }

    @Override
    public void removed(@NotNull Player playerIn) {
        super.removed(playerIn);
        this.saveData(this.contentHolder);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.contentHolder != null;
    }

}
