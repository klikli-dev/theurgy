// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.codedefinedgui.gui.texture.GuiSprite;
import com.klikli_dev.codedefinedgui.gui.texture.GuiSprites;
import com.klikli_dev.codedefinedgui.gui.widget.IconButtonBackgroundSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.klikli_dev.theurgy.content.gui.GuiSprites.ATTRIBUTE_FILTER_SUMMARY;

class TheurgyAttributeFilterScreenBase extends com.klikli_dev.codedefinedgui.gui.filter.AttributeFilterScreen<AttributeFilterMenu> {
    protected static final int BACKGROUND_TINT = 0xFFC2AA88;
    private static final int SLOT_TINT = 0xFFB8946A;
    private static final int BUTTON_TINT = 0xFFB78F63;
    private static final int BUTTON_HOVER_TINT = 0xFFC89E70;
    private static final GuiSprite TINTED_BACKGROUND = GuiSprites.GUI_BACKGROUND.tinted(BACKGROUND_TINT);
    private static final GuiSprite TINTED_SLOT = GuiSprites.INVENTORY_SLOT.tinted(SLOT_TINT);
    private static final GuiSprite TINTED_ATTRIBUTE_SELECTION = GuiSprites.ATTRIBUTE_FILTER_SELECTION.tinted(SLOT_TINT);
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
        return TINTED_BACKGROUND;
    }

    @Override
    protected GuiSprite playerInventorySlotSprite() {
        return TINTED_SLOT;
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
        return ATTRIBUTE_FILTER_SUMMARY;
    }
}
