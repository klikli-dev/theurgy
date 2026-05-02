// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.codedefinedgui.filter.core.layout.BuiltinSlotRoles;
import com.klikli_dev.theurgy.registry.MenuTypeRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;

class TheurgyAttributeFilterMenuBase extends com.klikli_dev.codedefinedgui.filter.attribute.AttributeFilterMenu {
    private static final int REFERENCE_SLOT = 0;
    private static final int SUMMARY_SLOT = 1;

    protected TheurgyAttributeFilterMenuBase(int containerId, Inventory inventory, InteractionHand hand) {
        super(MenuTypeRegistry.ATTRIBUTE_FILTER.get(), containerId, inventory, hand);
    }

    @Override
    protected void addFilterSlots() {
        this.addGhostSlot(REFERENCE_SLOT, 19, 24, BuiltinSlotRoles.FILTER_REFERENCE, FilterScreenStyle.TINTED_FILTER_SLOT_SKIN);
        this.addGhostSlot(SUMMARY_SLOT, 22, 59, BuiltinSlotRoles.FILTER_SUMMARY, FilterScreenStyle.TINTED_FILTER_SLOT_SKIN);
    }
}
