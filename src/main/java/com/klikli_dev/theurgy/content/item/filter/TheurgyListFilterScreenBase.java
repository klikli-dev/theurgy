// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

class TheurgyListFilterScreenBase extends com.klikli_dev.codedefinedgui.gui.filter.ListFilterScreen<ListFilterMenu> {
    protected TheurgyListFilterScreenBase(ListFilterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
}
