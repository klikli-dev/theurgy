// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.registry.MenuTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ListFilterMenu extends AbstractFilterMenu {

    public boolean respectDataComponents;
    public boolean isDenyList;


    public ListFilterMenu(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public ListFilterMenu(MenuType<?> type, int id, Inventory inv, ItemStack contentHolder) {
        super(type, id, inv, contentHolder);
    }

    public static ListFilterMenu create(int id, Inventory inv, ItemStack stack) {
        return new ListFilterMenu(MenuTypeRegistry.LIST_FILTER.get(), id, inv, stack);
    }

    public static ListFilterMenu create(int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        return new ListFilterMenu(MenuTypeRegistry.LIST_FILTER.get(), id, inv, extraData);
    }

    @Override
    protected ComponentItemHandler createGhostInventory() {
        return new ComponentItemHandler(this.contentHolder, DataComponentRegistry.FILTER_ITEMS.get(), 18);
    }

    @Override
    protected int getPlayerInventoryXOffset() {
        return 27;
    }

    @Override
    protected int getPlayerInventoryYOffset() {
        return 121;
    }

    @Override
    protected void addFilterSlots() {
        int x = 23;
        int y = 22;
        for (int row = 0; row < 2; ++row)
            for (int col = 0; col < 9; ++col)
                this.addSlot(new SlotItemHandler(this.ghostInventory, col + row * 9, x + col * 18, y + row * 18));
    }

    @Override
    protected void initAndReadInventory(ItemStack contentHolder) {
        super.initAndReadInventory(contentHolder);

        this.respectDataComponents = contentHolder.getOrDefault(DataComponentRegistry.FILTER_RESPECTS_DATA_COMPONENTS, false);
        this.isDenyList = contentHolder.getOrDefault(DataComponentRegistry.FILTER_IS_DENY_LIST, false);
    }

    @Override
    protected void saveData(ItemStack contentHolder) {
        contentHolder.set(DataComponentRegistry.FILTER_RESPECTS_DATA_COMPONENTS, this.respectDataComponents);
        contentHolder.set(DataComponentRegistry.FILTER_IS_DENY_LIST, this.isDenyList);
    }
}
