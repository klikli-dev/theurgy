// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class Label extends AbstractButton {

    public Component text;
    public String suffix;
    protected boolean hasShadow;
    protected int color;
    protected Font font;

    public Label(int x, int y, Component text) {
        super(x, y, Minecraft.getInstance().font.width(text), 10);
        this.font = Minecraft.getInstance().font;
        this.text = Component.literal("Label");
        this.color = 0xFFFFFF;
        this.hasShadow = false;
        this.suffix = "";
    }

    public Label colored(int color) {
        this.color = color;
        return this;
    }

    public Label withShadow() {
        this.hasShadow = true;
        return this;
    }

    public Label withSuffix(String s) {
        this.suffix = s;
        return this;
    }

    public void setTextAndTrim(Component newText, boolean trimFront, int maxWidthPx) {
        Font fontRenderer = Minecraft.getInstance().font;

        if (fontRenderer.width(newText) <= maxWidthPx) {
            this.text = newText;
            return;
        }

        String trim = "...";
        int trimWidth = fontRenderer.width(trim);

        String raw = newText.getString();
        StringBuilder builder = new StringBuilder(raw);
        int startIndex = trimFront ? 0 : raw.length() - 1;
        int endIndex = !trimFront ? 0 : raw.length() - 1;
        int step = (int) Math.signum(endIndex - startIndex);

        for (int i = startIndex; i != endIndex; i += step) {
            String sub = builder.substring(trimFront ? i : startIndex, trimFront ? endIndex + 1 : i + 1);
            if (fontRenderer.width(Component.literal(sub).setStyle(newText.getStyle())) + trimWidth <= maxWidthPx) {
                this.text = Component.literal(trimFront ? trim + sub : sub + trim).setStyle(newText.getStyle());
                return;
            }
        }

    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.text == null || this.text.getString().isEmpty())
            return;

        RenderSystem.setShaderColor(1, 1, 1, 1);
        MutableComponent copy = this.text.plainCopy();
        if (this.suffix != null && !this.suffix.isEmpty())
            copy.append(this.suffix);

        pGuiGraphics.drawString(this.font, copy, this.getX(), this.getY(), this.color, this.hasShadow);
    }
}
