package com.klikli_dev.theurgy.content.gui;

import net.minecraft.client.gui.components.events.GuiEventListener;

public interface TickableGuiEventListener extends GuiEventListener {
    void tick();
}
