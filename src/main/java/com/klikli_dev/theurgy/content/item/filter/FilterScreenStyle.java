// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.codedefinedgui.filter.core.layout.BuiltinSlotRoles;
import com.klikli_dev.codedefinedgui.filter.core.layout.MenuSlotView;
import com.klikli_dev.codedefinedgui.gui.filter.AttributeFilterScreen;
import com.klikli_dev.codedefinedgui.gui.filter.FilterUiStyle;
import com.klikli_dev.codedefinedgui.gui.filter.FilterUiStyleKey;
import com.klikli_dev.codedefinedgui.gui.filter.FilterUiStyleRegistry;
import com.klikli_dev.codedefinedgui.gui.filter.ListFilterScreen;
import com.klikli_dev.codedefinedgui.gui.filter.SlotSkinRenderer;
import com.klikli_dev.codedefinedgui.gui.texture.GuiSprite;
import com.klikli_dev.codedefinedgui.gui.texture.GuiSprites;
import com.klikli_dev.codedefinedgui.gui.widget.GuiBackgroundWidget;
import com.klikli_dev.codedefinedgui.gui.widget.HorizontalSeparatorWidget;
import com.klikli_dev.codedefinedgui.gui.widget.IconButtonBackgroundSprites;
import com.klikli_dev.codedefinedgui.gui.widget.VerticalSeparatorWidget;

public final class FilterScreenStyle implements FilterUiStyle {
    private static final int BLACK = 0xFF000000;
    private static final int BACKGROUND_TINT = 0xFFC2AA88;
    private static final int TOP_BAR_TINT = 0xFFC4D8F5;
    private static final int LIST_TOP_SECTION_HEIGHT = 15;
    private static final int LIST_SECOND_BACKGROUND_X_OFFSET = 3;
    private static final int LIST_SECOND_BACKGROUND_Y_OFFSET = LIST_TOP_SECTION_HEIGHT - 3;
    private static final int LIST_SECOND_BACKGROUND_WIDTH_OFFSET = 6;
    private static final int LIST_MIDDLE_SECTION_HEIGHT = 87;
    private static final int LIST_HORIZONTAL_SEPARATOR_Y = 64;
    private static final int LIST_VERTICAL_SEPARATOR_X = 145;
    private static final int ATTRIBUTE_TOP_SECTION_HEIGHT = 15;
    private static final int ATTRIBUTE_MIDDLE_SECTION_HEIGHT = 75;
    private static final int ATTRIBUTE_SECOND_BACKGROUND_X_OFFSET = 3;
    private static final int ATTRIBUTE_SECOND_BACKGROUND_Y_OFFSET = ATTRIBUTE_TOP_SECTION_HEIGHT - 3;
    private static final int ATTRIBUTE_SECOND_BACKGROUND_WIDTH_OFFSET = 6;
    private static final int ATTRIBUTE_HORIZONTAL_SEPARATOR_Y = 48;
    private static final int ATTRIBUTE_VERTICAL_SEPARATOR_X = 202;

    private static final int SLOT_TINT = 0xFFB8946A;
    private static final int BUTTON_TINT = 0xFFB78F63;
    private static final int BUTTON_HOVER_TINT = 0xFFC89E70;

    private static final GuiSprite TINTED_FILTER_SLOT = GuiSprites.INVENTORY_SLOT.tinted(SLOT_TINT);
    private static final GuiSprite TINTED_ATTRIBUTE_SELECTION = GuiSprites.ATTRIBUTE_FILTER_SELECTION.tinted(SLOT_TINT);
    private static final GuiSprite TINTED_ATTRIBUTE_SUMMARY = GuiSprites.INVENTORY_SLOT.tinted(SLOT_TINT).sized(24, 24);
    private static final SlotSkinRenderer TINTED_FILTER_SLOT_RENDERER = SlotSkinRenderer.create(TINTED_FILTER_SLOT, 1, 1);
    private static final IconButtonBackgroundSprites TINTED_BUTTON_BACKGROUNDS = new IconButtonBackgroundSprites(
            GuiSprites.FILTER_BUTTON.tinted(BUTTON_TINT),
            GuiSprites.FILTER_BUTTON_DOWN.tinted(BUTTON_TINT),
            GuiSprites.FILTER_BUTTON_HOVER.tinted(BUTTON_HOVER_TINT)
    );
    private static final FilterScreenStyle INSTANCE = new FilterScreenStyle();

    private FilterScreenStyle() {
    }

    public static void register() {
        FilterUiStyleRegistry.register(FilterUiStyles.THEURGY, INSTANCE);
    }

    @Override
    public SlotSkinRenderer slotRenderer(MenuSlotView slotView) {
        if (slotView.role().equals(BuiltinSlotRoles.FILTER_GRID)
                || slotView.role().equals(BuiltinSlotRoles.FILTER_REFERENCE)
                || slotView.role().equals(BuiltinSlotRoles.FILTER_SUMMARY)) {
            return TINTED_FILTER_SLOT_RENDERER;
        }

        return FilterUiStyle.super.slotRenderer(slotView);
    }

    @Override
    public IconButtonBackgroundSprites buttonBackgroundSprites() {
        return TINTED_BUTTON_BACKGROUNDS;
    }

    @Override
    public int attributeTitleColor() {
        return 0x592424;
    }

    @Override
    public GuiSprite attributeSelectionSprite() {
        return TINTED_ATTRIBUTE_SELECTION;
    }

    @Override
    public GuiSprite attributeSummarySprite() {
        return TINTED_ATTRIBUTE_SUMMARY;
    }

    @Override
    public void addListBackgroundWidgets(ListFilterScreen<?> screen) {
        var tintedBackground = GuiSprites.GUI_BACKGROUND.tinted(BACKGROUND_TINT);
        var tintedTopBar = GuiSprites.GUI_BACKGROUND.tinted(TOP_BAR_TINT);

        screen.addRootChild(new GuiBackgroundWidget(
                screen,
                screen.leftPos() + LIST_SECOND_BACKGROUND_X_OFFSET,
                screen.topPos() + LIST_SECOND_BACKGROUND_Y_OFFSET,
                screen.imageWidth() - LIST_SECOND_BACKGROUND_WIDTH_OFFSET,
                LIST_MIDDLE_SECTION_HEIGHT,
                tintedBackground
        ));
        screen.addRootChild(new GuiBackgroundWidget(screen, screen.leftPos(), screen.topPos(), screen.imageWidth(), LIST_TOP_SECTION_HEIGHT, tintedTopBar));
    }

    @Override
    public void addListForegroundWidgets(ListFilterScreen<?> screen) {
        int secondBackgroundX = screen.leftPos() + LIST_SECOND_BACKGROUND_X_OFFSET;
        int secondBackgroundY = screen.topPos() + LIST_SECOND_BACKGROUND_Y_OFFSET;
        int secondBackgroundWidth = screen.imageWidth() - LIST_SECOND_BACKGROUND_WIDTH_OFFSET;
        int secondBackgroundBottom = secondBackgroundY + LIST_MIDDLE_SECTION_HEIGHT;
        int horizontalSeparatorY = screen.topPos() + LIST_HORIZONTAL_SEPARATOR_Y;

        screen.addRootChild(new HorizontalSeparatorWidget(secondBackgroundX, horizontalSeparatorY, secondBackgroundWidth));
        screen.addRootChild(new VerticalSeparatorWidget(screen.leftPos() + LIST_VERTICAL_SEPARATOR_X, horizontalSeparatorY, secondBackgroundBottom - horizontalSeparatorY, BLACK));
    }

    @Override
    public void addAttributeBackgroundWidgets(AttributeFilterScreen<?> screen) {
        var tintedBackground = GuiSprites.GUI_BACKGROUND.tinted(BACKGROUND_TINT);
        var tintedTopBar = GuiSprites.GUI_BACKGROUND.tinted(TOP_BAR_TINT);

        screen.addRootChild(new GuiBackgroundWidget(
                screen,
                screen.leftPos() + ATTRIBUTE_SECOND_BACKGROUND_X_OFFSET,
                screen.topPos() + ATTRIBUTE_SECOND_BACKGROUND_Y_OFFSET,
                screen.imageWidth() - ATTRIBUTE_SECOND_BACKGROUND_WIDTH_OFFSET,
                ATTRIBUTE_MIDDLE_SECTION_HEIGHT,
                tintedBackground
        ));
        screen.addRootChild(new GuiBackgroundWidget(screen, screen.leftPos(), screen.topPos(), screen.imageWidth(), ATTRIBUTE_TOP_SECTION_HEIGHT, tintedTopBar));
    }

    @Override
    public void addAttributeForegroundWidgets(AttributeFilterScreen<?> screen) {
        int secondBackgroundX = screen.leftPos() + ATTRIBUTE_SECOND_BACKGROUND_X_OFFSET;
        int secondBackgroundY = screen.topPos() + ATTRIBUTE_SECOND_BACKGROUND_Y_OFFSET;
        int secondBackgroundWidth = screen.imageWidth() - ATTRIBUTE_SECOND_BACKGROUND_WIDTH_OFFSET;
        int secondBackgroundBottom = secondBackgroundY + ATTRIBUTE_MIDDLE_SECTION_HEIGHT;
        int horizontalSeparatorY = screen.topPos() + ATTRIBUTE_HORIZONTAL_SEPARATOR_Y;

        screen.addRootChild(new HorizontalSeparatorWidget(secondBackgroundX, horizontalSeparatorY, secondBackgroundWidth));
        screen.addRootChild(new VerticalSeparatorWidget(screen.leftPos() + ATTRIBUTE_VERTICAL_SEPARATOR_X, horizontalSeparatorY, secondBackgroundBottom - horizontalSeparatorY, BLACK));
    }
}
