// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.codedefinedgui.filter.core.layout.BuiltinSlotRoles;
import com.klikli_dev.theurgy.registry.MenuTypeRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;

class TheurgyListFilterMenuBase extends com.klikli_dev.codedefinedgui.filter.list.ListFilterMenu {
    private static final int FILTER_SLOTS = 18;

    protected TheurgyListFilterMenuBase(int containerId, Inventory inventory, InteractionHand hand) {
        super(MenuTypeRegistry.LIST_FILTER.get(), containerId, inventory, hand);
    }

    @Override
    protected void addFilterSlots() {
        int x = 25;
        int y = 24;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 9; col++) {
                this.addGhostSlot(col + row * 9, x + col * 18, y + row * 18, BuiltinSlotRoles.FILTER_GRID, FilterScreenStyle.TINTED_FILTER_SLOT_SKIN);
            }
        }
    }
}
