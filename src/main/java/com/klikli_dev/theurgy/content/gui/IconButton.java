// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;

public class IconButton extends AbstractButton {

    protected ScreenElement icon;

    public IconButton(int x, int y, ScreenElement icon) {
        this(x, y, 18, 18, icon);
    }

    public IconButton(int x, int y, int w, int h, ScreenElement icon) {
        super(x, y, w, h);
        this.icon = icon;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.isHovered = pMouseX >= this.getX() && pMouseY >= this.getY() && pMouseX < this.getX() + this.width && pMouseY < this.getY() + this.height;

        GuiTextures button = !this.active ? GuiTextures.BUTTON_DOWN
                : this.isMouseOver(pMouseX, pMouseY) ? GuiTextures.BUTTON_HOVER : GuiTextures.BUTTON;

        //draw button background
        pGuiGraphics.blit(RenderPipelines.GUI_TEXTURED, button.location, this.getX(), this.getY(), (float) button.x, (float) button.y, button.width, button.height, 256, 256);
        this.icon.render(pGuiGraphics, this.getX() + 1, this.getY() + 1);
    }
}
