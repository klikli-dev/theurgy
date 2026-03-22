// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.theurgy.content.gui.menu.GhostItemMenu;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFilterMenu extends GhostItemMenu<ItemStack> {

    protected AbstractFilterMenu(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    protected AbstractFilterMenu(MenuType<?> type, int id, Inventory inv, ItemStack contentHolder) {
        super(type, id, inv, contentHolder);
    }

    @Override
    protected abstract SettableItemStorage createGhostInventory();

    @Override
    protected boolean allowRepeats() {
        return false;
    }

    @Override
    protected ItemStack createOnClient(RegistryFriendlyByteBuf extraData) {
        return ItemStack.STREAM_CODEC.decode(extraData);
    }

    @Override
    protected void addSlots() {
        this.addPlayerSlots(this.getPlayerInventoryXOffset(), this.getPlayerInventoryYOffset());
        this.addFilterSlots();
    }

    @Override
    protected void saveData(ItemStack contentHolder) {
        //no need to do anything as our item handler will be a component item handler that auto-saves
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.playerInventory.getSelectedItem() == this.contentHolder;
    }

    @Override
    public void clicked(int slotId, int dragType, @NotNull ContainerInput clickTypeIn, @NotNull Player player) {
        if (slotId == this.playerInventory.getSelectedSlot() && clickTypeIn != ContainerInput.THROW)
            return;
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    protected abstract int getPlayerInventoryXOffset();

    protected abstract int getPlayerInventoryYOffset();

    protected abstract void addFilterSlots();

}
