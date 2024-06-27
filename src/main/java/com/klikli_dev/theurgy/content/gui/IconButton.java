// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;

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
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.visible) {
            this.isHovered = pMouseX >= this.getX() && pMouseY >= this.getY() && pMouseX < this.getX() + this.width && pMouseY < this.getY() + this.height;

            GuiTextures button = !this.active ? GuiTextures.BUTTON_DOWN
                    : this.isMouseOver(pMouseX, pMouseY) ? GuiTextures.BUTTON_HOVER : GuiTextures.BUTTON;

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            //draw button background
            pGuiGraphics.blit(button.location, this.getX(), this.getY(), button.x, button.y, button.width, button.height);
            this.icon.render(pGuiGraphics, this.getX() + 1, this.getY() + 1);
        }
    }
}
