// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

class TheurgyAttributeFilterScreenBase extends com.klikli_dev.codedefinedgui.gui.filter.AttributeFilterScreen<AttributeFilterMenu> {
    protected TheurgyAttributeFilterScreenBase(AttributeFilterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
}
