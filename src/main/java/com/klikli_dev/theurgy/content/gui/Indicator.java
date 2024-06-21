package com.klikli_dev.theurgy.content.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class Indicator extends AbstractButton {

    public State state;

    public Indicator(int x, int y, Component tooltip) {
        super(x, y, GuiTextures.INDICATOR.width, GuiTextures.INDICATOR.height);
        this.state = State.OFF;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (!this.visible)
            return;
        GuiTextures toDraw = switch (this.state) {
            case ON -> GuiTextures.INDICATOR_WHITE;
            case OFF -> GuiTextures.INDICATOR;
            case RED -> GuiTextures.INDICATOR_RED;
            case YELLOW -> GuiTextures.INDICATOR_YELLOW;
            case GREEN -> GuiTextures.INDICATOR_GREEN;
            default -> GuiTextures.INDICATOR;
        };
        toDraw.render(pGuiGraphics, this.getX(), this.getY());
    }

    public enum State {
        OFF, ON,
        RED, YELLOW, GREEN
    }
}
