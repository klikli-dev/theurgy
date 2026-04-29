// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;

public class AttributeFilterMenu extends TheurgyAttributeFilterMenuBase {
    public AttributeFilterMenu(int containerId, Inventory inventory, InteractionHand hand) {
        super(containerId, inventory, hand);
    }

    public static AttributeFilterMenu create(int containerId, Inventory inventory, RegistryFriendlyByteBuf extraData) {
        return new AttributeFilterMenu(containerId, inventory, extraData.readEnum(InteractionHand.class));
    }
}
