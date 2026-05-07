// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.codedefinedgui.api.style.BuiltinGuiParts;
import com.klikli_dev.codedefinedgui.api.style.GuiStyle;
import com.klikli_dev.codedefinedgui.api.style.GuiStyleProperties;
import com.klikli_dev.codedefinedgui.api.style.GuiStyleRegistry;
import com.klikli_dev.codedefinedgui.api.texture.GuiSprites;
import com.klikli_dev.codedefinedgui.premade.filter.core.layout.BuiltinFilterParts;

public final class FilterStyles {
    private static final int BACKGROUND_TINT = 0xFFC2AA88;
    private static final int TOP_BAR_TINT = 0xFFC4D8F5;
    private static final int SLOT_TINT = 0xFFB8946A;
    private static final int BUTTON_TINT = 0xFFB78F63;
    private static final int BUTTON_HOVER_TINT = 0xFFC89E70;
    private static final int BLACK = 0xFF000000;

    private FilterStyles() {
    }

    public static void register() {
        GuiStyleRegistry.register(FilterUiStyles.THEURGY_LIST, GuiStyle.builder()
                .set(BuiltinGuiParts.PLAYER_INVENTORY_BACKGROUND, GuiStyleProperties.SPRITE, GuiSprites.GUI_BACKGROUND.tinted(BACKGROUND_TINT))
                .set(BuiltinGuiParts.PLAYER_SLOT, GuiStyleProperties.SPRITE, GuiSprites.INVENTORY_SLOT.tinted(SLOT_TINT))
                .set(BuiltinFilterParts.FILTER_SLOT, GuiStyleProperties.SPRITE, GuiSprites.INVENTORY_SLOT.tinted(SLOT_TINT))
                .set(BuiltinFilterParts.BUTTON, GuiStyleProperties.SPRITE, GuiSprites.FILTER_BUTTON.tinted(BUTTON_TINT))
                .set(BuiltinFilterParts.BUTTON, GuiStyleProperties.PRESSED_SPRITE, GuiSprites.FILTER_BUTTON_DOWN.tinted(BUTTON_TINT))
                .set(BuiltinFilterParts.BUTTON, GuiStyleProperties.HOVER_SPRITE, GuiSprites.FILTER_BUTTON_HOVER.tinted(BUTTON_HOVER_TINT))
                .set(BuiltinFilterParts.LIST_PANEL, GuiStyleProperties.SPRITE, GuiSprites.GUI_BACKGROUND.tinted(BACKGROUND_TINT))
                .set(BuiltinFilterParts.LIST_TOP_BAR, GuiStyleProperties.SPRITE, GuiSprites.GUI_BACKGROUND.tinted(TOP_BAR_TINT))
                .set(BuiltinFilterParts.LIST_HORIZONTAL_SEPARATOR, GuiStyleProperties.COLOR, BLACK)
                .set(BuiltinFilterParts.LIST_VERTICAL_SEPARATOR, GuiStyleProperties.COLOR, BLACK)
                .set(BuiltinFilterParts.LIST_TITLE, GuiStyleProperties.TEXT_COLOR, 0x303030)
                .build());

        GuiStyleRegistry.register(FilterUiStyles.THEURGY_ATTRIBUTE, GuiStyle.builder()
                .set(BuiltinGuiParts.PLAYER_INVENTORY_BACKGROUND, GuiStyleProperties.SPRITE, GuiSprites.GUI_BACKGROUND.tinted(BACKGROUND_TINT))
                .set(BuiltinGuiParts.PLAYER_SLOT, GuiStyleProperties.SPRITE, GuiSprites.INVENTORY_SLOT.tinted(SLOT_TINT))
                .set(BuiltinFilterParts.FILTER_SLOT, GuiStyleProperties.SPRITE, GuiSprites.INVENTORY_SLOT.tinted(SLOT_TINT))
                .set(BuiltinFilterParts.BUTTON, GuiStyleProperties.SPRITE, GuiSprites.FILTER_BUTTON.tinted(BUTTON_TINT))
                .set(BuiltinFilterParts.BUTTON, GuiStyleProperties.PRESSED_SPRITE, GuiSprites.FILTER_BUTTON_DOWN.tinted(BUTTON_TINT))
                .set(BuiltinFilterParts.BUTTON, GuiStyleProperties.HOVER_SPRITE, GuiSprites.FILTER_BUTTON_HOVER.tinted(BUTTON_HOVER_TINT))
                .set(BuiltinFilterParts.ATTRIBUTE_PANEL, GuiStyleProperties.SPRITE, GuiSprites.GUI_BACKGROUND.tinted(BACKGROUND_TINT))
                .set(BuiltinFilterParts.ATTRIBUTE_TOP_BAR, GuiStyleProperties.SPRITE, GuiSprites.GUI_BACKGROUND.tinted(TOP_BAR_TINT))
                .set(BuiltinFilterParts.ATTRIBUTE_HORIZONTAL_SEPARATOR, GuiStyleProperties.COLOR, BLACK)
                .set(BuiltinFilterParts.ATTRIBUTE_VERTICAL_SEPARATOR, GuiStyleProperties.COLOR, BLACK)
                .set(BuiltinFilterParts.ATTRIBUTE_TITLE, GuiStyleProperties.TEXT_COLOR, 0x592424)
                .set(BuiltinFilterParts.ATTRIBUTE_SELECTION, GuiStyleProperties.SPRITE, GuiSprites.ATTRIBUTE_FILTER_SELECTION.tinted(SLOT_TINT))
                .set(BuiltinFilterParts.ATTRIBUTE_SUMMARY, GuiStyleProperties.SPRITE, GuiSprites.INVENTORY_SLOT.tinted(SLOT_TINT).sized(24, 24))
                .build());
    }
}
