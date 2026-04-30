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
    protected static final int BACKGROUND_TINT = 0xFFC2AA88;
    protected static final int TOP_BAR_TINT = 0xFFD9E8FF;
    private static final int SLOT_TINT = 0xFFB8946A;
    private static final int BUTTON_TINT = 0xFFB78F63;
    private static final int BUTTON_HOVER_TINT = 0xFFC89E70;
    private static final GuiSprite TINTED_SLOT = GuiSprites.INVENTORY_SLOT.tinted(SLOT_TINT);
    private static final GuiSprite TINTED_ATTRIBUTE_SELECTION = GuiSprites.ATTRIBUTE_FILTER_SELECTION.tinted(SLOT_TINT);
    private static final GuiSprite TINTED_ATTRIBUTE_SUMMARY = GuiSprites.INVENTORY_SLOT.tinted(SLOT_TINT).sized(24, 24);
    private static final IconButtonBackgroundSprites BUTTON_BACKGROUND_SPRITES = new IconButtonBackgroundSprites(
            GuiSprites.FILTER_BUTTON.tinted(BUTTON_TINT),
            GuiSprites.FILTER_BUTTON_DOWN.tinted(BUTTON_TINT),
            GuiSprites.FILTER_BUTTON_HOVER.tinted(BUTTON_HOVER_TINT)
    );

    protected TheurgyAttributeFilterScreenBase(AttributeFilterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected IconButtonBackgroundSprites buttonBackgroundSprites() {
        return BUTTON_BACKGROUND_SPRITES;
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
        return TINTED_SLOT;
    }

    @Override
    protected GuiSprite attributeSelectionSprite() {
        return TINTED_ATTRIBUTE_SELECTION;
    }

    @Override
    protected GuiSprite attributeSummarySprite() {
        return TINTED_ATTRIBUTE_SUMMARY;
    }
}
