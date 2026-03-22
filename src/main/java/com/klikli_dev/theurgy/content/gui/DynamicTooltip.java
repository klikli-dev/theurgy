// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import com.klikli_dev.theurgy.tooltips.TooltipHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

/**
 * Utility for creating tooltips that expand when shift is held.
 */
public final class DynamicTooltip {
    private DynamicTooltip() {
    }

    public static Tooltip create(Component pMessage, Component shiftDownMessage) {
        if (Minecraft.getInstance().hasShiftDown()) {
            return Tooltip.create(Component.empty().append(pMessage).append("\n").append(shiftDownMessage));
        }

        return Tooltip.create(Component.empty().append(pMessage).append("\n").append(TooltipHandler.holdShift()));
    }

    public static Tooltip create(Component pMessage) {
        return Tooltip.create(pMessage);
    }
}
