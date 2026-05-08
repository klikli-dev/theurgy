// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public record GuiSprite(Identifier sprite, int width, int height, int tint) {
    public GuiSprite(Identifier sprite, int width, int height) {
        this(sprite, width, height, -1);
    }

    public GuiSprite sized(int width, int height) {
        return new GuiSprite(this.sprite, width, height, this.tint);
    }

    public GuiSprite tinted(int tint) {
        return new GuiSprite(this.sprite, this.width, this.height, tint);
    }

    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int x, int y) {
        this.extractRenderState(guiGraphics, x, y, this.width, this.height);
    }

    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, x, y, width, height, this.tint);
    }
}
