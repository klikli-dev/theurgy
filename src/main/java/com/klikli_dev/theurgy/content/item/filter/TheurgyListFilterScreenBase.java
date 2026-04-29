// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.codedefinedgui.gui.texture.GuiSprite;
import com.klikli_dev.codedefinedgui.gui.widget.IconButtonBackgroundSprites;
import com.klikli_dev.theurgy.content.gui.GuiSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

class TheurgyListFilterScreenBase extends com.klikli_dev.codedefinedgui.gui.filter.ListFilterScreen<ListFilterMenu> {
    private static final IconButtonBackgroundSprites BUTTON_BACKGROUND_SPRITES = new IconButtonBackgroundSprites(
            GuiSprites.BUTTON,
            GuiSprites.BUTTON_DOWN,
            GuiSprites.BUTTON_HOVER
    );

    protected TheurgyListFilterScreenBase(ListFilterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected IconButtonBackgroundSprites buttonBackgroundSprites() {
        return BUTTON_BACKGROUND_SPRITES;
    }

    @Override
    protected GuiSprite inventorySlotSprite() {
        return GuiSprites.INVENTORY_SLOT;
    }

    @Override
    protected GuiSprite filterSlotSprite() {
        return GuiSprites.INVENTORY_SLOT;
    }
}
