// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import com.klikli_dev.theurgy.tooltips.TooltipHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A tooltip class that handles tooltips that differ if shift is pressed.
 */
public class DynamicTooltip extends Tooltip {
    private final Component shiftDownMessage;

    @Nullable
    public List<FormattedCharSequence> shiftDownCachedTooltip;

    private DynamicTooltip(Component pMessage, Component shiftDownMessage) {
        super(pMessage, pMessage);
        this.shiftDownMessage = shiftDownMessage;
    }

    public static DynamicTooltip create(Component pMessage, Component shiftDownMessage) {
        return new DynamicTooltip(pMessage, shiftDownMessage);
    }

    public static Tooltip create(Component pMessage) {
        return new Tooltip(pMessage, pMessage);
    }

    @Override
    public List<FormattedCharSequence> toCharSequence(Minecraft pMinecraft) {
        var showMoreComponent = TooltipHandler.holdShift();

        Language language = Language.getInstance();
        if (Screen.hasShiftDown()) {
            if (this.shiftDownCachedTooltip == null || language != this.splitWithLanguage) {
                this.shiftDownCachedTooltip = splitTooltip(pMinecraft, Component.empty().append(this.message).append("\n").append(this.shiftDownMessage));
                this.splitWithLanguage = language;
            }
            return this.shiftDownCachedTooltip;
        } else {
            if (this.cachedTooltip == null || language != this.splitWithLanguage) {
                this.cachedTooltip = splitTooltip(pMinecraft, Component.empty().append(this.message).append("\n").append(showMoreComponent));
                this.splitWithLanguage = language;
            }
            return this.cachedTooltip;
        }
    }
}
