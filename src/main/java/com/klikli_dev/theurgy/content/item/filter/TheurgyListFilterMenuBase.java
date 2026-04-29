// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.theurgy.registry.MenuTypeRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;

class TheurgyListFilterMenuBase extends com.klikli_dev.codedefinedgui.filter.list.ListFilterMenu {
    protected TheurgyListFilterMenuBase(int containerId, Inventory inventory, InteractionHand hand) {
        super(MenuTypeRegistry.LIST_FILTER.get(), containerId, inventory, hand);
    }
}
