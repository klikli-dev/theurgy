// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.codedefinedgui.gui.texture.GuiSprite;
import com.klikli_dev.codedefinedgui.gui.texture.GuiSprites;
import com.klikli_dev.codedefinedgui.gui.widget.IconButtonBackgroundSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

class TheurgyListFilterScreenBase extends com.klikli_dev.codedefinedgui.gui.filter.ListFilterScreen<ListFilterMenu> {
    protected TheurgyListFilterScreenBase(ListFilterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected IconButtonBackgroundSprites buttonBackgroundSprites() {
        return FilterScreenStyle.TINTED_BUTTON_BACKGROUNDS;
    }

    @Override
    protected GuiSprite playerInventoryBackgroundSprite() {
        return GuiSprites.GUI_BACKGROUND;
    }

    @Override
    protected GuiSprite playerInventorySlotSprite() {
        return GuiSprites.INVENTORY_SLOT;
    }

    @Override
    protected GuiSprite filterSlotSprite() {
        return FilterScreenStyle.TINTED_FILTER_SLOT;
    }
}
