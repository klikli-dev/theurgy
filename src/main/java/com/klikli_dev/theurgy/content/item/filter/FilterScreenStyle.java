// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.codedefinedgui.gui.texture.GuiSprite;
import com.klikli_dev.codedefinedgui.gui.texture.GuiSprites;
import com.klikli_dev.codedefinedgui.gui.widget.IconButtonBackgroundSprites;

final class FilterScreenStyle {
    static final int BACKGROUND_TINT = 0xFFC2AA88;
    static final int TOP_BAR_TINT = 0xFFC4D8F5;

    private static final int SLOT_TINT = 0xFFB8946A;
    private static final int BUTTON_TINT = 0xFFB78F63;
    private static final int BUTTON_HOVER_TINT = 0xFFC89E70;

    static final GuiSprite TINTED_FILTER_SLOT = GuiSprites.INVENTORY_SLOT.tinted(SLOT_TINT);
    static final GuiSprite TINTED_ATTRIBUTE_SELECTION = GuiSprites.ATTRIBUTE_FILTER_SELECTION.tinted(SLOT_TINT);
    static final GuiSprite TINTED_ATTRIBUTE_SUMMARY = GuiSprites.INVENTORY_SLOT.tinted(SLOT_TINT).sized(24, 24);
    static final IconButtonBackgroundSprites TINTED_BUTTON_BACKGROUNDS = new IconButtonBackgroundSprites(
            GuiSprites.FILTER_BUTTON.tinted(BUTTON_TINT),
            GuiSprites.FILTER_BUTTON_DOWN.tinted(BUTTON_TINT),
            GuiSprites.FILTER_BUTTON_HOVER.tinted(BUTTON_HOVER_TINT)
    );

    private FilterScreenStyle() {
    }
}
