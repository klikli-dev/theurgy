package com.klikli_dev.theurgy.content.render;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.gui.overlay.ExtendedGui;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;

public class ModeItemOverlay implements IGuiOverlay {

    private static final ModeItemOverlay instance = new ModeItemOverlay();

    public static ModeItemOverlay get() {
        return instance;
    }

    @Override
    public void render(ExtendedGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {

    }
}
