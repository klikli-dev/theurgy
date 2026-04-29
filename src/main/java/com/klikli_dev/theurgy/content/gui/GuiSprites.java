package com.klikli_dev.theurgy.content.gui;

import com.klikli_dev.codedefinedgui.gui.texture.GuiSprite;
import com.klikli_dev.theurgy.Theurgy;

public class GuiSprites {
    public static final GuiSprite GUI_BACKGROUND = new GuiSprite(Theurgy.loc("core/gui_background"), 16, 16);
    public static final GuiSprite INVENTORY_SLOT = new GuiSprite(Theurgy.loc("core/inventory_slot"), 18, 18);

    public static final GuiSprite BUTTON = new GuiSprite(Theurgy.loc("widget/button"), 16, 16);
    public static final GuiSprite BUTTON_DOWN = new GuiSprite(Theurgy.loc("widget/button_down"), 16, 16);
    public static final GuiSprite BUTTON_HOVER = new GuiSprite(Theurgy.loc("widget/button_hover"), 16, 16);

    public static final GuiSprite ATTRIBUTE_FILTER_SELECTION = new GuiSprite(Theurgy.loc("filter/attribute_filter_selection"), 137, 18);
    public static final GuiSprite ATTRIBUTE_FILTER_SUMMARY = new GuiSprite(Theurgy.loc("filter/attribute_filter_summary"), 24, 24);
}
