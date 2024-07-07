package com.klikli_dev.theurgy.content.gui;

import net.minecraft.client.gui.components.WidgetTooltipHolder;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import org.jetbrains.annotations.NotNull;

public class WidgetTooltipHolderWithDefaultPositioner extends WidgetTooltipHolder {

    @Override
    public @NotNull ClientTooltipPositioner createTooltipPositioner(@NotNull ScreenRectangle pScreenRectangle, boolean pHovering, boolean pFocused) {
        return DefaultTooltipPositioner.INSTANCE;
    }
}
