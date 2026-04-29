// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.theurgy.registry.MenuTypeRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;

class TheurgyAttributeFilterMenuBase extends com.klikli_dev.codedefinedgui.filter.attribute.AttributeFilterMenu {
    protected TheurgyAttributeFilterMenuBase(int containerId, Inventory inventory, InteractionHand hand) {
        super(MenuTypeRegistry.ATTRIBUTE_FILTER.get(), containerId, inventory, hand);
    }
}
