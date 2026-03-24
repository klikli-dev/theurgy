// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface ScreenElement {
    void render(GuiGraphicsExtractor graphics, int x, int y);
}