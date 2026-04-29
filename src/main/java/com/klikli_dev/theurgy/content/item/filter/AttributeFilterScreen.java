// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.codedefinedgui.gui.texture.GuiSprites;
import com.klikli_dev.codedefinedgui.gui.widget.GuiBackgroundWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AttributeFilterScreen extends TheurgyAttributeFilterScreenBase {
    private static final int FILTER_BACKGROUND_HEIGHT = 85;
    private static final int TOP_SECTION_HEIGHT = 15;
    private static final int MIDDLE_SECTION_HEIGHT = 34;
    private static final int BOTTOM_SECTION_HEIGHT = FILTER_BACKGROUND_HEIGHT - TOP_SECTION_HEIGHT - MIDDLE_SECTION_HEIGHT;
    private static final int LIGHT_BLUE_TINT = 0xFFD9E8FF;
    private static final int DARK_TINT = 0xFFB0B0B0;

    public AttributeFilterScreen(AttributeFilterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void addBackgroundWidgets() {
        this.root.addChild(new GuiBackgroundWidget(this, this.leftPos, this.topPos, this.imageWidth, TOP_SECTION_HEIGHT, GuiSprites.GUI_BACKGROUND.tinted(LIGHT_BLUE_TINT)));
        this.root.addChild(new GuiBackgroundWidget(this, this.leftPos + 3, this.topPos + TOP_SECTION_HEIGHT, this.imageWidth - 6, MIDDLE_SECTION_HEIGHT, com.klikli_dev.theurgy.content.gui.GuiSprites.GUI_BACKGROUND));
        this.root.addChild(new GuiBackgroundWidget(this, this.leftPos, this.topPos + TOP_SECTION_HEIGHT + MIDDLE_SECTION_HEIGHT, this.imageWidth, BOTTOM_SECTION_HEIGHT, com.klikli_dev.theurgy.content.gui.GuiSprites.GUI_BACKGROUND.tinted(DARK_TINT)));
    }
}
