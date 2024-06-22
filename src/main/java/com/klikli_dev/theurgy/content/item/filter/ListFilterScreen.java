package com.klikli_dev.theurgy.content.item.filter;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ListFilterScreen extends AbstractFilterScreen<ListFilterMenu>{
    public ListFilterScreen(ListFilterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
}
