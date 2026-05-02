// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.codedefinedgui.gui.texture.GuiSprite;
import com.klikli_dev.codedefinedgui.gui.texture.GuiSprites;
import com.klikli_dev.codedefinedgui.gui.widget.IconButtonBackgroundSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

class TheurgyAttributeFilterScreenBase extends com.klikli_dev.codedefinedgui.gui.filter.AttributeFilterScreen<AttributeFilterMenu> {
    protected static final int BACKGROUND_TINT = FilterScreenStyle.BACKGROUND_TINT;
    protected static final int TOP_BAR_TINT = FilterScreenStyle.TOP_BAR_TINT;

    protected TheurgyAttributeFilterScreenBase(AttributeFilterMenu menu, Inventory playerInventory, Component title) {
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
    protected GuiSprite attributeSelectionSprite() {
        return FilterScreenStyle.TINTED_ATTRIBUTE_SELECTION;
    }

    @Override
    protected GuiSprite attributeSummarySprite() {
        return FilterScreenStyle.TINTED_ATTRIBUTE_SUMMARY;
    }
}
