// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiIcons implements ScreenElement {

    public static final GuiIcons TRASH = new GuiIcons(1, 0);
    public static final GuiIcons CONFIRM = new GuiIcons(0, 1);
    public static final GuiIcons DENY_LIST = new GuiIcons(8, 0);
    public static final GuiIcons ACCEPT_LIST = new GuiIcons(9, 0);
    public static final GuiIcons RESPECT_DATA_COMPONENTS = new GuiIcons(13, 0);
    public static final GuiIcons IGNORE_DATA_COMPONENTS = new GuiIcons(14, 0);
    private static final ResourceLocation TEXTURE = Theurgy.loc("textures/gui/icons.png");
    private final int iconX;
    private final int iconY;

    public GuiIcons(int x, int y) {
        this.iconX = x * 16;
        this.iconY = y * 16;
    }


    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        graphics.blit(TEXTURE, x, y, 0, this.iconX, this.iconY, 16, 16, 256, 256);
    }

}
